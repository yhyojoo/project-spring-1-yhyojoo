package com.codesoom.project.controllers;

import com.codesoom.project.application.DiaryService;
import com.codesoom.project.domain.Diary;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 다이어리 관련 요청을 처리합니다.
 */
@RestController
@RequestMapping("/diaries")
@CrossOrigin
@RequiredArgsConstructor
public class DiaryController {
    private final DiaryService diaryService;

    /**
     * 전체 다이어리 목록을 반환합니다.
     *
     * @return 전체 다이어리 목록
     */
    @GetMapping
    public List<Diary> list() {
        return diaryService.getDiaries();
    }

    /**
     * 주어진 id에 해당하는 다이어리를 반환합니다.
     *
     * @param id 다이어리 식별자
     * @return 주어진 id를 갖는 다이어리
     */
    @GetMapping("{id}")
    public Diary detail(@PathVariable Long id) {
        return diaryService.getDiary(id);
    }
}
