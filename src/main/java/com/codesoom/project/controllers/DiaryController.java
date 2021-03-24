package com.codesoom.project.controllers;

import com.codesoom.project.application.DiaryService;
import com.codesoom.project.domain.Diary;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
}
