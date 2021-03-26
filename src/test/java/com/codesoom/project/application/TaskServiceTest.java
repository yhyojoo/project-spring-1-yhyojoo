package com.codesoom.project.application;

import com.codesoom.project.domain.Task;
import com.codesoom.project.domain.TaskRepository;
import com.codesoom.project.errors.TaskNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TaskServiceTest {
    private TaskService taskService;

    private TaskRepository taskRepository;

    private static final Long NOT_EXIST_ID = 100L;
    private static final Long ID = 1L;
    private static final String TITLE = "첫 번째 할 일";

    private List<Task> tasks;
    private Task task;
    private Long givenValidId;
    private Long givenInvalidId;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);

        taskService = new TaskService(taskRepository);

        task = Task.builder()
                .id(ID)
                .title(TITLE)
                .build();

        tasks = taskService.getTasks();

        given(taskRepository.findAll()).willReturn(tasks);

        given(taskRepository.findById(eq(ID))).willReturn(Optional.of(task));
    }

    @Nested
    @DisplayName("getTasks 메소드는")
    class Describe_getTasks {

        @Nested
        @DisplayName("할 일이 존재한다면")
        class Context_with_task {

            @BeforeEach
            void setUp() {
                task = new Task();

                tasks.add(task);
            }

            @Test
            @DisplayName("전체 목록을 반환한다")
            void it_returns_list() {
                verify(taskRepository).findAll();

                assertThat(tasks).hasSize(1);
            }
        }

        @Nested
        @DisplayName("할 일이 존재하지 않는다면")
        class Context_without_task {

            @Test
            @DisplayName("빈 목록을 반환한다")
            void it_returns_empty_list() {
                verify(taskRepository).findAll();

                assertThat(tasks).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("getTask 메소드는")
    class Describe_getTask {

        @Nested
        @DisplayName("등록된 할 일의 id가 주어진다면")
        class Context_with_valid_id {

            @BeforeEach
            void setUp() {
                givenValidId = ID;
            }

            @Test
            @DisplayName("주어진 id를 갖는 할 일을 반환한다")
            void it_returns_task() {
                task = taskService.getTask(givenValidId);

                verify(taskRepository).findById(givenValidId);

                assertThat(task.getId()).isEqualTo(ID);
                assertThat(task.getTitle()).isEqualTo(TITLE);
            }
        }

        @Nested
        @DisplayName("등록되지 않은 할 일의 id가 주어진다면")
        class Context_with_Invalid_id {

            @BeforeEach
            void setUp() {
                givenInvalidId = NOT_EXIST_ID;
            }

            @Test
            @DisplayName("조회할 할 일을 찾을 수 없다는 예외를 던진다")
            void it_returns_exception() {
                assertThatThrownBy(() -> taskService.getTask(givenInvalidId))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}
