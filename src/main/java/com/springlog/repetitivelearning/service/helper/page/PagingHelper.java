package com.springlog.repetitivelearning.service.helper.page;

import static com.springlog.repetitivelearning.service.helper.VisibiltyValidator.publicVisibilityValidator;
import static com.springlog.repetitivelearning.service.helper.page.PageSizeValidator.pageClamp;

import com.springlog.repetitivelearning.domain.type.Visibility;
import com.springlog.repetitivelearning.dto.request.PagingRequest;
import com.springlog.repetitivelearning.exception.NotValidPageException;
import com.springlog.repetitivelearning.exception.NotValidSortFieldException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PagingHelper {

  public static PagingSetup buildPagingSetup(PagingRequest pagingRequest) {

    Visibility publicVisibility = publicVisibilityValidator(pagingRequest.visibility());
    PageSizeResult validPageSize = pageClamp(pagingRequest.pageSize());
    String sort = pagingRequest.sort();
    int page = pagingRequest.page() == null ? 0 : pagingRequest.page();
    Sort.Direction direction = pagingRequest.direction() == null
                                          ?Sort.Direction.DESC
                                          :pagingRequest.direction();

    String sortField = switch (sort == null? "id":sort){
      case "id"-> "id";
      case "minutes" -> "minutes";
      case "title" -> "title";
      default -> throw new NotValidSortFieldException(sort);
    };

    if(page < 0 ) {
      throw new NotValidPageException(page);
    }

    Sort validSort = Sort.by(direction, sortField);

    Pageable pageable = PageRequest.of(page, validPageSize.pageSize(), validSort);

    return PagingSetup.of(publicVisibility, validPageSize,  pageable );
  }

}
