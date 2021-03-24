package com.codesoom.project.application;

import com.codesoom.project.domain.Diary;
import com.codesoom.project.domain.DiaryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DiaryServiceTest {
    private DiaryRepository diaryRepository;

    private DiaryService diaryService;

    private static final Long ID = 1L;
    private static final String TITLE = "오늘의 다이어리";
    private static final String COMMENT = "아쉬운 하루였다";

    private List<Diary> diaries;
    private Diary diary;

    @BeforeEach
    void setUp() {
        diaryRepository = mock(DiaryRepository.class);

        diaryService = new DiaryService(diaryRepository);

        diaries = diaryService.getDiaries();

        diary = Diary.builder()
                .id(ID)
                .title(TITLE)
                .comment(COMMENT)
                .build();

        given(diaryRepository.findAll()).willReturn(diaries);
    }

    @Test
    void getDiariesWithDiary() {
        diaries.add(diary);

        verify(diaryRepository).findAll();

        assertThat(diaries).hasSize(1);
    }

    @Test
    void getDiariesWithoutDiary() {
        assertThat(diaries).isEmpty();
    }
}
