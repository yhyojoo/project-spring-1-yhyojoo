package com.codesoom.project.dto;

import com.github.dozermapper.core.Mapping;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 다이어리 DTO.
 */
@Getter
@NoArgsConstructor
public class DiaryData {
    @NotBlank(message = "다이어리 제목을 입력해 주세요.")
    @Mapping("title")
    private String title;

    @Mapping("comment")
    private String comment;

    @Builder
    public DiaryData(String title, String comment) {
        this.title = title;
        this.comment = comment;
    }
}
