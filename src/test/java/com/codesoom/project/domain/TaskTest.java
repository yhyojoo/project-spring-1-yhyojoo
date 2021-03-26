package com.codesoom.project.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {
    private static final Long ID = 1L;
    private static final String TITLE = "첫 번째 할 일";
    private static final String UPDATE_TITLE = "새로운 할 일";

    private Task task;
    private Task updatedTask;

    @BeforeEach
    void setUp() {
        task = Task.builder()
                .id(ID)
                .title(TITLE)
                .build();

        updatedTask = Task.builder()
                .id(ID)
                .title(UPDATE_TITLE)
                .build();
    }

    @Test
    void creationWithBuilder() {
        assertThat(task.getId()).isEqualTo(ID);
        assertThat(task.getTitle()).isEqualTo(TITLE);
    }

    @Test
    void updateWith() {
        task.updateWith(updatedTask);

        assertThat(task.getId()).isEqualTo(ID);
        assertThat(task.getTitle()).isEqualTo(UPDATE_TITLE);
    }
}
