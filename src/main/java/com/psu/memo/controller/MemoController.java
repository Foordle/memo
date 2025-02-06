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
        Memo memo = new Memo(requestDto);
        Long maxId = memoList.size() > 0 ? Collections.max(memoList.keySet()) + 1 : 1;
        memo.setId(maxId);
        memoList.put(memo.getId(), memo);
        return new MemoResponseDto(memo);
    }

    @GetMapping("/memos")
    public List<MemoResponseDto> getMemos() {
        return memoList.values().stream().map(MemoResponseDto::new).toList();
    }

    // 게시글 상세 조회
    @GetMapping("/memos/{id}")
    public MemoResponseDto getMemoById(@PathVariable Long id) {
        Memo memo = memoList.get(id);
        if (memo == null) {
            throw new IllegalArgumentException("선택한 게시글이 존재하지 않습니다.");
        }
        return new MemoResponseDto(memo);
    }

    @PutMapping("/memos/{id}")
    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {
        if (memoList.containsKey(id)) {
            Memo memo = memoList.get(id);
            memo.update(requestDto);
            return memo.getId();
        } else {
            throw new IllegalArgumentException("선택한 게시글이 존재하지 않습니다.");
        }
    }

    @DeleteMapping("/memos/{id}")
    public Long deleteMemo(@PathVariable Long id) {
        if (memoList.containsKey(id)) {
            memoList.remove(id);
            return id;
        } else {
            throw new IllegalArgumentException("선택한 게시글이 존재하지 않습니다.");
        }
    }
}
