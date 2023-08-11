package com.toy4.global.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Locale;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class BindingResultHandler {

    private final MessageSource ms;

    /** bindingResult ErrorMessage 반환 */
    public String getErrorMessageFromBindingResult(BindingResult bindingResult) {
        FieldError fieldError = Objects.requireNonNull(bindingResult.getFieldError());
        for (String code : Objects.requireNonNull(fieldError.getCodes())) {
            try {
                return ms.getMessage(code, fieldError.getArguments(), Locale.getDefault());
            } catch (NoSuchMessageException ignored) {
            }
        }
        log.error("[{}]에 대하여 적절한 에러 메시지를 찾을 수 없습니다.", fieldError.getField());
        return null;
    }
}
