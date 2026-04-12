# CITRUS Delivery Record

- Change set: `java-21-config`
- Goal: switch the project build configuration from Java 17 to Java 21 and make the requirement explicit

## Scope

- `pom.xml`
  - set `java.version` to `21`
  - add Maven Enforcer rule to require JDK 21+

## Validation

- `./mvnw.cmd -version`
- `./mvnw.cmd clean test`

## Outcome

- Maven wrapper runs on JDK 21
- project compiles and tests pass with Java 21 target level
- build now fails early with a clear message if started on an older JDK
