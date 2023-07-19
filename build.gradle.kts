plugins {
    java
    id("org.springframework.boot") version "3.1.1"
    id("io.spring.dependency-management") version "1.1.0"
}

group = "com.lukk"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-validation")


    testImplementation("org.springframework.boot:spring-boot-starter-test"){
        exclude("junit", "junit")
    }
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0-M1")

    compileOnly("org.projectlombok:lombok:1.18.28")
    compileOnly("org.springframework.boot:spring-boot-configuration-processor")

    annotationProcessor("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
