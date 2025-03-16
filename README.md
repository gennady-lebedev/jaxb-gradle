# jaxb-gradle

With great respect to original solution https://stackoverflow.com/a/66775613 

Complete runnable example of JAXB on Gradle+Kotlin:
- ./schemas/*.xsd files converts to ./build/generated/sources/xjc/main/jaxb/gradle/jaxb/*.java files
- using ./schemas/bindings.xjb with custom adapter from ./src/main/java/jaxb/gradle/adapter/TimestampAdapter.java

Try `./gradlew clean test`