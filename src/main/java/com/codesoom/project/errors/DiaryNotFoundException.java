package com.codesoom.project.errors;

/**
 * 다이어리를 찾을 수 없을 경우 발생하는 예외.
 */
public class DiaryNotFoundException extends RuntimeException {
    public DiaryNotFoundException(Long id) {
        super("Diary ID not found: " + id);
    }
}
