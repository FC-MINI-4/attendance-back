package com.toy4.domain.schedule;

import com.toy4.domain.schedule.dto.response.ScheduleResponse;
import com.toy4.global.response.dto.CommonResponse;
import com.toy4.global.response.service.ResponseService;
import com.toy4.global.response.type.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/schedules")
@RestController
public class ScheduleController {

    private final ScheduleMainService scheduleMainService;
    private final ResponseService responseService;

    @GetMapping("/{employeeId}")
    public CommonResponse<?> getSchedules(
            @PathVariable Long employeeId) {

        ScheduleResponse scheduleResponse = scheduleMainService.getSchedules(employeeId);

        return responseService.success(scheduleResponse, SuccessCode.SUCCESS);
    }

}
