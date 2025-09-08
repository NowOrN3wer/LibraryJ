# 📚 Java 21 Teknoloji Demo Projesi (Java / Spring Boot)

Bu proje, **Java 21** ve **Spring Boot 3.5.5** üzerinde yeni özellikleri ve modern yazılım geliştirme pratiklerini denemek amacıyla hazırlanmış bir teknoloji demosudur.  
Hem halihazırda öğrendiğim teknikleri uygulamak hem de ileride eklemeyi planladığım teknolojiler için bir test ortamı oluşturmak hedeflenmiştir.  
Ayrıca proje, gelecekte **mikro servis mimarisi** üzerine yapılacak çalışmalar için temel bir yapı sunar.

---

## 🚀 Mevcut Teknolojiler

- **Spring Boot Web** – REST API oluşturma  
- **Spring Security** – OAuth2 Resource Server ile güvenlik  
- **Spring Data JPA** – Veri erişim katmanı  
- **Flyway** – Veritabanı versiyonlama  
- **PostgreSQL** – Veritabanı yönetimi  
- **QueryDSL (JPA)** – Tip güvenli sorgular  
- **MapStruct** – DTO <-> Entity dönüşümleri  
- **Lombok** – Kod sadeleştirme  
- **SpringDoc OpenAPI** – Swagger dokümantasyonu  
- **JJWT** – JWT işlemleri  
- **Validation** – Spring Boot doğrulama anotasyonları  
- **JUnit 5 & Testcontainers** – Entegre test ortamı  

---

## 🛠️ Planlanan ve Denemek İstediğim Teknolojiler

- **Apache Kafka**  
- **RabbitMQ**  
- **MongoDB**  
- **Elastic APM**  
- **Full-Text Search (PostgreSQL & ElasticSearch)**  
- **Mikro servis mimarisi uygulamaları (Spring Cloud / Kubernetes)**  

---

## 🗂️ Proje Yapısı (Clean Architecture)

Proje, **Clean Architecture** prensiplerine göre aşağıdaki katmanlardan oluşur:

- **Domain** – İş kuralları, entity tanımları  
- **Application** – Use-case’ler, servis tanımları  
- **Infrastructure** – Veritabanı, dış servis adapter'ları  
- **Web / API** – Controller'lar, request/response modelleri  

---

## 🔧 Build ve Konfigürasyon (Gradle – Java 21)

```groovy
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.5.5'
    id 'io.spring.dependency-management' version '1.1.7'
}

group = 'com.idb.microservice-demo'
version = '0.0.1-SNAPSHOT'
description = 'Library'

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // === Spring & Altyapı ===
    implementation 'org.springframework.boot:spring-boot-starter-actuator'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-oauth2-resource-server'
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.12'
    implementation 'org.flywaydb:flyway-core'
    implementation 'org.flywaydb:flyway-database-postgresql'
    implementation 'io.jsonwebtoken:jjwt-api:0.12.6'

    runtimeOnly 'org.postgresql:postgresql'
    runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.12.6'
    runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.12.6'

    // === Lombok ===
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
    annotationProcessor 'org.springframework.boot:spring-boot-configuration-processor'

    // === MapStruct ===
    implementation 'org.mapstruct:mapstruct:1.6.2'
    annotationProcessor 'org.mapstruct:mapstruct-processor:1.6.2'
    annotationProcessor 'org.projectlombok:lombok-mapstruct-binding:0.2.0'

    // === QueryDSL (JPA/Jakarta) ===
    implementation 'com.querydsl:querydsl-jpa:5.1.0:jakarta'
    annotationProcessor 'com.querydsl:querydsl-apt:5.1.0:jakarta'
    annotationProcessor 'jakarta.persistence:jakarta.persistence-api:3.1.0'
    annotationProcessor 'jakarta.annotation:jakarta.annotation-api:2.1.1'

    // === Test Altyapısı ===
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'org.springframework.boot:spring-boot-testcontainers'
    testImplementation 'org.springframework.security:spring-security-test'
    testImplementation 'org.testcontainers:junit-jupiter'
    testImplementation 'org.testcontainers:postgresql'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'

    testAnnotationProcessor 'org.mapstruct:mapstruct-processor:1.6.2'
    testAnnotationProcessor 'org.projectlombok:lombok'
    testAnnotationProcessor 'org.projectlombok:lombok-mapstruct-binding:0.2.0'
    testAnnotationProcessor 'com.querydsl:querydsl-apt:5.1.0:jakarta'
    testAnnotationProcessor 'jakarta.persistence:jakarta.persistence-api:3.1.0'
    testAnnotationProcessor 'jakarta.annotation:jakarta.annotation-api:2.1.1'
}

tasks.named('test') {
    useJUnitPlatform()
}
