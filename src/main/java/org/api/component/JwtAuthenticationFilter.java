package org.api.component;

import org.api.constants.ConstantJwt;
import org.api.constants.ConstantMessage;
import org.api.services.impl.CustomUserDetailsServiceImpl;
import org.api.utils.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsServiceImpl customUserDetailsServiceImpl;

    @Autowired
    private Environment evn;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            if (StringUtils.hasText(jwt) && tokenProvider.validateJwtToken(jwt)) {
                String mail = tokenProvider.getMailFromJwtToken(jwt);
                UserDetails userDetails = customUserDetailsServiceImpl.loadUserByUsername(mail);
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception ex) {
            log.error(MessageUtils.getMessage(ConstantMessage.ID_MESSAGE_SC_UNAUTHORIZED, null, null), ex);
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(evn.getProperty(ConstantJwt.HEADER_STRING));
        log.info("Token: " + bearerToken);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(evn.getProperty(ConstantJwt.TOKEN_PREFIX) + " ")) {
            String jwt = bearerToken.substring(7, bearerToken.length());
            return jwt;
        }
        return null;
    }

}