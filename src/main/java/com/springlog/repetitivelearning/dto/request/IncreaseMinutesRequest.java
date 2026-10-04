package com.springlog.repetitivelearning.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record IncreaseMinutesRequest(
    @Max(value = 1440, message = "학습 시간은 하루(1440분)을 넘을 수 없습니다.")
    @Min(value = 1, message = "학습 시간은 최소 1분 이상 부터 입력 가능합니다.")
    int minutes
) {

}
