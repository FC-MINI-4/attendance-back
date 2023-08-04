package com.toy4.global.response.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "로그인에 실패했습니다."),
    LOAD_USER_FAILED(HttpStatus.UNAUTHORIZED, "회원 정보를 불러오는데 실패했습니다."),

    EMPLOYEE_TRANSACTION_LOCK(HttpStatus.UNAUTHORIZED, "이메일이 이미 사용 중입니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류가 발생하였습니다."),
    CONSTRAINT_VIOLATION(HttpStatus.CONFLICT, "제약 조건 위반"),

    ALREADY_EXISTS_EMAIL(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일입니다."),
    ALREADY_EXISTS_PHONE(HttpStatus.BAD_REQUEST, "이미 사용중인 전화번호입니다."),

    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INVALID_REQUEST_POSITION_ID(HttpStatus.BAD_REQUEST, "잘못된 직급 요청입니다."),
    INVALID_REQUEST_DEPARTMENT_TYPE(HttpStatus.BAD_REQUEST, "잘못된 부서 요청입니다."),
    INVALID_REQUEST_POSITION_TYPE(HttpStatus.BAD_REQUEST, "잘못된 직급 요청입니다."),
    INVALID_REQUEST_STATUS_TYPE(HttpStatus.BAD_REQUEST, "잘못된 회원 상태 요청입니다."),

    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "존재하지 않는 이메일입니다."),
    INVALID_USERNAME(HttpStatus.BAD_REQUEST, "이름은 2글자 이상 3글자 이하이며 한글로만 작성되어야 합니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호는 공백은 사용할 수 없으며, 영어와 숫자를 혼용하여 최소 8글자 이상 최대 16글자 이하로 작성되어야 합니다."),
    INVALID_PHONE_FORMAT(HttpStatus.BAD_REQUEST, "전화번호 형식이 올바르지 않습니다."),

    ENTITY_NOT_FOUND(HttpStatus.BAD_REQUEST, "요청된 데이터를 찾을 수 없습니다."),
    UNAUTHENTICATED_REQUEST(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자의 요청입니다."),
    FILE_CANNOT_BE_PROCESSED(HttpStatus.BAD_REQUEST, "처리할 수 없는 파일입니다."),
    FILE_MAXIMUM_SIZE(HttpStatus.BAD_REQUEST, "파일 크키는 1MB 이하여야 합니다."),
    ALREADY_REPORTED(HttpStatus.BAD_REQUEST, "이미 신고한 게시글입니다."),

    MISMATCH_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    EMPLOYEE_NOT_FOUND(HttpStatus.BAD_REQUEST, "요청된 id를 가진 직원 정보를 찾을 수 없습니다."),
    INVALID_DAY_OFF_TYPE(HttpStatus.BAD_REQUEST, "유효한 연차 유형은 ['오전 반차', '오후 반차', '연차', '특별 휴가'] 입니다."),

    DAY_OFF_HISTORIES_NOT_FOUND(HttpStatus.BAD_REQUEST, "연차 리스트 내역이 존재하지 않습니다."),
    DUTY_HISTORIES_NOT_FOUND(HttpStatus.BAD_REQUEST, "당직 리스트 내역이 존재하지 않습니다."),
    EMPLOYEE_APPROVED_DUTY_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 직원의 승인된 당직 내역이 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
    public static ErrorCode getByErrorCodeName(String errorCodeName) {
        Optional<ErrorCode> errorCode = Arrays.stream(ErrorCode.values())
                .filter(code -> code.name().equals(errorCodeName))
                .findFirst();

        return errorCode.orElse(null);
    }
}
