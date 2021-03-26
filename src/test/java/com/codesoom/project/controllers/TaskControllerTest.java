package com.codesoom.project.controllers;

import com.codesoom.project.application.TaskService;
import com.codesoom.project.domain.Task;
import com.codesoom.project.domain.TaskRepository;
import com.codesoom.project.errors.TaskNotFoundException;
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

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @MockBean
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
        tasks = new ArrayList<>();

        task = Task.builder()
                .id(ID)
                .title(TITLE)
                .build();

        given(taskService.getTasks()).willReturn(tasks);

        given(taskService.getTask(ID)).willReturn(task);

        given(taskService.getTask(eq(NOT_EXIST_ID)))
                .willThrow(new TaskNotFoundException(NOT_EXIST_ID));
    }

    @Nested
    @DisplayName("list 메소드는")
    class Describe_list {

        @Nested
        @DisplayName("할 일이 존재한다면")
        class Context_with_task {

            @BeforeEach
            void setUp() {
                task = new Task();

                tasks.add(task);
            }

            @Test
            @DisplayName("전체 할 일 목록과 응답코드 200을 반환한다")
            void it_returns_list_and_200() throws Exception {
                mockMvc.perform(get("/diaries/1/tasks"))
                        .andExpect(status().isOk());

                verify(taskService).getTasks();
            }
        }
    }

    @Nested
    @DisplayName("detail 메소드는")
    class Describe_detail {

        @Nested
        @DisplayName("등록된 할 일의 id가 주어진다면")
        class Context_with_valid_id {

            @BeforeEach
            void setUp() {
                givenValidId = ID;
            }

            @Test
            @DisplayName("주어진 id를 갖는 할 일과 응답코드 200을 반환한다")
            void it_returns_task_and_200() throws Exception {
                mockMvc.perform(get("/diaries/1/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("id").value(ID))
                        .andExpect(jsonPath("title").value(TITLE));

                verify(taskService).getTask(givenValidId);
            }
        }

        @Nested
        @DisplayName("등록되지 않은 할 일의 id가 주어진다면")
        class Context_with_invalid_id {

            @BeforeEach
            void setUp() {
                givenInvalidId = NOT_EXIST_ID;
            }

            @Test
            @DisplayName("응답코드 404를 반환한다")
            void it_returns_404() throws Exception {
                mockMvc.perform(get("/diaries/1/tasks/100"))
                        .andExpect(status().isNotFound());

                verify(taskService).getTask(givenInvalidId);
            }
        }
    }

    @AfterEach
    public void afterEach() {
        taskRepository.delete(task);
    }
}
