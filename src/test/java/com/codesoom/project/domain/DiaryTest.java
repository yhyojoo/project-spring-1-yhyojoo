package com.codesoom.project.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DiaryTest {
    private static final Long ID = 1L;

    private static final String TITLE = "오늘의 할 일";

    private static final String COMMENT = "아쉬운 하루였다";

    private Diary diary;

    @BeforeEach
    void setUp() {
        diary = Diary.builder()
                .id(ID)
                .title(TITLE)
                .comment(COMMENT)
                .build();
    }

    @Test
    void creationWithBuilder() {
        assertThat(diary.getId()).isEqualTo(ID);
        assertThat(diary.getTitle()).isEqualTo(TITLE);
        assertThat(diary.getComment()).isEqualTo(COMMENT);
    }
}