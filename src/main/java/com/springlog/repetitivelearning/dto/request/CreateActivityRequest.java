package com.springlog.repetitivelearning.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.springlog.repetitivelearning.domain.type.ActivityCategory;
import com.springlog.repetitivelearning.domain.type.Visibility;
import com.springlog.repetitivelearning.validation.ValidActivityByType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@ValidActivityByType
public record CreateActivityRequest(
    @NotBlank(message = "제목은 필수 입니다.")
    @Size(max = 100, message = "제목은 100자 까지 입력 가능합니다.")
    String title,

    @Max(value = 1440, message = "학습 시간은 24 시간을 넘을 수 없습니다.")
    @Min(value = 1, message = "학습 시간은 최소 1분 이상이여야 합니다.")
    int minutes,

    @Size(max = 10, message = "태그는 최대 10개까지 추가할 수 있습니다.")
    Set<
        @Size(max = 20, message = "각 태그는 20자를 넘을 수 없습니다.")
        @Pattern(regexp = "^[a-zA-Z가-힣0-9@#-]+$", message = "태그는 힌글, 영문, 숫자, @, #, -만 입력 가능합니다.")
        String> tags,

    @PastOrPresent(message = "학습한 날짜는 미래일 수 없습니다.")
    LocalDate startDate,

    @NotNull(message = "공개 여부는 필수 항목입니다.")
    Visibility visibility,

    @NotNull(message = "카테고리를 정해주세요.")
    @JsonProperty("category")
    ActivityCategory type,

    @Size(max = 50, message = "강사 이름은 50자를 넘을 수 없습니다.")
    String instructorName,

    @Min(value = 0, message = "완료율은 0 이상이여야 합니다.")
    @Max(value = 100, message = "완료율은 100을 넘을 수 없습니다.")
    Integer completionRate,

    @Size(max = 200, message = "책 제목은 200자를 넘을 수 없습니다.")
    String bookTitle,
    Long ownerId //추후 로그인 기능이 생기면 요청에서 삭제.
) {

}
