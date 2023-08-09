package com.toy4.global.exception;

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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import static com.toy4.global.response.type.ErrorCode.*;


@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ResponseService responseService;

    @ExceptionHandler(EmployeeException.class)
    public CommonResponse<?> handleEmployeeException(EmployeeException e) {
        log.error("{} is occurred. {}", e.getErrorCode(), e.getErrorMessage());
        return responseService.failure(e.getErrorCode());
    }

    @ExceptionHandler(PositionException.class)
    public CommonResponse<?> handlePositionException(PositionException e) {
        log.error("{} is occurred. {}", e.getErrorCode(), e.getErrorMessage());
        return responseService.failure(e.getErrorCode());
    }

    @ExceptionHandler(StatusException.class)
    public CommonResponse<?> handleStatusException(StatusException e) {
        log.error("{} is occurred. {}", e.getErrorCode(), e.getErrorMessage());
        return responseService.failure(e.getErrorCode());
    }

    @ExceptionHandler(DepartmentException.class)
    public CommonResponse<?> handleDepartmentException(DepartmentException e) {
        log.error("{} is occurred. {}", e.getErrorCode(), e.getErrorMessage());
        return responseService.failure(e.getErrorCode());
    }

    @ExceptionHandler(DayOffByPositionException.class)
    public CommonResponse<?> handleDayOffByPositionException(DayOffByPositionException e) {
        log.error("{} is occurred. {}", e.getErrorCode(), e.getErrorMessage());
        return responseService.failure(e.getErrorCode());
    }

    @ExceptionHandler(ScheduleException.class)
    public CommonResponse<?> handleScheduleException(ScheduleException e){
        log.error("{} is occurred. {}", e.getErrorCode(), e.getErrorMessage());
        return responseService.failure(e.getErrorCode());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public CommonResponse<?> handleUploadFileException(MaxUploadSizeExceededException e) {
        log.error("MaxUploadSizeExceededException is occurred.", e);
        return responseService.failure(FILE_MAXIMUM_SIZE);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public CommonResponse<?> handlerUsernameNotFoundException(UsernameNotFoundException e) {
        log.error("UsernameNotFoundException is occurred.", e);
        return responseService.failure(LOAD_USER_FAILED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error("MethodArgumentNotValidException is occurred.", e);
        return responseService.failure(INVALID_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public CommonResponse<?> handleDataIntegrityViolationException(DataIntegrityViolationException e){
        log.error("DataIntegrityViolationException is occurred.", e);
        return responseService.failure(CONSTRAINT_VIOLATION);
    }

    @ExceptionHandler(Exception.class)
    public CommonResponse<?> handleException(Exception e){
        log.error("Exception is occurred.", e);
        return responseService.failure(INTERNAL_SERVER_ERROR);
    }

}
