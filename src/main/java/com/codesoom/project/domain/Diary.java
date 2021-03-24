package com.codesoom.project.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 다이어리 정보.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Diary {
    @Id
    @GeneratedValue
    private Long id;

    @Setter
    private String title;

    @Setter
    private String comment;

    public void updateWith(Diary source) {
        this.title = source.getTitle();
        this.comment = source.getComment();
    }

    public static Diary of(Diary diary) {
        return Diary.builder()
                .id(diary.getId())
                .title(diary.getTitle())
                .comment(diary.getComment())
                .build();
    }
}
