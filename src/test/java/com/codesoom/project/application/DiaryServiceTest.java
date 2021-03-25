package com.codesoom.project.application;

import com.codesoom.project.domain.Diary;
import com.codesoom.project.domain.DiaryRepository;
import com.codesoom.project.dto.DiaryData;
import com.codesoom.project.errors.DiaryNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DiaryServiceTest {
    private DiaryRepository diaryRepository;

    private DiaryService diaryService;

    private static final Long NOT_EXIST_ID = 100L;
    private static final Long ID = 1L;

    private static final String TITLE = "오늘의 다이어리";
    private static final String COMMENT = "아쉬운 하루였다";

    private static final String UPDATE_TITLE = "3월 25일의 다이어리";
    private static final String UPDATE_COMMENT = "보람찬 하루였다";

    private List<Diary> diaries;
    private Diary diary;
    private Long givenValidId;
    private Long givenInvalidId;

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

        given(diaryRepository.findById(ID)).willReturn(Optional.of(diary));

        given(diaryRepository.save(any(Diary.class))).willReturn(diary);
    }

    @Nested
    @DisplayName("getDiaries 메소드는")
    class Describe_getDiaries {

        @Nested
        @DisplayName("다이어리가 존재한다면")
        class Context_with_diary {

            @BeforeEach
            void setUp() {
                diary = new Diary();
            }

            @Test
            @DisplayName("전체 다이어리 목록을 반환한다")
            void it_returns_list() {
                diaries.add(diary);

                verify(diaryRepository).findAll();

                assertThat(diaries).hasSize(1);
            }
        }

        @Nested
        @DisplayName("다이어리가 존재하지 않는다면")
        class Context_without_diary {

            @Test
            @DisplayName("빈 목록을 반환한다")
            void it_returns_empty_list() {
                assertThat(diaries).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("getDiary 메소드는")
    class Describe_getDiary {

        @Nested
        @DisplayName("등록된 다이어리의 id가 주어진다면")
        class Context_with_valid_id {

            @BeforeEach
            void setUp() {
                givenValidId = ID;
            }

            @Test
            @DisplayName("주어진 id를 갖는 다이어리를 반환한다")
            void it_returns_diary() {
                diary = diaryService.getDiary(givenValidId);

                verify(diaryRepository).findById(givenValidId);

                assertThat(diary.getTitle()).isEqualTo(TITLE);
                assertThat(diary.getComment()).isEqualTo(COMMENT);
            }
        }

        @Nested
        @DisplayName("등록되지 않은 다이어리 id가 주어진다면")
        class Context_with_Invalid_id {

            @BeforeEach
            void setUp() {
                givenInvalidId = NOT_EXIST_ID;
            }

            @Test
            @DisplayName("조회할 다이어리를 찾을 수 없다는 예외를 던진다")
            void it_returns_exception() {
                assertThatThrownBy(() -> diaryService.getDiary(givenInvalidId))
                        .isInstanceOf(DiaryNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("createDiary 메소드는")
    class Describe_createDiary {
        DiaryData createRequest;

        @Nested
        @DisplayName("생성할 다이어리 정보가 주어진다면")
        class Context_with_create_request {

            @BeforeEach
            void setUp() {
                createRequest = DiaryData.builder()
                        .title(TITLE)
                        .comment(COMMENT)
                        .build();
            }

            @Test
            @DisplayName("새로운 다이어리를 생성한다")
            void it_returns_diary() {
                diaryService.createDiary(createRequest);

                verify(diaryRepository).save(any(Diary.class));
            }
        }
    }

    @Nested
    @DisplayName("updateDiary 메소드는")
    class Describe_updateDiary {
        DiaryData updateRequest;

        @Nested
        @DisplayName("등록된 다이어리 id와 수정할 정보가 주어진다면")
        class Context_with_valid_id_and_update_request {

            @BeforeEach
            void setUp() {
                givenValidId = ID;

                updateRequest = DiaryData.builder()
                        .title(UPDATE_TITLE)
                        .comment(UPDATE_COMMENT)
                        .build();
            }

            @Test
            @DisplayName("주어진 id를 갖는 다이어리의 정보를 수정한다")
            void it_returns_diary() {
                diaryService.updateDiary(givenValidId, updateRequest);

                verify(diaryRepository).findById(givenValidId);

                assertThat(diary.getTitle()).isEqualTo(UPDATE_TITLE);
                assertThat(diary.getComment()).isEqualTo(UPDATE_COMMENT);
            }
        }

        @Nested
        @DisplayName("등록되지 않은 다이어리 id와 수정할 정보가 주어진다면")
        class Context_with_Invalid_id_and_update_request {

            @BeforeEach
            void setUp() {
                givenInvalidId = NOT_EXIST_ID;

                updateRequest = DiaryData.builder()
                        .title(UPDATE_TITLE)
                        .comment(UPDATE_COMMENT)
                        .build();
            }

            @Test
            @DisplayName("수정할 다이어리를 찾을 수 없다는 예외를 던진다")
            void it_returns_exception() {
                assertThatThrownBy(() -> diaryService.updateDiary(givenInvalidId, updateRequest))
                        .isInstanceOf(DiaryNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("deleteDiary 메소드는")
    class Describe_deleteDiary {

        @Nested
        @DisplayName("등록된 다이어리의 id가 주어진다면")
        class Context_with_valid_id {

            @BeforeEach
            void setUp() {
                givenValidId = ID;
            }

            @Test
            @DisplayName("주어진 id를 갖는 다이어리를 삭제한다")
            void it_returns_diary() {
                diaryService.deleteDiary(givenValidId);

                verify(diaryRepository).findById(givenValidId);
                verify(diaryRepository).delete(diary);

                assertThat(diaryRepository.findAll()).isNotIn(diary);
            }
        }

        @Nested
        @DisplayName("등록되지 않은 다이어리 id가 주어진다면")
        class Context_with_Invalid_id {

            @BeforeEach
            void setUp() {
                givenInvalidId = NOT_EXIST_ID;
            }

            @Test
            @DisplayName("삭제할 다이어리를 찾을 수 없다는 예외를 던진다")
            void it_returns_exception() {
                assertThatThrownBy(() -> diaryService.getDiary(givenInvalidId))
                        .isInstanceOf(DiaryNotFoundException.class);
            }
        }
    }
}
