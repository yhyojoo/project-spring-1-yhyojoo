package com.codesoom.project.dto;

import com.codesoom.project.domain.Diary;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 다이어리 정보 응답 DTO.
 */
@Getter
@NoArgsConstructor
@ToString
public class DiaryResultData {
    private Long id;

    private String title;

    private String comment;

    @Builder
    public DiaryResultData(
            Long id, String title, String comment
    ) {
        this.id = id;
        this.title = title;
        this.comment = comment;
    }

    public static DiaryResultData of(Diary diary) {
        return DiaryResultData.builder()
                .id(diary.getId())
                .title(diary.getTitle())
                .comment(diary.getComment())
                .build();
    }
}
