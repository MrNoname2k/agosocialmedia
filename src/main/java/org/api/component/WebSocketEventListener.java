package org.api.component;

import org.api.constants.ConstantOnline;
import org.api.entities.UserEntity;
import org.api.enumeration.WebSocketEventNameEnum;
import org.api.payload.WebSocketMessage;
import org.api.services.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {
    @Autowired
    private UserEntityService userEntityService;

    @Autowired
    private SimpMessagingTemplate template;

    @EventListener
    private void handleSessionConnected(SessionConnectEvent event) throws Exception {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String username = headers.getUser().getName();
        UserEntity userServiceModel = userEntityService.updateOnline(username, ConstantOnline.ON);
        String userId = userServiceModel.getId();
        WebSocketMessage message = new WebSocketMessage(WebSocketEventNameEnum.CONNECT, userId, username, String.valueOf(ConstantOnline.ON));
        template.convertAndSend(WebSocketEventNameEnum.CONNECT.getDestination(), message);
    }

    @EventListener
    private void handleSessionDisconnect(SessionDisconnectEvent event) throws Exception {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String username = headers.getUser().getName();
        UserEntity userServiceModel = userEntityService.updateOnline(username, ConstantOnline.OFF);
        String userId = userServiceModel.getId();
        WebSocketMessage message = new WebSocketMessage(WebSocketEventNameEnum.DISCONNECT, userId, username, String.valueOf(ConstantOnline.OFF));
        template.convertAndSend(WebSocketEventNameEnum.DISCONNECT.getDestination(), message);
    }
}