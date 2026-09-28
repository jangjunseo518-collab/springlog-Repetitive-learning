package com.springlog.repetitivelearning.service.helper.page;

import static com.springlog.repetitivelearning.service.helper.HelperMethod.publicVisibilityValidator;
import static com.springlog.repetitivelearning.service.helper.page.PageSizeValidator.resolvePageSize;
import static org.springframework.data.domain.Sort.Direction.DESC;

import com.springlog.repetitivelearning.domain.type.Visibility;
import com.springlog.repetitivelearning.dto.request.PagingRequest;
import com.springlog.repetitivelearning.exception.PageValidationFailureException;
import com.springlog.repetitivelearning.exception.PagingRequestException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PagingHelper {

  //page, visibility, sort 검증 헬퍼 메서드
  public static PagingSetup buildPagingSetup(PagingRequest pagingRequest) {

    Visibility visibility = publicVisibilityValidator(pagingRequest.visibility());
    PageSizeResult pageSizeResult = resolvePageSize(pagingRequest.requestedSize());
    String requestSort = pagingRequest.sort();
    Sort.Direction direction = pagingRequest.direction() == null
        ? DESC : pagingRequest.direction();
    int page = pagingRequest.page() == null ? 0 : pagingRequest.page();

    if(page < 0){
      throw new PageValidationFailureException(page);
    }

    String sortField = switch (requestSort == null? "id":requestSort) {
      case "id" -> "id";
      case "title" -> "title";
      case "minutes" -> "minutes";
      default -> throw new PagingRequestException(requestSort);
    };

    Sort sort = Sort.by(direction, sortField);

    Pageable pageable = PageRequest.of(page, pageSizeResult.size(), sort);

    return PagingSetup.of(visibility, pageSizeResult.adjustedSizeMessage(), pageable);

  }


}
