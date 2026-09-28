package com.springlog.repetitivelearning.service.helper.paging;

import static com.springlog.repetitivelearning.service.helper.VisibilityValidator.visibilityPublicValidator;
import static com.springlog.repetitivelearning.service.helper.paging.PageSizeClamper.clampedPageSize;

import com.springlog.repetitivelearning.domain.type.Visibility;
import com.springlog.repetitivelearning.dto.request.PagingRequest;
import com.springlog.repetitivelearning.exception.InvalidSortFieldException;
import com.springlog.repetitivelearning.exception.PageValidationFailureException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PagingHelper {

  public static PagingSetup buildPagingSetup(PagingRequest pagingRequest) {
    Visibility visibility = visibilityPublicValidator(pagingRequest.visibility());
    PageSizeResult pageSizeResult = clampedPageSize(pagingRequest.pageSize());
    Sort.Direction direction = pagingRequest.sortDirection()== null?
                                 Direction.DESC : pagingRequest.sortDirection();
    String requestSort = pagingRequest.sort();
    int page = pagingRequest.page() == null ? 0 : pagingRequest.page();

    if(page < 0) {
      throw new PageValidationFailureException(page);
    }

    String sortField = switch (requestSort==null? "id":requestSort){
      case "id" -> "id";
      case "minutes" -> "minutes";
      case "title" -> "title";
      default -> throw new InvalidSortFieldException(requestSort);
    };

    Sort sort = Sort.by(direction, sortField);
    Pageable pageable = PageRequest.of(page, pageSizeResult.pageSize(), sort);

   return PagingSetup.of(visibility, pageSizeResult.adjustedMessage(),pageable);

  }



}
