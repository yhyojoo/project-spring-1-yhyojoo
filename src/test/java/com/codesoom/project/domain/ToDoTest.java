package com.codesoom.project.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ToDoTest {
    private static final Long ID = 1L;
    private static final String TITLE = "첫 번째 할 일";

    @Test
    void creationWithBuilder() {
        ToDo toDo = ToDo.builder()
                .id(ID)
                .title(TITLE)
                .build();

        assertThat(toDo.getId()).isEqualTo(ID);
        assertThat(toDo.getTitle()).isEqualTo(TITLE);
    }
}
