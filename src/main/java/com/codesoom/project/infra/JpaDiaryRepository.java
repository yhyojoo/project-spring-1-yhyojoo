package com.codesoom.project.infra;

import com.codesoom.project.domain.Diary;
import com.codesoom.project.domain.DiaryRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@Primary
public interface JpaDiaryRepository
        extends DiaryRepository, CrudRepository<Diary, Long> {
    List<Diary> findAll();

    Optional<Diary> findById(Long id);

    Diary save(Diary diary);

    void delete(Diary diary);
}
