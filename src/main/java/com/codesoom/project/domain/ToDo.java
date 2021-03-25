package com.codesoom.project.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class ToDo {
    @Id
    @GeneratedValue
    private Long id;

    @Setter
    private String title;

    @Builder
    public ToDo(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
