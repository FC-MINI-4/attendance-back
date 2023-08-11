package com.toy4.domain.dayOffHistory.dto.request;

import com.toy4.domain.dayOffHistory.dto.DayOffStatusUpdate;
import com.toy4.domain.schedule.RequestStatus;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class DayOffStatusUpdateRequest {

	@NotNull
	private Long dayOffId;
	@NotBlank
	private String status;

	public DayOffStatusUpdate toDto() {
		return DayOffStatusUpdate.builder()
			.id(dayOffId)
			.status(RequestStatus.getByDescription(status))
			.build();
	}
}
