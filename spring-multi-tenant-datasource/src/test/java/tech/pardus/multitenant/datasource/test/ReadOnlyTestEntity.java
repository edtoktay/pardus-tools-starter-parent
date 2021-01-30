/**
 *
 */
package tech.pardus.multitenant.datasource.test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.pardus.multitenant.datasource.entity.ReadOnlyEntity;

/**
 * @author edtoktay
 *
 */
@Entity
@Table(name = "ReadOnlyTable")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReadOnlyTestEntity extends ReadOnlyEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "TestColumn")
	private String testColumn;

}
