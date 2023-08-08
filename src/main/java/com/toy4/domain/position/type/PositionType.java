package com.toy4.domain.position.type;

import java.util.Arrays;
import com.toy4.domain.position.exception.PositionException;
import com.toy4.global.response.type.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PositionType {

  STAFF("사원"),
  ASSISTANT_MANAGER("대리"),
  MANAGER("과장"),
  DEPUTY_GENERAL_MANAGER("차장"),
  GENERAL_MANAGER("부장"),
  ;

  private final String description;

  public static PositionType getByTypeString(String type) {
    return Arrays.stream(values())
        .filter(positionType -> positionType.description.equals(type))
        .findFirst()
        .orElseThrow(() -> new PositionException(ErrorCode.INVALID_DAY_OFF_TYPE));
  }
}
