package com.toy4.global.exception;

import static com.toy4.global.response.type.ErrorCode.CONSTRAINT_VIOLATION;
import static com.toy4.global.response.type.ErrorCode.FILE_MAXIMUM_SIZE;
import static com.toy4.global.response.type.ErrorCode.INTERNAL_SERVER_ERROR;
import static com.toy4.global.response.type.ErrorCode.INVALID_REQUEST;
import static com.toy4.global.response.type.ErrorCode.LOAD_USER_FAILED;
import static com.toy4.global.response.type.ErrorCode.NO_ACCESS;

import com.toy4.domain.dayOffByPosition.exception.DayOffByPositionException;
import com.toy4.domain.department.exception.DepartmentException;
import com.toy4.domain.employee.exception.EmployeeException;
import com.toy4.domain.position.exception.PositionException;
import com.toy4.domain.schedule.ScheduleException;
import com.toy4.domain.status.exception.StatusException;
import com.toy4.global.response.dto.CommonResponse;
import com.toy4.global.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;


@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ResponseService responseService;

    @ExceptionHandler(EmployeeException.class)
    public ResponseEntity<?> handleEmployeeException(EmployeeException e) {
        log.error("{} is occurred. {}", e.getErrorCode(), e.getErrorMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(responseService.failure(e.getErrorCode()));
    }

    @ExceptionHandler(PositionException.class)
    public ResponseEntity<?> handlePositionException(PositionException e) {
        log.error("{} is occurred. {}", e.getErrorCode(), e.getErrorMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(responseService.failure(e.getErrorCode()));
    }

    @ExceptionHandler(StatusException.class)
    public ResponseEntity<?> handleStatusException(StatusException e) {
        log.error("{} is occurred. {}", e.getErrorCode(), e.getErrorMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(responseService.failure(e.getErrorCode()));
    }

    @ExceptionHandler(DepartmentException.class)
    public ResponseEntity<?> handleDepartmentException(DepartmentException e) {
        log.error("{} is occurred. {}", e.getErrorCode(), e.getErrorMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(responseService.failure(e.getErrorCode()));
    }

    @ExceptionHandler(DayOffByPositionException.class)
    public ResponseEntity<?> handleDayOffByPositionException(DayOffByPositionException e) {
        log.error("{} is occurred. {}", e.getErrorCode(), e.getErrorMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(responseService.failure(e.getErrorCode()));
    }

    @ExceptionHandler
    public ResponseEntity<?> handleScheduleException(ScheduleException e) {
        log.error("{} is occurred. {}", e.getErrorCode(), e.getErrorMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(responseService.failure(e.getErrorCode()));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleUploadFileException(MaxUploadSizeExceededException e) {
        log.error("MaxUploadSizeExceededException is occurred.", e);
        return ResponseEntity.status(FILE_MAXIMUM_SIZE.getHttpStatus()).body(responseService.failure(FILE_MAXIMUM_SIZE));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handlerUsernameNotFoundException(UsernameNotFoundException e) {
        log.error("UsernameNotFoundException is occurred.", e);
        return ResponseEntity.status(LOAD_USER_FAILED.getHttpStatus()).body(responseService.failure(LOAD_USER_FAILED));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e){
        log.error("MethodArgumentNotValidException is occurred.", e);
        return ResponseEntity.status(NO_ACCESS.getHttpStatus()).body(responseService.failure(NO_ACCESS));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error("MethodArgumentNotValidException is occurred.", e);
        return ResponseEntity.status(INVALID_REQUEST.getHttpStatus()).body(responseService.failure(INVALID_REQUEST));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException e){
        log.error("DataIntegrityViolationException is occurred.", e);
        return ResponseEntity.status(CONSTRAINT_VIOLATION.getHttpStatus()).body(responseService.failure(CONSTRAINT_VIOLATION));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e){
        log.error("Exception is occurred.", e);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR.getHttpStatus()).body(responseService.failure(INTERNAL_SERVER_ERROR));
    }

}
