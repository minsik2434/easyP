package com.easy_p.easyp.service.alarm;

import com.easy_p.easyp.common.socket.WebSocketHandler;
import com.easy_p.easyp.dto.response.AlarmDto;
import com.easy_p.easyp.service.AlarmService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {
    private final WebSocketHandler webSocketHandler;
    private ObjectMapper mapper = new ObjectMapper();
    @Override
    public void sendAlarm(String targetEmail, AlarmDto alarmDto) {
        Map<String, WebSocketSession> sessionMap = webSocketHandler.getSessionMap();
        WebSocketSession session = sessionMap.get(targetEmail);
        if(session == null){
            return;
        }
        String jsonResponse;
        try{
            jsonResponse = mapper.writeValueAsString(alarmDto);
            session.sendMessage(new TextMessage(jsonResponse));
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
