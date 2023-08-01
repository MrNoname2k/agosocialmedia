package org.api.configuration;

import org.api.component.JWTAuthenticationToken;
import org.api.component.JwtTokenProvider;
import org.api.constants.ConstantJwt;
import org.api.entities.UserEntity;
import org.api.services.CustomUserDetailsService;
import org.api.services.impl.CustomUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.Optional;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsServiceImpl customUserDetailsServiceImpl;

    @Autowired
    private Environment evn;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/socket")
                .setAllowedOrigins("http://localhost:4200")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app")
                .setUserDestinationPrefix("/user")
                .enableSimpleBroker("/chat", "/topic", "/queue", "/user");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    Optional.ofNullable(accessor.getNativeHeader(evn.getProperty(ConstantJwt.HEADER_STRING))).ifPresent(ah -> {
                        String bearerToken = ah.get(0).replace(evn.getProperty(ConstantJwt.TOKEN_PREFIX), "");
                        JWTAuthenticationToken token = getJWTAuthenticationToken(bearerToken);
                        accessor.setUser(token);
                    });
                }
                return message;
            }
        });
    }

    private JWTAuthenticationToken getJWTAuthenticationToken(String token) {
        if (StringUtils.hasText(token) && tokenProvider.validateJwtToken(token)) {
            String mail = tokenProvider.getMailFromJwtToken(token);
            CustomUserDetailsService userDetails = (CustomUserDetailsService) customUserDetailsServiceImpl.loadUserByUsername(mail);

            if (userDetails != null) {
                UserEntity user = userDetails.getUserEntity();
                JWTAuthenticationToken jwtAuthenticationToken = new JWTAuthenticationToken(userDetails.getAuthorities(), token, user);
                jwtAuthenticationToken.setAuthenticated(true);
                return jwtAuthenticationToken;
            }
        }
        return null;
    }
}
