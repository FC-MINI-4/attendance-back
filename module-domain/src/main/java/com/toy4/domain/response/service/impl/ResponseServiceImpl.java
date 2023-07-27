package com.toy4.domain.response.service.impl;

import static com.toy4.domain.response.type.SuccessCode.SUCCESS;

import com.toy4.domain.response.dto.CommonResponse;
import com.toy4.domain.response.service.ResponseService;
import com.toy4.domain.response.type.ErrorCode;
import com.toy4.domain.response.type.SuccessCode;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ResponseServiceImpl implements ResponseService {

    @Override
    public <T> CommonResponse<T> success(T data, SuccessCode successCode) {
        return CommonResponse.<T>builder()
                .success(true)
                .code(successCode.name())
                .message(successCode.getMessage())
                .data(data)
                .build();
    }

    @Override
    public <T> CommonResponse<List<T>> successList(List<T> data, SuccessCode successCode) {
        return CommonResponse.<List<T>>builder()
                .success(true)
                .code(successCode.name())
                .message(successCode.getMessage())
                .data(data)
                .build();
    }

    @Override
    public CommonResponse<?> successWithNoContent(SuccessCode successCode) {
        return CommonResponse.builder()
                .success(true)
                .code(successCode.name())
                .message(successCode.getMessage())
                .build();
    }

    @Override
    public <T> CommonResponse<T> failure(ErrorCode errorCode) {
        return CommonResponse.<T>builder()
                .success(false)
                .code(errorCode.toString())
                .message(errorCode.getMessage())
                .build();
    }
}