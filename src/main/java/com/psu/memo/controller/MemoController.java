package com.psu.memo.controller;

import com.psu.memo.dto.MemoRequestDto;
import com.psu.memo.dto.MemoResponseDto;
import com.psu.memo.entity.Memo;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MemoController {

    private final Map<Long, Memo> memoList = new HashMap<>();

    @PostMapping("/memos")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto) {
        // Memo Max ID Check (값이 없으면 1L, 있으면 최댓값 + 1)
        Long maxId = memoList.isEmpty() ? 1L : Collections.max(memoList.keySet()) + 1;

        // RequestDto -> Entity
        Memo memo = new Memo(requestDto);
        memo.setId(maxId);  // ID 설정

        // DB 저장
        memoList.put(maxId, memo);

        // Entity -> ResponseDto
        return new MemoResponseDto(memo);
    }



    @GetMapping("/memos")
    public List<MemoResponseDto> getMemos(){
        // Map To List
        List<MemoResponseDto> responseList = memoList.values().stream()
                .map(MemoResponseDto::new).toList();
        return responseList;
    }
}
