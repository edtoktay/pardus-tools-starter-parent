# Java Utilities
Collection of the Java utility functions.

## Requirements
*	JDK 11 or higher
*	Maven 3.6 or higher (build only)

## Dependencies
*	commons-lang3.3-9.jar
*	commons-collections4-4.4.jar
*	slf4j-api-1.7.30.jar
*	reflections-0.9.11.jar
*	junit-jupiter-engine-5.6.2.jar

## Installation
```xml
<dependency>
	<groupId>tech.pardus</groupId>
	<artifactId>java-utilities</artifactId>
	<version>${pardus.tools.versions}</version>
</dependency>
  ```

## Usage
###	PAsserts
Basic assert controls with RuntimeException throw with given message. 
#### AssertException with no message
```java
var control = false;
PAsserts.isTrue(control);
```
#### AssertException with Message
```java
var control = false;
PAsserts.isTrue(control, () -> "Control Assertion is Failed");
```
#### Custom Runtime Exception
```java
public class TestException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public TesTexception(String message) {
		super(message);
	}
}
var control = false;
PAsserts.isTrue(control, () -> "Control Assertion is Failed", () -> TestException.class);
```

### LambdaWrapper
Wraps the Lambda operations for exception try catch. And terminates in exception by throwing the exception.
```java
private static int divider = -1;

// With LambdaWarapper stops in exception
public void test() {
	List<Integer> lst = Arrays.asList(1, 2, 3, 4);
	var result = lst.stream().map(LambdaWrapper.functionChecker(t -> division(t))).collect(Collectors.toList());
}

// Without LambdaWarapper continues in exception
public void test2() {
	List<Integer> lst = Arrays.asList(1, 2, 3, 4);
	var result = lst.stream().map(t -> {
		try {
			return division(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}).collect(Collectors.toList());
	System.out.println(result);
}

public int division(int a) throws Exception {
	divider += 1;
	return a / divider;
}
```

### ReflectionUtils
Various reflection operations such as run methods, searching and getting all the classes annotated with an annotation so on.
```java
Set<Class<? extends AnyInterface>> dispatcherSet =
          ReflectionUtils.listOfExtendedClasses(AnyInterface.class);
```