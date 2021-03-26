package com.codesoom.project.application;

import com.codesoom.project.domain.Task;
import com.codesoom.project.domain.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TaskServiceTest {
    private TaskService taskService;

    private TaskRepository taskRepository;

    private List<Task> tasks;
    private Task task;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);

        taskService = new TaskService(taskRepository);

        tasks = taskService.getTasks();

        given(taskRepository.findAll()).willReturn(tasks);
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
}
