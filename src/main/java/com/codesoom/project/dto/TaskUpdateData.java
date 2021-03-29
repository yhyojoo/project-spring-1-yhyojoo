package com.codesoom.project.dto;

import com.github.dozermapper.core.Mapping;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 할 일 수정 요청 DTO.
 */
@Getter
@NoArgsConstructor
public class TaskUpdateData {
    @NotBlank(message = "할 일을 입력해 주세요.")
    @Mapping("title")
    private String title;

    @Builder
    public TaskUpdateData(String title) {
        this.title = title;
    }
}
