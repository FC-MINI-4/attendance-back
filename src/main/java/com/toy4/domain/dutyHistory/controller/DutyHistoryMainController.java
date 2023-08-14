package com.toy4.domain.dutyHistory.controller;

import com.toy4.domain.dutyHistory.dto.request.DutyCancellationRequest;
import com.toy4.domain.dutyHistory.dto.request.DutyModificationRequest;
import com.toy4.domain.dutyHistory.dto.request.DutyRegistrationRequest;
import com.toy4.domain.dutyHistory.exception.DutyHistoryException;
import com.toy4.domain.dutyHistory.service.DutyHistoryMainService;
import com.toy4.global.response.service.ResponseService;
import com.toy4.global.response.type.SuccessCode;
import com.toy4.global.utils.BindingResultHandler;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/schedules/duty")
@RestController
public class DutyHistoryMainController {

    private final DutyHistoryMainService dutyHistoryMainService;
    private final ResponseService responseService;
    private final BindingResultHandler bindingResultHandler;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> requestDutyRegistration(
            @Valid @RequestBody DutyRegistrationRequest requestBody,
            BindingResult bindingResult) {

        if (bindingResult.hasFieldErrors()) {
            log.info("[POST /api/schedules/duty] errors");
            String errorMessage = bindingResultHandler.getErrorMessageFromBindingResult(bindingResult);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(responseService.failure(errorMessage));
        }

        dutyHistoryMainService.registerDuty(requestBody.toDto());

        return ResponseEntity.created(URI.create("/api/schedules"))
                .body(responseService.success(null, SuccessCode.SUCCESS));
    }

    @PutMapping("/{dutyId}/status")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> requestDutyCancellation(
            @PathVariable("dutyId") Long dutyHistoryId,
            @Valid @RequestBody DutyCancellationRequest requestBody,
            BindingResult bindingResult) {

        if (bindingResult.hasFieldErrors()) {
            log.error("[error] PUT /api/schedules/duty/{}/status: {}", dutyHistoryId, bindingResult.getFieldErrors());
            String errorMessage = bindingResultHandler.getErrorMessageFromBindingResult(bindingResult);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(responseService.failure(errorMessage));
        }

        dutyHistoryMainService.cancelDutyRegistrationRequest(dutyHistoryId, requestBody.toDto());

        return ResponseEntity.ok(responseService.success(null, SuccessCode.SUCCESS));
    }

    @PutMapping("/{dutyId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> requestDutyUpdate(
            @PathVariable("dutyId") Long dutyHistoryId,
            @Valid @RequestBody DutyModificationRequest requestBody,
            BindingResult bindingResult) {

        if (bindingResult.hasFieldErrors()) {
            log.error("[error] PUT /api/schedules/duty/{}: {}", dutyHistoryId, bindingResult.getFieldErrors());
            String errorMessage = bindingResultHandler.getErrorMessageFromBindingResult(bindingResult);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(responseService.failure(errorMessage));
        }

        dutyHistoryMainService.updateDutyRegistrationRequest(dutyHistoryId, requestBody.toDto());

        return ResponseEntity.ok(responseService.success());
    }

    @ExceptionHandler
    public ResponseEntity<?> handleDutyHistoryException(DutyHistoryException e) {
        return ResponseEntity.status(e.getHttpStatus())
                .body(responseService.failure(e.getErrorCode()));
    }
}
