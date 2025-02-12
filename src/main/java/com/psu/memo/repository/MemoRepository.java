// MemoRepository.java
package com.psu.memo.repository;

import com.psu.memo.entity.Memo;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemoRepository {

    private final Map<Long, Memo> memoStore = new HashMap<>();
    private Long currentId = 1L;

    public Memo save(Memo memo) {
        if (memo.getId() == null) {
            memo.setId(currentId++);
        }
        memoStore.put(memo.getId(), memo);
        return memo;
    }

    public List<Memo> findAll() {
        return new ArrayList<>(memoStore.values());
    }

    public Optional<Memo> findById(Long id) {
        return Optional.ofNullable(memoStore.get(id));
    }

    public void deleteById(Long id) {
        memoStore.remove(id);
    }
}