package tech.pardus.multitenant.datasource.entity;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * Create Page Request for given parameters.
 *
 * @author deniz.toktay
 * @since Dec 30, 2020
 */
public class PagingEntityOperations {

  /**
   * org.springframework.data.domain.PageRequest creation.
   *
   * @param pageSize size of the page
   * @param pageNumber number of the page
   * @param sortDirection sort direction default DESC
   * @param sortValue sort value default is id
   * @return PageRequest for JPA Repositories
   */
  public static PageRequest createPageRequest(
      int pageSize, int pageNumber, String sortDirection, String sortValue) {
    if (sortDirection.toLowerCase().equals(Sort.Direction.ASC.toString().toLowerCase())) {
      return PageRequest.of(
          pageNumber,
          pageSize,
          Sort.by(Sort.Direction.ASC, StringUtils.isBlank(sortValue) ? "id" : sortValue));
    } else {
      return PageRequest.of(
          pageNumber,
          pageSize,
          Sort.by(Sort.Direction.DESC, StringUtils.isBlank(sortValue) ? "id" : sortValue));
    }
  }
}
