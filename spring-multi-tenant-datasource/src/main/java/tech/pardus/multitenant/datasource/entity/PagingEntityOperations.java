/**
 *
 */
package tech.pardus.multitenant.datasource.entity;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * @author deniz.toktay
 * @since Dec 30, 2020
 */
public class PagingEntityOperations {

	public static PageRequest createPageRequest(int pageSize, int pageNumber, String sortDirection, String sortValue) {
		if (sortDirection.toLowerCase().equals(Sort.Direction.ASC.toString().toLowerCase())) {
			return PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, sortValue));
		} else {
			return PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, sortValue));
		}
	}

}
