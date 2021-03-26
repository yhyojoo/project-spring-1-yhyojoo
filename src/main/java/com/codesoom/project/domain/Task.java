package com.codesoom.project.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 할 일 정보.
 */
@Getter
@NoArgsConstructor
@Entity
public class Task {
    @Id
    @GeneratedValue
    private Long id;

    @Setter
    private String title;

    @Builder
    public Task(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public void updateWith(Task source) {
        this.title = source.getTitle();
    }
}
