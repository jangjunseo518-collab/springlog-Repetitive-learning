package com.springlog.repetitivelearning.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Set;

public record AddTagRequest(

    @Size(max = 10, message = "태그는 10개 까지 추가할 수 있습니다.")
    @NotEmpty
    Set<
        @Size(max = 20, message = "태그는 20자를 넘을 수 없습니다.")
        @Pattern(regexp = "^[a-zA-Z가-힣0-9@#-]+$",
            message = "태그는 한글, 영문, 숫자, @, #, -만 입력 가능합니다.")
            String> tags
) {

}
