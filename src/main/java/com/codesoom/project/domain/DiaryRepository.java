package com.codesoom.project.domain;

import java.util.List;
import java.util.Optional;

/**
 * 다이어리 저장소.
 */
public interface DiaryRepository {
    List<Diary> findAll();

    Optional<Diary> findById(Long id);

    Diary save(Diary diary);

    void delete(Diary diary);
}
