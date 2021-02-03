# Multi Tenant Datasource
Handle multiple datasoure connection in the project

## Requirements
*	JDK 11 or higher
*	Maven 3.6 or higher (build only)
*	spring-boot-dependencies 2.2.0.RELEASE

## Dependencies
*	spring-boot
*	spring-boot-autoconfigure
*	spring-boot-starter-data-jpa
*	spring-boot-starter-jdbc
*	lombok
*	commons-pool2
*	java-utilities

## Installation
```xml
<dependency>
	<groupId>tech.pardus</groupId>
	<artifactId>spring-multi-tenant-datasource</artifactId>
	<version>${pardus.tools.versions}</version>
</dependency>
  ```
Also requires relevant database jars as well.

## Usage
application.yml
```yml
multi-datasource:
  datasources:
    -
      id: maindb
      url: jdbc:sqlserver://localhost:1433;databaseName=MAINDB;
      username: sa
      password: password
      platform: org.hibernate.dialect.SQLServer2012Dialect 
      driver-class-name: com.microsoft.sqlserver.jdbc.SQLServerDriver
      hbm2dll-auto: update
      primary: true
      show-sql: true
      format-sql: true
    -
      id: ibmAs400
      url: jdbc:as400://localhost;libraries=as400Db;
      username: UNAME
      password: PASSWORD
      platform: org.hibernate.dialect.DB2400Dialect
      validation-query: 'SELECT current date FROM sysibm.sysdummy1'
      driver-class-name: com.ibm.as400.access.AS400JDBCDriver
    -
      id: oracleDb
      url: jdbc:oracle:thin:@Localhost:1521:TEST
      username: username
      password: password
      platform: org.hibernate.dialect.Oracle12cDialect
      driver-class-name: oracle.jdbc.OracleDriver
```
pom.xml
```xml
<dependencies>
	<!-- Sql Server Dependency -->
	<dependency>
		<groupId>com.microsoft.sqlserver</groupId>
		<artifactId>mssql-jdbc</artifactId>
	</dependency>
	<!-- IBM AS400 Dependency-->
	<dependency>
		<groupId>net.sf.jt400</groupId>
		<artifactId>jt400</artifactId>
		<version>6.6</version>
	</dependency>
	<!-- Oracle DB Dependency -->
	<dependency>
		<groupId>com.oracle</groupId>
		<artifactId>ojdbc7</artifactId>
		<version>12.1.0.2</version>
	</dependency>
</dependencies>
```

Connect Only Primary Datasource (All the entities and Repositories binded to primary datasource)
```java
@SpringBootApplication
@EnableMultiTenancy
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
```
Connect Multiple Datasource
```java
@SpringBootApplication
@EnableMultiTenancy(
		enable = true, 
		datasources = { 
				@DataSource(id = "maindb"), // Optional in all cases primary db will be added 
				@DataSource(id = "ibmAs400"), 
				@DataSource(id = "oracleDb") 
	})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
```
Entities
```java
// ID of the datasource given in the application file
@DataSourceSelection(datasource = "maindb") //Optional for primary datasource missing annotations will be handled in primary database
@Entity
@Table(name = "TestTable")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MainDbEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "Name", nullable = false, unique = true)
	private String name;
}
```
```java
// ID of the datasource given in the application file
@DataSourceSelection(datasource = "ibmAs400")
@Entity
@Table(name = "TestTable", schema = "TestSchema")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class As400DbEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "Name", nullable = false, unique = true)
	private String name;
}
```
```java
// ID of the datasource given in the application file
@DataSourceSelection(datasource = "oracleDb") 
@Entity
@Table(name = "TEST_TABLE")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OracleDbEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "Name", nullable = false, unique = true)
	private String name;
}
```
All defined spring repositories will be work with relevantly marked database.

### ReadOnly Entity
Throws exception if crud operations performed.
```java
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

public interface ReadOnlyRepository extends JpaRepository<ReadOnlyTestEntity, Long> {
}

ReadOnlyTestEntity entity = new ReadOnlyTestEntity();
entity.setTestColumn("Test Column");
readOnlyRepository.save(entity); // Throws Exception
```
In case of any operation rather than select throws exception.

### Audited Entity
Abstract class contains Id, Created Date User so on. 
**tech.pardus.utilities.SessionUserContextHolder** should require to set in order to set relevant username(s) missing username setted as "SYSTEM". (In Jwt Secure module **tech.pardus.utilities.SessionUserContextHolder** populated from JWT Token)
```java
@Entity
@Table(name = "AuditedTable")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuditedTestEntity extends AuditedBaseEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "TestColumn")
	private String testColumn;

}

public interface AuditedRepository extends JpaRepository<AuditedTestEntity, Long> {

	List<AuditedTestEntity> findByTestColumn(String testColumn);

}

SessionUserContextHolder.setCurrentSessionUser("TEST USER");
AuditedTestEntity entity = new AuditedTestEntity();
entity.setTestColumn("Test Audited");
auditedRepository.save(entity);
```

### String Encrypyed Column
```java
@Entity
@Table(name = "TestTable")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EncTestEntity extends AuditedBaseEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "TestColumn")
	@Convert(converter = StringEncryptionConverter.class) //  Enables Encryption
	private String testColumn;

}

public interface EncRepository extends JpaRepository<EncTestEntity, Long> {

	List<EncTestEntity> findByTestColumn(String testColumn);

}

var entity = new EncTestEntity();
entity.setTestColumn("Test Audited");
encRepository.save(entity);
var lst = encRepository.findByTestColumn("Test Audited");

// Setting AES KEY should be 16, 24 or 32 bytes
EncryptionKeyHolder.setEncryptionKey(encryptionKey);

// Configuration of the AES key from application.yml
@Configuration
public class KeyConfig {

	@Value("${encryption-key}")
	@Getter
	@Setter
	private String encryptionKey;

	@Bean
	public String keyHolder() {
		EncryptionKeyHolder.setEncryptionKey(encryptionKey);
		return "OK";
	}

}
```
  
