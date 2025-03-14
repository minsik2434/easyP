package com.easy_p.easyp.common.socket;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@Getter
public class WebSocketHandler extends TextWebSocketHandler {
    private List<WebSocketSession> sessions = new ArrayList<>();
    private Map<String, WebSocketSession> sessionMap = new HashMap<>();
    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        sessions.add(session);
        sessionMap.put(session.getPrincipal().getName(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        sessionMap.remove(session.getPrincipal().getName(), session);
    }
}
