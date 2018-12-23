package com.boo.websocketclient;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import static java.util.concurrent.TimeUnit.SECONDS;

@Component
@Log4j2
public class StompSessionHolder {

    @Value("${websocket.uri}")
    private String webSocketUri;


    @Value("${topic_task}")
    private String topicTask;

    @Autowired
    private TaskHandler taskHandler;

    private StompSession stompSession;

    @Scheduled(fixedRate = 5000)
    public void testConnection() {
        boolean connected = getSession() != null && getSession().isConnected();
        log.info("test connected: {}", connected);
    }

    public StompSession getSession() {

        if (stompSession == null || !stompSession.isConnected()) {
            try {

                WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
                stompClient.setMessageConverter(new MappingJackson2MessageConverter());

                stompSession = stompClient.connect(webSocketUri, new StompSessionHandlerAdapter() {
                }).get(1, SECONDS);

                stompSession.subscribe(topicTask, taskHandler);

            } catch (Exception e) {
                log.error("conection failed");
                return null;
            }
        }
        return stompSession;
    }
}
