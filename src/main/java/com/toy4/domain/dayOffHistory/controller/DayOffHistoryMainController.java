package com.toy4.domain.dayOffHistory.controller;

import com.toy4.domain.dayOffHistory.dto.request.DayOffCancellationRequest;
import com.toy4.domain.dayOffHistory.dto.request.DayOffModificationRequest;
import com.toy4.domain.dayOffHistory.dto.request.DayOffRegistrationRequest;
import com.toy4.domain.dayOffHistory.service.DayOffHistoryMainService;
import com.toy4.domain.dayOffHistory.exception.DayOffHistoryException;
import com.toy4.global.response.service.ResponseService;
import com.toy4.global.response.type.SuccessCode;
import com.toy4.global.utils.BindingResultHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class DayOffHistoryMainController {

    private final DayOffHistoryMainService dayOffHistoryMainService;
    private final ResponseService responseService;
    private final BindingResultHandler bindingResultHandler;

    @PostMapping("/schedules/day-off")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> requestDayOffRegistration(
            @Valid @RequestBody DayOffRegistrationRequest requestBody,
            BindingResult bindingResult) {

        if (bindingResult.hasFieldErrors()) {
            log.info("[POST /api/schedules/day-off] errors: {}", bindingResult.getFieldErrors());
            String errorMessage = bindingResultHandler.getErrorMessageFromBindingResult(bindingResult);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(responseService.failure(errorMessage));
        }

        dayOffHistoryMainService.registerDayOff(requestBody.toDto());

        return ResponseEntity.created(URI.create("/api/schedules"))
                .body(responseService.success(null, SuccessCode.SUCCESS));
    }

    @PutMapping("/schedules/day-off/{dayOffId}/status")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> requestDayOffCancellation(
            @PathVariable("dayOffId") Long dayOffHistoryId,
            @Valid @RequestBody DayOffCancellationRequest requestBody,
            BindingResult bindingResult) {

        if (bindingResult.hasFieldErrors()) {
            log.error("[POST /api/schedules/day-off] errors: {}", bindingResult.getFieldErrors());
            String errorMessage = bindingResultHandler.getErrorMessageFromBindingResult(bindingResult);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(responseService.failure(errorMessage));
        }

        dayOffHistoryMainService.cancelDayOffRegistrationRequest(dayOffHistoryId, requestBody.toDto());

        return ResponseEntity.ok(responseService.success(null, SuccessCode.SUCCESS));
    }

    @PutMapping("/schedules/day-off/{dayOffId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> requestDayOffUpdate(
            @PathVariable("dayOffId") Long dayOffHistoryId,
            @Valid @RequestBody DayOffModificationRequest requestBody,
            BindingResult bindingResult) {

        if (bindingResult.hasFieldErrors()) {
            log.error("[PUT /api/schedules/day-off/{}] errors: {}", dayOffHistoryId, bindingResult.getFieldErrors());
            String errorMessage = bindingResultHandler.getErrorMessageFromBindingResult(bindingResult);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(responseService.failure(errorMessage));
        }

        dayOffHistoryMainService.updateDayOffRegistrationRequest(dayOffHistoryId, requestBody.toDto());

        return ResponseEntity.ok(responseService.success(null, SuccessCode.SUCCESS));
    }

    @ExceptionHandler
    public ResponseEntity<?> handleDayOffHistoryException(DayOffHistoryException e) {
        return ResponseEntity.status(e.getHttpStatus())
                .body(responseService.failure(e.getErrorCode()));
    }
}
