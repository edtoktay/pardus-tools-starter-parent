/**
 *
 */
package tech.pardus.multitenant.datasource.test;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author edtoktay
 *
 */
public interface AuditedRepository extends JpaRepository<AuditedTestEntity, Long> {

	List<AuditedTestEntity> findByTestColumn(String testColumn);

}
