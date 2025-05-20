package com.psu.memo.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
public class WhiteboardSocketController {

    private final SimpMessagingTemplate template;

    public WhiteboardSocketController(SimpMessagingTemplate template) {
        this.template = template;
    }

    /**
     * 클라이언트가 /app/draw 로 보낸 메세지를 받아
     * 모든 구독자에게 /topic/draw 로 뿌려준다.
     */
    @MessageMapping("/draw")
    public void broadcastDraw(WhiteboardMessage message) {
        template.convertAndSend("/topic/draw", message);
    }
}
