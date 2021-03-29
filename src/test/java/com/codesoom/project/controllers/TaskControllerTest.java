package com.codesoom.project.controllers;

import com.codesoom.project.application.TaskService;
import com.codesoom.project.domain.Task;
import com.codesoom.project.domain.TaskRepository;
import com.codesoom.project.dto.TaskCreateData;
import com.codesoom.project.dto.TaskUpdateData;
import com.codesoom.project.dto.TaskResultData;
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
    private static final String UPDATE_TITLE = "새로운 할 일";


    private List<Task> tasks;
    private Task task;
    private TaskResultData createdTask;
    private TaskResultData updatedTask;
    private Long givenValidId;
    private Long givenInvalidId;

    @BeforeEach
    void setUp() {
        tasks = new ArrayList<>();

        task = Task.builder()
                .id(ID)
                .title(TITLE)
                .build();

        createdTask = TaskResultData.builder()
                .id(ID)
                .title(TITLE)
                .build();

        updatedTask = TaskResultData.builder()
                .id(ID)
                .title(UPDATE_TITLE)
                .build();

        given(taskService.getTasks()).willReturn(tasks);

        given(taskService.getTask(ID)).willReturn(task);

        given(taskService.getTask(eq(NOT_EXIST_ID)))
                .willThrow(new TaskNotFoundException(NOT_EXIST_ID));

        given(taskService.createTask(any(TaskCreateData.class))).willReturn(createdTask);

        given(taskService.updateTask(eq(ID), any(TaskUpdateData.class)))
                .willReturn(updatedTask);

        given(taskService.updateTask(eq(NOT_EXIST_ID), any(TaskUpdateData.class)))
                .willThrow(new TaskNotFoundException(NOT_EXIST_ID));

        given(taskService.deleteTask(eq(NOT_EXIST_ID)))
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
        @DisplayName("존재하는 할 일의 id가 주어진다면")
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
        @DisplayName("존재하지 않는 할 일의 id가 주어진다면")
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

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {
        TaskCreateData createRequest;
        TaskCreateData invalidAttributes;

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
            @DisplayName("추가된 할 일과 응답코드 201을 반환한다")
            void it_returns_task_and_201() throws Exception {
                mockMvc.perform(post("/diaries/1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest))
                )
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("id").value(ID))
                        .andExpect(jsonPath("title").value(TITLE));

                verify(taskService).createTask(any(TaskCreateData.class));
            }
        }

        @Nested
        @DisplayName("할 일의 타이틀이 주어지지 않는다면")
        class Context_with_invalid_attributes {

            @BeforeEach
            void setUp() {
                invalidAttributes = TaskCreateData.builder()
                        .title("")
                        .build();
            }

            @Test
            @DisplayName("응답코드 400을 반환한다")
            void it_returns_400() throws Exception {
                mockMvc.perform(post("/diaries/1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidAttributes))
                )
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_update {
        TaskUpdateData updateRequest;
        TaskUpdateData invalidAttributes;

        @Nested
        @DisplayName("존재하는 할 일 id와 수정할 타이틀이 주어진다면")
        class Context_with_valid_id_and_update_request {

            @BeforeEach
            void setUp() {
                givenValidId = ID;

                updateRequest = TaskUpdateData.builder()
                        .title(UPDATE_TITLE)
                        .build();
            }

            @Test
            @DisplayName("수정된 할 일 응답코드 200을 반환한다")
            void it_returns_task_and_200() throws Exception {
                mockMvc.perform(patch("/diaries/1/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest))
                )
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("id").value(ID))
                        .andExpect(jsonPath("title").value(UPDATE_TITLE));

                verify(taskService).updateTask(eq(ID), any(TaskUpdateData.class));
            }
        }

        @Nested
        @DisplayName("존재하지 않는 할 일 id와 수정할 타이틀이 주어진다면")
        class Context_with_invalid_id_and_update_request {

            @BeforeEach
            void setUp() {
                givenInvalidId = NOT_EXIST_ID;

                updateRequest = TaskUpdateData.builder()
                        .title(UPDATE_TITLE)
                        .build();
            }

            @Test
            @DisplayName("응답코드 404를 반환한다")
            void it_returns_404() throws Exception {
                mockMvc.perform(patch("/diaries/1/tasks/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest))
                )
                        .andExpect(status().isNotFound());

                verify(taskService).updateTask(eq(NOT_EXIST_ID), any(TaskUpdateData.class));
            }
        }

        @Nested
        @DisplayName("존재하는 할 일 id와 수정할 타이틀이 주어지지 않는다면")
        class Context_with_valid_id_and_invalid_attributes {

            @BeforeEach
            void setUp() {
                givenValidId = ID;

                invalidAttributes = TaskUpdateData.builder()
                        .title("")
                        .build();
            }

            @Test
            @DisplayName("응답코드 400를 반환한다")
            void it_returns_400() throws Exception {
                mockMvc.perform(patch("/diaries/1/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidAttributes))
                )
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("delete 메소드는")
    class Describe_delete {

        @Nested
        @DisplayName("존재하는 할 일의 id가 주어진다면")
        class Context_with_valid_id {

            @BeforeEach
            void setUp() {
                givenValidId = ID;
            }

            @Test
            @DisplayName("주어진 id를 갖는 할 일을 삭제하고 응답코드 200을 반환한다")
            void it_returns_task_and_200() throws Exception {
                mockMvc.perform(delete("/diaries/1/tasks/1"))
                        .andExpect(status().isOk());

                verify(taskService).deleteTask(givenValidId);
            }

            @Nested
            @DisplayName("존재하지 않는 할 일의 id가 주어진다면")
            class Context_with_invalid_id {

                @BeforeEach
                void setUp() {
                    givenInvalidId = NOT_EXIST_ID;
                }

                @Test
                @DisplayName("응답코드 404를 반환한다")
                void it_returns_404() throws Exception {
                    mockMvc.perform(delete("/diaries/1/tasks/100"))
                            .andExpect(status().isNotFound());

                    verify(taskService).deleteTask(givenInvalidId);
                }
            }
        }

        @AfterEach
        public void afterEach() {
            taskRepository.delete(task);
        }
    }
}
