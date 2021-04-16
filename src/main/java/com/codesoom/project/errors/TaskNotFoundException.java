package com.codesoom.project.errors;

/**
 * 할 일을 찾을 수 없을 경우 발생하는 예외.
 */
public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Long id) {
        super("Task ID not found: " + id);
    }
}
