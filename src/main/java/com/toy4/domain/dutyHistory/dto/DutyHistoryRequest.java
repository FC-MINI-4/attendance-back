package com.toy4.domain.dutyHistory.dto;
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
public class DutyHistoryRequest {

	@NotNull
	private Long dutyId;
	@NotBlank(message = "상태가 누락되었습니다.")
	private String status;

	public static DutyHistoryDto to(DutyHistoryRequest request) {
		return DutyHistoryDto.builder()
			.id(request.getDutyId())
			.status(RequestStatus.getByDescription(request.getStatus()))
			.build();
	}
}
