package com.codesoom.project.dto;

import com.codesoom.project.domain.Task;
import com.github.dozermapper.core.Mapping;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 할 일 추가 요청 DTO.
 */
@Getter
@NoArgsConstructor
public class TaskCreateData {
    @NotBlank(message = "할 일을 입력해 주세요.")
    @Mapping("title")
    private String title;

    @Builder
    public TaskCreateData(String title) {
        this.title = title;
    }

    public Task toEntity() {
        return Task.builder()
                .title(this.title)
                .build();
    }
}
