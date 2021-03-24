package com.codesoom.project.application;

import com.codesoom.project.domain.Diary;
import com.codesoom.project.domain.DiaryRepository;
import com.codesoom.project.dto.DiaryData;
import com.codesoom.project.errors.DiaryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 다이어리 관련 비즈니스 로직을 담당합니다.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;

    /**
     * 전체 다이어리 목록을 반환합니다.
     *
     * @return 전체 다이어리 목록
     */
    public List<Diary> getDiaries() {
        return diaryRepository.findAll();
    }

    /**
     * 주어진 id에 해당하는 다이어리를 반환합니다.
     *
     * @param id 다이어리 식별자
     * @return 주어진 id를 갖는 다이어리
     * @throws DiaryNotFoundException 다이어리를 찾을 수 없을 경우
     */
    public Diary getDiary(Long id) {
        return findDiary(id);
    }

    /**
     * 새로운 다이어리를 생성합니다.
     *
     * @param diaryData 생성할 다이어리 정보
     * @return 생성된 다이어리
     */
    public Diary createDiary(DiaryData diaryData) {
        Diary createdDiary = diaryRepository.save(Diary.builder()
                .title(diaryData.getTitle())
                .comment(diaryData.getComment())
                .build());

        return Diary.of(createdDiary);
    }

    /**
     * 주어진 id에 해당하는 다이어리의 정보를 수정합니다.
     *
     * @param id 다이어리 식별자
     * @param diaryData 수정할 다이어리 정보
     * @return 수정된 다이어리
     */
    public Diary updateDiary(Long id, DiaryData diaryData) {
        Diary diary = findDiary(id);

        diary.updateWith(Diary.builder()
                .title(diaryData.getTitle())
                .comment(diaryData.getComment())
                .build());

        return Diary.of(diary);
    }

    public Diary findDiary(Long id) {
        return diaryRepository.findById(id)
                .orElseThrow(() -> new DiaryNotFoundException(id));
    }
}
