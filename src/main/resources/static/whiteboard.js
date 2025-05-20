// src/main/resources/static/whiteboard.js

// 1) 전역 변수 정의
let stompClient, canvas, ctx;
let currentTool = 'select', drawing = false;
let startX = 0, startY = 0;

// 툴 매핑
const tools = {
    selectBtn: 'select',
    rectBtn:   'rect',
    circleBtn: 'circle',
    arrowBtn:  'arrow',
    textBtn:   'text'
};

// 페이지 로드
console.log('✅ whiteboard.js 로드됨');
window.addEventListener('load', () => {
    console.log('✅ window.load');

    // 툴 버튼 바인딩
    Object.keys(tools).forEach(btnId => {
        const btn = document.getElementById(btnId);
        console.log('바인딩:', btnId, btn);
        btn.addEventListener('click', () => {
            console.log('툴 클릭:', btnId, '→', tools[btnId]);
            currentTool = tools[btnId];
            // 시각적 토글
            Object.keys(tools).forEach(id => document.getElementById(id).classList.remove('active'));
            btn.classList.add('active');
        });
    });

    // 캔버스 세팅
    canvas = document.getElementById('board');
    ctx = canvas.getContext('2d');
    ctx.lineWidth = 2;
    ctx.strokeStyle = '#000';

    canvas.addEventListener('pointerdown', onPointerDown);
    canvas.addEventListener('pointermove', onPointerMove);
    canvas.addEventListener('pointerup',   onPointerUp);

    // WebSocket 연결
    const sock = new SockJS('/ws-whiteboard');
    stompClient = Stomp.over(sock);
    stompClient.connect({}, () => {
        console.log('WebSocket Connected');
        stompClient.subscribe('/topic/draw', msg => {
            const wb = JSON.parse(msg.body);
            console.log('<<< MESSAGE', wb);
            drawFromMessage(wb);
        });
    });
});

// pointerdown
function onPointerDown(e) {
    drawing = true;
    const r = canvas.getBoundingClientRect();
    startX = e.clientX - r.left;
    startY = e.clientY - r.top;
    console.log('pointerdown', currentTool, startX, startY);

    if (currentTool === 'select') {
        ctx.beginPath();
        ctx.moveTo(startX, startY);
    }
}

// pointermove
function onPointerMove(e) {
    if (!drawing) return;
    const r = canvas.getBoundingClientRect();
    const x = e.clientX - r.left, y = e.clientY - r.top;

    if (currentTool === 'select') {
        // free-hand 그리기
        ctx.lineTo(x, y);
        ctx.stroke();
        // (원하면 전송)
        // sendMessage('draw', { x, y });
    }
}

// pointerup
function onPointerUp(e) {
    if (!drawing) return;
    drawing = false;
    const r = canvas.getBoundingClientRect();
    const endX = e.clientX - r.left;
    const endY = e.clientY - r.top;
    console.log('pointerup', currentTool, endX, endY);

    // pointerup 내부
    if (currentTool === 'text') {
        const input = document.createElement('input');
        input.style.position = 'absolute';
        input.style.left = `${e.pageX}px`;
        input.style.top  = `${e.pageY}px`;
        input.style.zIndex = 1000;
        document.body.appendChild(input);
        input.focus();

        input.addEventListener('blur', () => {
            const txt = input.value.trim();
            document.body.removeChild(input);
            if (!txt) return;

            // ① 캔버스에 텍스트 그리기
            ctx.fillStyle = '#000';
            ctx.font = '16px sans-serif';
            ctx.fillText(txt, startX, startY);

            // ② 서버로도 전송
            sendMessage('text', { text: txt, x: startX, y: startY });
        });

        // 텍스트 툴일 땐 sendMessage 아래에 있는 기본 전송은 막기
        drawing = false;
        return;
    }


    // server 전송
    sendMessage(currentTool, { x1:startX, y1:startY, x2:endX, y2:endY });
    // 내 화면에도 그리기
    drawFromMessage({ type: currentTool, payload:{ x1:startX, y1:startY, x2:endX, y2:endY } });
}

// 전송
function sendMessage(type, payload) {
    const msg = JSON.stringify({ type, payload });
    console.log('>>> SEND', type, payload);
    stompClient.send('/app/draw', {}, msg);
}

// 수신/그리기
function drawFromMessage(msg) {
    const p = msg.payload;
    ctx.beginPath();
    switch (msg.type) {
        case 'rect':
            ctx.strokeRect(p.x1, p.y1, p.x2-p.x1, p.y2-p.y1);
            break;
        case 'circle':
            const rx = (p.x2-p.x1)/2, ry = (p.y2-p.y1)/2;
            ctx.ellipse(p.x1+rx, p.y1+ry, Math.abs(rx), Math.abs(ry), 0, 0, 2*Math.PI);
            ctx.stroke();
            break;
        case 'arrow':
            drawArrow(p.x1,p.y1,p.x2,p.y2);
            break;
        case 'select':
            // 선택툴은 free-hand 로 처리했으면 이쪽
            break;
    }
    ctx.closePath();
}

// 화살표 그리기
function drawArrow(x1,y1,x2,y2) {
    const head=10, dx=x2-x1, dy=y2-y1, ang=Math.atan2(dy,dx);
    ctx.moveTo(x1,y1);
    ctx.lineTo(x2,y2);
    ctx.lineTo(x2-head*Math.cos(ang-Math.PI/6), y2-head*Math.sin(ang-Math.PI/6));
    ctx.moveTo(x2,y2);
    ctx.lineTo(x2-head*Math.cos(ang+Math.PI/6), y2-head*Math.sin(ang+Math.PI/6));
    ctx.stroke();
}
