package com.psu.memo.websocket;

// 클라이언트 ↔ 서버 간 주고받을 메시지 구조 DTO역할을한다고 생각!
public class WhiteboardMessage {
    private String type;      // "draw", "clear" 등
    private String payload;   // 예: {x:10,y:20,...} 같은 JSON
    // + getters / setters
    public String getType() { return type; }
    public void setType(String t) { this.type = t; }
    public String getPayload() { return payload; }
    public void setPayload(String p) { this.payload = p; }
}
