package com.psu.memo.websocket;

import java.util.Map;

// 클라이언트 ↔ 서버 간 주고받을 메시지 구조
public class WhiteboardMessage {
    private String type;                   // "draw", "text", "rect" 등
    private Map<String, Object> payload;   // ← 객체 형태의 payload

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }
    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }
}
