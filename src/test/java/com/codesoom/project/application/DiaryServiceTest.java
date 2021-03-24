package com.codesoom.project.application;

import com.codesoom.project.domain.Diary;
import com.codesoom.project.domain.DiaryRepository;
import com.codesoom.project.errors.DiaryNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DiaryServiceTest {
    private DiaryRepository diaryRepository;

    private DiaryService diaryService;

    private static final Long INVALID_ID = 100L;
    private static final Long VALID_ID = 1L;
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
                .id(VALID_ID)
                .title(TITLE)
                .comment(COMMENT)
                .build();

        given(diaryRepository.findAll()).willReturn(diaries);

        given(diaryRepository.findById(VALID_ID)).willReturn(Optional.of(diary));
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

    @Test
    void getDiaryWithValidId() {
        diary = diaryService.getDiary(VALID_ID);

        verify(diaryRepository).findById(VALID_ID);

        assertThat(diary.getTitle()).isEqualTo(TITLE);
        assertThat(diary.getComment()).isEqualTo(COMMENT);
    }

    @Test
    void getDiaryWithInvalidId() {
        assertThatThrownBy(() -> diaryService.getDiary(INVALID_ID))
                .isInstanceOf(DiaryNotFoundException.class);
    }
}
