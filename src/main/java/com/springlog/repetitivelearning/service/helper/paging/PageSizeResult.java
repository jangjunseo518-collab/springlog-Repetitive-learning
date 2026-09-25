package com.springlog.repetitivelearning.service.helper.paging;

public record PageSizeResult(
 int size,
 String message
) {
 public static PageSizeResult of(int size, String message) {
   PageSizeResult pageSizeResult = new PageSizeResult(size, message);
   return pageSizeResult;
 }
}
