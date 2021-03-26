package com.codesoom.project.dto;

import com.github.dozermapper.core.Mapping;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 할 일 DTO.
 */
@Getter
@NoArgsConstructor
public class TaskData {
    @NotBlank(message = "할 일을 입력해 주세요.")
    @Mapping("title")
    private String title;

    @Builder
    public TaskData(String title) {
        this.title = title;
    }
}
