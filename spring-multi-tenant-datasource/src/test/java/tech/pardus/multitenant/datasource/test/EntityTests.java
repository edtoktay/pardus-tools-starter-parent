/**
 *
 */
package tech.pardus.multitenant.datasource.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.TransactionSystemException;

import tech.pardus.multitenant.datasource.entity.ReadOnlyEntityException;
import tech.pardus.utilities.SessionUserContextHolder;

/**
 * @author edtoktay
 *
 */
@SpringBootTest(classes = { App.class })
@Sql({ "/data.sql" })
class EntityTests {

	@Autowired
	private ReadOnyRepository readOnyRepository;

	@Autowired
	private AuditedRepository auditedRepository;

	@Autowired
	private DataSource dataSource;

	@Test
	void test_readOnly_save() {
		var entity = new ReadOnlyTestEntity();
		entity.setTestColumn("Test Column");
		var exception = assertThrows(ReadOnlyEntityException.class, () -> readOnyRepository.save(entity));
		assertEquals("Save Not Permitted On This Table", exception.getMessage());
	}

	@Test
	void test_readOnly_update() {
		var entity = new ReadOnlyTestEntity();
		entity.setId(1L);
		entity.setTestColumn("Test Columnz");
		var exception = assertThrows(TransactionSystemException.class, () -> readOnyRepository.save(entity));
		assertEquals(
		        "Could not commit JPA transaction; nested exception is javax.persistence.RollbackException: Error while committing the transaction",
		        exception.getMessage());
	}

	@Test
	void test_readOnly_delete() {
		var entity = new ReadOnlyTestEntity();
		entity.setId(1L);
		var exception = assertThrows(ReadOnlyEntityException.class, () -> readOnyRepository.delete(entity));
		assertEquals("Delete Not Permitted On This Table", exception.getMessage());
	}

	@Test
	void test_audited_insert() {
		SessionUserContextHolder.setCurrentSessionUser("TEST USER");
		var entity = new AuditedTestEntity();
		entity.setTestColumn("Test Audited");
		auditedRepository.save(entity);
		var lst = auditedRepository.findByTestColumn("Test Audited");
		assertTrue(lst.size() > 0);
		assertEquals("TEST USER", lst.get(0).getCreatedBy());
		assertEquals("Test Audited", lst.get(0).getTestColumn());
	}

	@Test
	void test_encrypted_column() throws SQLException {
		SessionUserContextHolder.setCurrentSessionUser("TEST USER");
		var entity = new AuditedTestEntity();
		entity.setTestColumn("Test Audited");
		auditedRepository.save(entity);
		var prepstatement = dataSource.getConnection().prepareStatement("select * from AuditedTable");
		var rs = prepstatement.executeQuery();
		while (rs.next()) {
			assertNotEquals("Test Audited", rs.getString("TestColumn"));
		}
	}

}
