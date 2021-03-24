package com.codesoom.project.dto;
import com.github.dozermapper.core.Mapping;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 다이어리 DTO.
 */
@Getter
@NoArgsConstructor
public class DiaryData {
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
