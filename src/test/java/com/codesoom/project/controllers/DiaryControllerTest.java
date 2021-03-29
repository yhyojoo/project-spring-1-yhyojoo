package com.codesoom.project.controllers;

import com.codesoom.project.application.DiaryService;
import com.codesoom.project.domain.Diary;
import com.codesoom.project.domain.DiaryRepository;
import com.codesoom.project.dto.DiaryCreateData;
import com.codesoom.project.dto.DiaryResultData;
import com.codesoom.project.dto.DiaryUpdateData;
import com.codesoom.project.errors.DiaryNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DiaryController.class)
class DiaryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DiaryService diaryService;

    @MockBean
    private DiaryRepository diaryRepository;

    private static final Long NOT_EXIST_ID = 100L;
    private static final Long ID = 1L;

    private static final String TITLE = "오늘의 다이어리";
    private static final String COMMENT = "아쉬운 하루였다";

    private static final String UPDATE_TITLE = "3월 25일의 다이어리";
    private static final String UPDATE_COMMENT = "보람찬 하루였다";

    private Diary diary;
    private DiaryResultData createdDiary;
    private DiaryResultData updatedDiary;
    private Long givenValidId;
    private Long givenInvalidId;
    private DiaryCreateData InvalidAttributes;

    @BeforeEach
    void setUp() {
        List<Diary> diaries = new ArrayList<>();

        diary = Diary.builder()
                .id(ID)
                .title(TITLE)
                .comment(COMMENT)
                .build();

        createdDiary = DiaryResultData.builder()
                .id(ID)
                .title(TITLE)
                .comment(COMMENT)
                .build();

        updatedDiary = DiaryResultData.builder()
                .id(ID)
                .title(UPDATE_TITLE)
                .comment(UPDATE_COMMENT)
                .build();

        diaries.add(diary);

        given(diaryService.getDiaries()).willReturn(diaries);

        given(diaryService.getDiary(eq(ID))).willReturn(diary);

        given(diaryService.getDiary(eq(NOT_EXIST_ID)))
                .willThrow(new DiaryNotFoundException(NOT_EXIST_ID));

        given(diaryService.createDiary(any(DiaryCreateData.class))).willReturn(createdDiary);

        given(diaryService.updateDiary(eq(ID), any(DiaryUpdateData.class)))
                .willReturn(updatedDiary);

        given(diaryService.updateDiary(eq(NOT_EXIST_ID), any(DiaryUpdateData.class)))
                .willThrow(new DiaryNotFoundException(NOT_EXIST_ID));

        given(diaryService.deleteDiary(eq(NOT_EXIST_ID)))
                .willThrow(new DiaryNotFoundException(NOT_EXIST_ID));
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

                verify(diaryService).getDiaries();
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
                mockMvc.perform(get("/diaries/1"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("id").value(ID))
                        .andExpect(jsonPath("title").value(TITLE))
                        .andExpect(jsonPath("comment").value(COMMENT));

                verify(diaryService).getDiary(givenValidId);
            }
        }

        @Nested
        @DisplayName("등록되지 않은 다이어리 id가 주어진다면")
        class Context_with_invalid_id {

            @BeforeEach
            void setUp() {
                givenInvalidId = NOT_EXIST_ID;
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

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {
        DiaryCreateData createRequest;

        @Nested
        @DisplayName("생성할 다이어리 정보가 주어진다면")
        class Context_with_create_request {

            @BeforeEach
            void setUp() {
                createRequest = DiaryCreateData.builder()
                        .title(TITLE)
                        .comment(COMMENT)
                        .build();
            }

            @Test
            @DisplayName("생성된 다이어리와 응답코드 201을 반환한다")
            void it_returns_diary_and_201() throws Exception {
                mockMvc.perform(post("/diaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest))
                )
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("id").value(ID))
                        .andExpect(jsonPath("title").value(TITLE))
                        .andExpect(jsonPath("comment").value(COMMENT));

                verify(diaryService).createDiary(any(DiaryCreateData.class));
            }
        }

        @Nested
        @DisplayName("유효하지 않은 정보가 주어진다면")
        class Context_with_invalid_attributes {

            @BeforeEach
            void setUp() {
                InvalidAttributes = DiaryCreateData.builder()
                        .title("")
                        .comment(COMMENT)
                        .build();
            }

            @Test
            @DisplayName("응답코드 400을 반환한다")
            void it_returns_400() throws Exception {
                mockMvc.perform(post("/diaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(InvalidAttributes))
                )
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_update {
        DiaryCreateData updateRequest;

        @Nested
        @DisplayName("등록된 다이어리 id와 수정할 정보가 주어진다면")
        class Context_with_valid_id_and_update_request {

            @BeforeEach
            void setUp() {
                givenValidId = ID;

                updateRequest = DiaryCreateData.builder()
                        .title(UPDATE_TITLE)
                        .comment(UPDATE_COMMENT)
                        .build();
            }

            @Test
            @DisplayName("수정된 다이어리와 응답코드 200을 반환한다")
            void it_returns_diary_and_200() throws Exception {
                mockMvc.perform(patch("/diaries/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest))
                )
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("id").value(ID))
                        .andExpect(jsonPath("title").value(UPDATE_TITLE))
                        .andExpect(jsonPath("comment").value(UPDATE_COMMENT));

                verify(diaryService).updateDiary(eq(ID), any(DiaryUpdateData.class));
            }
        }

        @Nested
        @DisplayName("등록되지 않은 다이어리 id와 수정할 정보가 주어진다면")
        class Context_with_invalid_id_and_update_request {

            @BeforeEach
            void setUp() {
                givenInvalidId = NOT_EXIST_ID;

                updateRequest = DiaryCreateData.builder()
                        .title(UPDATE_TITLE)
                        .comment(UPDATE_COMMENT)
                        .build();
            }

            @Test
            @DisplayName("응답코드 404를 반환한다")
            void it_returns_404() throws Exception {
                mockMvc.perform(patch("/diaries/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest))
                )
                        .andExpect(status().isNotFound());

                verify(diaryService).updateDiary(eq(NOT_EXIST_ID), any(DiaryUpdateData.class));
            }
        }

        @Nested
        @DisplayName("등록된 다이어리 id와 유효하지 않은 정보가 주어진다면")
        class Context_with_valid_id_and_invalid_attributes {

            @BeforeEach
            void setUp() {
                givenValidId = ID;

                InvalidAttributes = DiaryCreateData.builder()
                        .title("")
                        .comment(UPDATE_COMMENT)
                        .build();
            }

            @Test
            @DisplayName("응답코드 400를 반환한다")
            void it_returns_400() throws Exception {
                mockMvc.perform(patch("/diaries/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(InvalidAttributes))
                )
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_delete {

        @Nested
        @DisplayName("등록된 다이어리 id가 주어진다면")
        class Context_with_valid_id {

            @BeforeEach
            void setUp() {
                givenValidId = ID;
            }

            @Test
            @DisplayName("주어진 id를 갖는 다이어리를 삭제하고 응답코드 200을 반환한다")
            void it_deletes_diary_and_200() throws Exception {
                mockMvc.perform(delete("/diaries/1"))
                        .andExpect(status().isOk());

                verify(diaryService).deleteDiary(givenValidId);
            }
        }

        @Nested
        @DisplayName("등록되지 않은 다이어리 id가 주어진다면")
        class Context_with_invalid_id {

            @BeforeEach
            void setUp() {
                givenInvalidId = NOT_EXIST_ID;
            }

            @Test
            @DisplayName("응답코드 404를 반환한다")
            void it_returns_404() throws Exception {
                mockMvc.perform(delete("/diaries/100"))
                        .andExpect(status().isNotFound());

                verify(diaryService).deleteDiary(givenInvalidId);
            }
        }
    }

    @AfterEach
    public void afterEach() {
        diaryRepository.delete(diary);
    }
}
