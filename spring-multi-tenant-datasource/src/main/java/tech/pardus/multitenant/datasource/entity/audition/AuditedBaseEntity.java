/**
 *
 */
package tech.pardus.multitenant.datasource.entity.audition;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author deniz.toktay
 * @since Dec 29, 2020
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuditedBaseEntity implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@CreatedBy
	@Column(name = "CREATED_USER", updatable = false)
	private String createdBy;

	@CreatedDate
	@Column(name = "CREATED_DATE", updatable = false)
	private LocalDateTime createdDate;

	@LastModifiedBy
	@Column(name = "UPDATED_BY")
	private String modifiedBy;

	@LastModifiedDate
	@Column(name = "UPDATED_DATE")
	private LocalDateTime modifiedDate;

	@Column(name = "DELETED")
	private Boolean deleted = Boolean.FALSE;

	@Column(name = "DELETED_DATE")
	private LocalDateTime deleteDate;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (createdBy == null ? 0 : createdBy.hashCode());
		result = prime * result + (createdDate == null ? 0 : createdDate.hashCode());
		result = prime * result + (deleteDate == null ? 0 : deleteDate.hashCode());
		result = prime * result + (deleted == null ? 0 : deleted.hashCode());
		result = prime * result + (id == null ? 0 : id.hashCode());
		result = prime * result + (modifiedBy == null ? 0 : modifiedBy.hashCode());
		result = prime * result + (modifiedDate == null ? 0 : modifiedDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AuditedBaseEntity other = (AuditedBaseEntity) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

}
