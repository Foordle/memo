package com.psu.memo.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
public class WhiteboardSocketController {

    private final SimpMessagingTemplate template;
    // thread-safe 리스트에 모든 메시지 이력 저장
    private final List<WhiteboardMessage> history = new CopyOnWriteArrayList<>();

    public WhiteboardSocketController(SimpMessagingTemplate template) {
        this.template = template;
    }

    /**
     * 클라이언트가 /app/draw 로 보낸 메세지를 받아
     * 1) history에 저장하고
     * 2) /topic/draw 로 브로드캐스트
     */
    @MessageMapping("/draw")
    public void broadcastDraw(WhiteboardMessage message) {
        history.add(message);
        template.convertAndSend("/topic/draw", message);
    }

    /**
     * 클라이언트가 구독 직후 /app/init 을 호출하면
     * 지금까지 저장된 모든 history를 순서대로 브로드캐스트
     */
    @MessageMapping("/init")
    public void init(WhiteboardMessage unused) {
        // init 페이로드는 사용하지 않으므로 unused로 받기만 합니다.
        history.forEach(msg -> template.convertAndSend("/topic/draw", msg));
    }
}
