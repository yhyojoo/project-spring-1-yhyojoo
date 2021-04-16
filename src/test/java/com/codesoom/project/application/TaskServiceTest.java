package com.codesoom.project.application;

import com.codesoom.project.domain.Task;
import com.codesoom.project.domain.TaskRepository;
import com.codesoom.project.dto.TaskCreateData;
import com.codesoom.project.dto.TaskUpdateData;
import com.codesoom.project.errors.TaskNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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
    private static final String UPDATE_TITLE = "새로운 할 일";

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

        given(taskRepository.save(any(Task.class))).willReturn(task);
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
        @DisplayName("존재하는 할 일의 id가 주어진다면")
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
        @DisplayName("존재하지 않는 할 일의 id가 주어진다면")
        class Context_with_Invalid_id {

            @BeforeEach
            void setUp() {
                givenInvalidId = NOT_EXIST_ID;
            }

            @Test
            @DisplayName("할 일을 찾을 수 없다는 예외를 던진다")
            void it_returns_exception() {
                assertThatThrownBy(() -> taskService.getTask(givenInvalidId))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("createTask 메소드는")
    class Describe_createTask {
        TaskCreateData createRequest;

        @Nested
        @DisplayName("할 일의 타이틀이 주어진다면")
        class Context_with_create_request {

            @BeforeEach
            void setUp() {
                createRequest = TaskCreateData.builder()
                        .title(TITLE)
                        .build();
            }

            @Test
            @DisplayName("새로운 할 일을 추가한다")
            void it_returns_task() {
                taskService.createTask(createRequest);

                verify(taskRepository).save(any(Task.class));

                assertThat(task.getId()).isEqualTo(ID);
                assertThat(task.getTitle()).isEqualTo(TITLE);
            }
        }
    }

    @Nested
    @DisplayName("updateTask 메소드는")
    class Describe_updateTask {
        TaskUpdateData updateRequest;

        @Nested
        @DisplayName("존재하는 할 일 id와 수정할 정보가 주어진다면")
        class Context_with_valid_id_and_update_request {

            @BeforeEach
            void setUp() {
                givenValidId = ID;

                updateRequest = TaskUpdateData.builder()
                        .title(UPDATE_TITLE)
                        .build();
            }

            @Test
            @DisplayName("주어진 id를 갖는 할 일의 타이틀을 수정한다")
            void it_returns_task() {
                taskService.updateTask(givenValidId, updateRequest);

                verify(taskRepository).findById(givenValidId);

                assertThat(task.getTitle()).isEqualTo(UPDATE_TITLE);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 할 일 id와 수정할 타이틀이 주어진다면")
        class Context_with_Invalid_id_and_update_request {

            @BeforeEach
            void setUp() {
                givenInvalidId = NOT_EXIST_ID;

                updateRequest = TaskUpdateData.builder()
                        .title(UPDATE_TITLE)
                        .build();
            }

            @Test
            @DisplayName("수정할 할 일을 찾을 수 없다는 예외를 던진다")
            void it_returns_exception() {
                assertThatThrownBy(() -> taskService.updateTask(givenInvalidId, updateRequest))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }

    @Nested
    @DisplayName("deleteTask 메소드는")
    class Describe_deleteTask {

        @Nested
        @DisplayName("존재하는 할 일의 id가 주어진다면")
        class Context_with_valid_id {

            @BeforeEach
            void setUp() {
                givenValidId = ID;
            }

            @Test
            @DisplayName("주어진 id를 갖는 할 일을 삭제한다")
            void it_returns_task() {
                taskService.deleteTask(givenValidId);

                verify(taskRepository).findById(givenValidId);

                assertThat(taskRepository.findAll()).isNotIn(givenValidId);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 할 일의 id가 주어진다면")
        class Context_with_Invalid_id {

            @BeforeEach
            void setUp() {
                givenInvalidId = NOT_EXIST_ID;
            }

            @Test
            @DisplayName("할 일을 찾을 수 없다는 예외를 던진다")
            void it_returns_exception() {
                assertThatThrownBy(() -> taskService.getTask(givenInvalidId))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }
    }
}
