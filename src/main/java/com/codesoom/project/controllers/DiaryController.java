package com.codesoom.project.controllers;

import com.codesoom.project.application.DiaryService;
import com.codesoom.project.domain.Diary;
import com.codesoom.project.dto.DiaryCreateData;
import com.codesoom.project.dto.DiaryResultData;
import com.codesoom.project.dto.DiaryUpdateData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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

    /**
     * 새로운 다이어리를 생성합니다.
     *
     * @param diaryCreateData 생성할 다이어리 정보
     * @return 생성된 다이어리
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DiaryResultData create(@RequestBody @Valid DiaryCreateData diaryCreateData) {
        return diaryService.createDiary(diaryCreateData);
    }

    /**
     * 주어진 id에 해당하는 다이어리 정보를 수정합니다.
     *
     * @param id 다이어리 식별자
     * @param diaryUpdateData 수정할 다이어리 정보
     * @return 수정된 다이어리
     */
    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public DiaryResultData update(@PathVariable Long id,
                        @RequestBody @Valid DiaryUpdateData diaryUpdateData) {
        return diaryService.updateDiary(id, diaryUpdateData);
    }

    /**
     * 주어진 id에 해당하는 다이어리를 삭제합니다.
     *
     * @param id 다이어리 식별자
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        diaryService.deleteDiary(id);
    }
}
