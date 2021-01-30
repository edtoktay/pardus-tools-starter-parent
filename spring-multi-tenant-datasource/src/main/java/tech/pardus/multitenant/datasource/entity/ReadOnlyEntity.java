package tech.pardus.multitenant.datasource.entity;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

/**
 * Parent class for read only entities all CRUD operations throws error.
 *
 * @author deniz.toktay
 * @since Dec 30, 2020
 */
@MappedSuperclass
public class ReadOnlyEntity {

	@PrePersist
	public void onPrePersist() {
		throw new ReadOnlyEntityException("Save Not Permitted On This Table");
	}

	@PreUpdate
	public void onPreUpdate() {
		throw new ReadOnlyEntityException("Update Not Permitted On This Table");
	}

	@PreRemove
	public void onPreRemove() {
		throw new ReadOnlyEntityException("Delete Not Permitted On This Table");
	}

}
