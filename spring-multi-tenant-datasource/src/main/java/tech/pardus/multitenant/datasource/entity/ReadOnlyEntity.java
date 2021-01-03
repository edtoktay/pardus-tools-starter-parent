/**
 *
 */
package tech.pardus.multitenant.datasource.entity;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

/**
 * @author deniz.toktay
 * @since Dec 30, 2020
 */
public class ReadOnlyEntity {

	@PrePersist
	void onPrePersist(Object o) {
		throw new RuntimeException("Save Not Permitted On This Table");
	}

	@PreUpdate
	void onPreUpdate(Object o) {
		throw new RuntimeException("Update Not Permitted On This Table");
	}

	@PreRemove
	void onPreRemove(Object o) {
		throw new RuntimeException("Delete Not Permitted On This Table");
	}

}
