package org.api.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.api.enumeration.WebSocketEventNameEnum;

import java.io.Serializable;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private WebSocketEventNameEnum webSocketEventNameEnum;
    private String userId;
    private String username;
    private String online;
}
