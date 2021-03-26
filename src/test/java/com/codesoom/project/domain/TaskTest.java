package com.codesoom.project.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {
    private static final Long ID = 1L;
    private static final String TITLE = "첫 번째 할 일";

    @Test
    void creationWithBuilder() {
        Task task = Task.builder()
                .id(ID)
                .title(TITLE)
                .build();

        assertThat(task.getId()).isEqualTo(ID);
        assertThat(task.getTitle()).isEqualTo(TITLE);
    }
}
