package com.toy4.domain.dutyHistory.dto.response;

import static com.toy4.global.date.DateFormatter.*;
import com.toy4.domain.dutyHistory.domain.DutyHistory;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApprovedDutyResponse {
	private Long dutyId;
	private final String type = "당직";
	private String  requestDate;
	private String date;
	private String status;

	public static ApprovedDutyResponse from(DutyHistory dutyHistory) {
		return ApprovedDutyResponse.builder()
			.dutyId(dutyHistory.getId())
			.requestDate(dutyHistory.getCreatedAt().format(formatter))
			.date(dutyHistory.getDate().format(formatter))
			.status(dutyHistory.getStatus().getDescription())
			.build();
	}
}
