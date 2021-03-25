package com.codesoom.project.controllers;

import com.codesoom.project.dto.ErrorResponse;
import com.codesoom.project.errors.DiaryNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 에러에 대한 메시지를 반환합니다.
 */
@ResponseBody
@ControllerAdvice
public class ControllerErrorAdvice {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(DiaryNotFoundException.class)
    public ErrorResponse handleDiaryNotFound() {
        return new ErrorResponse("Diary not found");
    }
}
