/**
 * 
 */
package tech.pardus.multitenant.datasource.test;

import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @author edtoktay
 *
 */
public interface ReadOnyRepository extends JpaRepository<ReadOnlyTestEntity, Long> {
}
