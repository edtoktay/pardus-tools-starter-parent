/**
 *
 */
package tech.pardus.multitenant.datasource.test;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.pardus.multitenant.datasource.entity.audition.AuditedBaseEntity;
import tech.pardus.multitenant.datasource.entity.encryption.StringEncryptionConverter;

/**
 * @author edtoktay
 *
 */
@Entity
@Table(name = "AuditedTable")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuditedTestEntity extends AuditedBaseEntity {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "TestColumn")
	@Convert(converter = StringEncryptionConverter.class)
	private String testColumn;

}
