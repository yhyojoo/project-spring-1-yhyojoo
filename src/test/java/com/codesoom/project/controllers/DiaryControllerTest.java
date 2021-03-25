package com.codesoom.project.controllers;

import com.codesoom.project.application.DiaryService;
import com.codesoom.project.domain.Diary;
import com.codesoom.project.domain.DiaryRepository;
import com.codesoom.project.errors.DiaryNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DiaryController.class)
class DiaryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DiaryService diaryService;

    @MockBean
    private DiaryRepository diaryRepository;

    private static final Long NOT_EXIST_ID = 100L;
    private static final Long ID = 1L;
    private static final String TITLE = "오늘의 다이어리";
    private static final String COMMENT = "아쉬운 하루였다";

    private Diary diary;
    private Long givenValidId;
    private Long givenInvalidId;

    @BeforeEach
    void setUp() {
        List<Diary> diaries = new ArrayList<>();

        diary = Diary.builder()
                .id(ID)
                .title(TITLE)
                .comment(COMMENT)
                .build();

        diaries.add(diary);

        given(diaryService.getDiaries()).willReturn(diaries);

        given(diaryService.getDiary(eq(ID))).willReturn(diary);
    }

    @Nested
    @DisplayName("list 메소드는")
    class Describe_list {

        @Nested
        @DisplayName("다이어리가 존재한다면")
        class Context_with_diary {

            @BeforeEach
            void setUp() {
                diary = new Diary();
            }

            @Test
            @DisplayName("전체 다이어리 목록과 응답코드 200을 반환한다")
            void it_returns_list_and_200() throws Exception {
                mockMvc.perform(get("/diaries"))
                        .andExpect(status().isOk());
            }
        }
    }

    @Nested
    @DisplayName("detail 메소드는")
    class Describe_detail {

        @Nested
        @DisplayName("등록된 다이어리 id가 주어진다면")
        class Context_with_valid_id {

            @BeforeEach
            void setUp() {
                givenValidId = ID;
            }

            @Test
            @DisplayName("주어진 id를 갖는 다이어리와 응답코드 200을 반환한다")
            void it_returns_diary_and_200() throws Exception {
                mockMvc.perform(get("/diaries/1")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                )
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("아쉬운")));

                verify(diaryService).getDiary(givenValidId);
            }
        }

        @Nested
        @DisplayName("등록되지 않은 다이어리 id가 주어진다면")
        class Context_with_invalid_id {

            @BeforeEach
            void setUp() {
                givenInvalidId = NOT_EXIST_ID;

                given(diaryService.getDiary(NOT_EXIST_ID))
                        .willThrow(new DiaryNotFoundException(NOT_EXIST_ID));
            }

            @Test
            @DisplayName("응답코드 404를 반환한다")
            void it_returns_404() throws Exception {
                mockMvc.perform(get("/diaries/100"))
                        .andExpect(status().isNotFound());

                verify(diaryService).getDiary(givenInvalidId);
            }
        }
    }

    @AfterEach
    public void afterEach() {
        diaryRepository.delete(diary);
    }
}
