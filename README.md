# Starter Tools Parent
Parent project for the various tools

## Requirements
*	JDK 11 or higher
*	Maven 3.6 or higher (build only)

## Modules
*	[Java Utilities](https://github.com/edtoktay/pardus-tools-starter-parent/blob/main/java-utilities)
*	[Multi-Tenant DataSource](https://github.com/edtoktay/pardus-tools-starter-parent/tree/main/spring-multi-tenant-datasource)
*	[JWT Security](https://github.com/edtoktay/pardus-tools-starter-parent/tree/main/spring-jwt-security)
*	[Rule Flow](https://github.com/edtoktay/pardus-tools-starter-parent/tree/main/spring-rule-flow-manager)

## Installation
```xml
<dependencyManagement>
	<dependencies>
		<dependency>
			<groupId>tech.pardus</groupId>
			<artifactId>pardus-tools-starter-parent</artifactId>
			<version>0.0.5</version>
			<type>pom</type>
			<scope>import</scope>
		</dependency>
	</dependencies>
</dependencyManagement>
```

## Building and Testing
### Build with Maven
```bash
$ mvn clean package
```

## License
This repository is distributed under a MIT license. See the provided [LICENSE](/LICENSE) file.
