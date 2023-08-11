package com.toy4.domain.employee.controller;

import static com.toy4.global.response.type.SuccessCode.AVAILABLE_EMAIL;
import static com.toy4.global.response.type.SuccessCode.COMPLETE_CHANGE_PASSWORD;
import static com.toy4.global.response.type.SuccessCode.COMPLETE_EMAIL_TRANSMISSION;
import static com.toy4.global.response.type.SuccessCode.COMPLETE_SIGNUP;
import static com.toy4.global.response.type.SuccessCode.MATCH_PASSWORD;

import com.toy4.domain.employee.dto.ChangePassword;
import com.toy4.domain.employee.dto.ResetPassword;
import com.toy4.domain.employee.dto.Signup;
import com.toy4.domain.employee.dto.ValidateMatchPassword;
import com.toy4.domain.employee.dto.request.ChangePasswordRequest;
import com.toy4.domain.employee.dto.request.EmailDuplicateCheckRequest;
import com.toy4.domain.employee.dto.request.ResetPasswordRequest;
import com.toy4.domain.employee.dto.request.SendPasswordResetEmailRequest;
import com.toy4.domain.employee.dto.request.SignupRequest;
import com.toy4.domain.employee.dto.request.ValidateMatchPasswordRequest;
import com.toy4.domain.employee.dto.response.SignupResponse;
import com.toy4.domain.employee.service.EmployeeService;
import com.toy4.global.aop.EmployeeLock;
import com.toy4.global.response.dto.CommonResponse;
import com.toy4.global.response.service.ResponseService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class EmployeeAccountController {

    private final EmployeeService employeeService;
    private final ResponseService responseService;

    @PostMapping("/check-email")
    public ResponseEntity<?> checkEmailNotExist(
            @Valid @RequestBody EmailDuplicateCheckRequest request
            , BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            String errorMessage = getErrorMessageFromBindingResult(bindingResult);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseService.failure(errorMessage));
        }

        employeeService.validateUniqueEmail(request.getEmail());
        CommonResponse<?> response = responseService.successWithNoContent(AVAILABLE_EMAIL);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/users/{employeeId}/check-password")
    public ResponseEntity<?> checkMatchPasswordWithDB(
            @RequestBody ValidateMatchPasswordRequest request
            , @PathVariable String employeeId) {
        employeeService.validateMatchPasswordWithDB(ValidateMatchPassword.fromRequest(request), Long.valueOf(employeeId));
        CommonResponse<?> response = responseService.successWithNoContent(MATCH_PASSWORD);
        return ResponseEntity.ok(response);
    }

    @EmployeeLock
    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @Valid @RequestBody SignupRequest request
            , BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            String errorMessage = getErrorMessageFromBindingResult(bindingResult);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseService.failure(errorMessage));
        }

        SignupResponse data = employeeService.signup(Signup.fromRequest(request));
        CommonResponse<?> response = responseService.success(data, COMPLETE_SIGNUP);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PostMapping("/users/send-password-reset-email")
    public ResponseEntity<?> sendPasswordResetEmail(
            @RequestBody SendPasswordResetEmailRequest request
    ) {
        employeeService.sendPasswordChangeEmail(request.getEmail());
        CommonResponse<?> response = responseService.successWithNoContent(COMPLETE_EMAIL_TRANSMISSION);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/users/password/reset")
    public ResponseEntity<?> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request
            , BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            String errorMessage = getErrorMessageFromBindingResult(bindingResult);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseService.failure(errorMessage));
        }

        employeeService.resetPassword(ResetPassword.fromRequest(request));
        CommonResponse<?> response = responseService.successWithNoContent(COMPLETE_CHANGE_PASSWORD);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/users/{employeeId}/password/change")
    public ResponseEntity<?> changePassword(
            @Valid @RequestBody ChangePasswordRequest request
            , @PathVariable String employeeId
            , BindingResult bindingResult
            ) {
        if (bindingResult.hasErrors()) {
            String errorMessage = getErrorMessageFromBindingResult(bindingResult);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseService.failure(errorMessage));
        }

        employeeService.changePassword(ChangePassword.fromRequest(request), Long.valueOf(employeeId));
        CommonResponse<?> response = responseService.successWithNoContent(COMPLETE_CHANGE_PASSWORD);
        return ResponseEntity.ok(response);
    }

    /** bindingResult ErrorMessage 반환 */
    private String getErrorMessageFromBindingResult(BindingResult bindingResult) {
        List<FieldError> errors = bindingResult.getFieldErrors();
        return errors.get(0).getDefaultMessage();
    }

}