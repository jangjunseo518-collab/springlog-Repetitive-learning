package com.springlog.repetitivelearning.dto.request;

import com.springlog.repetitivelearning.domain.type.ActivityCategory;
import com.springlog.repetitivelearning.domain.type.Visibility;
import com.springlog.repetitivelearning.validation.ValidActivityByCategory;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@ValidActivityByCategory
public record CreateActivityRequest(
    @NotBlank
    @Size(max = 100,  message = "제목은 100자를 넘을 수 없습니다.")
    String title,

    @Max(value = 1440, message = "학습 시간은 24시간(1440분)을 넘을 수 없습니다.")
    @Min(value = 1, message = "학습 시간은 1분 이상을 입력해주세요.")
    int minutes,

    @PastOrPresent(message = "학습한 날은 미래일 수 없습니다.")
    LocalDate studiedOn,

    @Size(max = 10, message = "태그는 10개 까지 추가할 수 있습니다.")
    Set<
        @Size(max = 20, message = "각각의 태그는 20자를 넘을 수 없습니다.")
        String> tags,

    @NotNull
    Visibility visibility,
    @NotNull
    ActivityCategory category,

    @Size(max = 50, message = "강사 이름은 50자를 넘을 수 없습니다.")
    String instructorName,

    @Min(value = 0, message = "학습 완료율은 0이상 입력해주세요.")
    @Max(value = 100, message = "학습 완료율은 100을 넘을 수 없습니다.")
    Integer completionRate,
    @Size(max = 100,message = "책 제목은 100자를 넘을 수 없습니다.")
    String bookTitle,
    Long ownerId
) {

}
