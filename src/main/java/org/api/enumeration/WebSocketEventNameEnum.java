package org.api.enumeration;

public enum WebSocketEventNameEnum {
    SUBSCRIBE(""),
    CONNECT("/chat/login"),
    DISCONNECT("/chat/logout"),
    MESSAGE("");

    private String destination;

    WebSocketEventNameEnum(String destination) {
        this.destination = destination;
    }

    public String getDestination() {
        return this.destination;
    }
}
