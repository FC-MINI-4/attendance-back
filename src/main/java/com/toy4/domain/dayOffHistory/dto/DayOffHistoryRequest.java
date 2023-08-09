package com.toy4.domain.dayOffHistory.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.toy4.domain.schedule.RequestStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@Builder
public class DayOffHistoryRequest {

	@NotNull
	private Long dayOffId;
	@NotBlank(message = "상태가 누락되었습니다.")
	private String status;

	public static DayOffHistoryDto to(DayOffHistoryRequest request) {
		return DayOffHistoryDto.builder()
			.id(request.getDayOffId())
			.status(RequestStatus.getByDescription(request.getStatus()))
			.build();
	}
}
