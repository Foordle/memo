// MemoService.java
package com.psu.memo.service;

import com.psu.memo.dto.MemoRequestDto;
import com.psu.memo.dto.MemoResponseDto;
import com.psu.memo.entity.Memo;
import com.psu.memo.repository.MemoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemoService {

    private final MemoRepository memoRepository;

    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }

    public MemoResponseDto createMemo(MemoRequestDto requestDto) {
        Memo memo = new Memo(requestDto);
        memo = memoRepository.save(memo);
        return new MemoResponseDto(memo);
    }

    public List<MemoResponseDto> getMemos() {
        return memoRepository.findAll().stream()
                .map(MemoResponseDto::new)
                .collect(Collectors.toList());
    }

    public MemoResponseDto getMemoById(Long id) {
        Memo memo = memoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("선택한 게시글이 존재하지 않습니다."));
        return new MemoResponseDto(memo);
    }

    public Long updateMemo(Long id, MemoRequestDto requestDto) {
        Memo memo = memoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("선택한 게시글이 존재하지 않습니다."));
        memo.update(requestDto);
        memoRepository.save(memo);
        return memo.getId();
    }

    public Long deleteMemo(Long id) {
        memoRepository.deleteById(id);
        return id;
    }
}