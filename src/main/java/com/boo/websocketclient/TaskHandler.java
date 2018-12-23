package com.boo.websocketclient;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Log4j2
@Component
public class TaskHandler extends StompSessionHandlerAdapter {

    @Value("${topic_confirmation}")
    private String topicConfirmation;

    @Autowired
    private StompSessionHolder stompSessionHolder;

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {

        log.info("Get session id: {}", session.getSessionId());
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        exception.printStackTrace();
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return Task.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        log.info("Received and Confirm Task: {}", payload);
        stompSessionHolder.getSession().send(topicConfirmation, payload);

    }
}