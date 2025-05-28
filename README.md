# Gestión de Versiones de Producto

Microservicio desarrollado en Java 17 + Spring Boot 3 para gestionar productos y sus versiones. Cumple con las especificaciones de seguridad, documentación, pruebas y contenedorización.

---

## 🚀 Tecnologías utilizadas

- Java 17
- Spring Boot 3
- Spring Data JPA
- PostgreSQL
- Spring Security (JWT)
- Springdoc OpenAPI (Swagger)
- Hibernate Validator
- Testcontainers + JUnit 5
- Maven
- Docker

---

## 🧪 Requisitos previos

- Java 17
- Maven
- Docker (opcional para contenedor de PostgreSQL)
- Spring Tool Suite (STS) o IDE similar

---

## ⚙️ Configuración de base de datos

El microservicio se conecta a una base PostgreSQL local usando:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/productos_db
spring.datasource.username=postgres
spring.datasource.password=${DB_PASSWORD:changeme}

```


> Asegúrate de que la base de datos `productos_db` exista. Puedes usar pgAdmin o psql para crearla.

---

## 🧑‍💻 Ejecución local

```
mvn spring-boot:run
```
O bien, si estás usando .env directamente en terminal:


```
DB_PASSWORD=tu_contraseña_de_postgres mvn spring-boot:run

```

##  Usuario Generico

```
{
  "username": "admin",
  "password": "admin123"
}

```
## 🧪 Pruebas
Para ejecutar las pruebas unitarias e integración:

```
mvn test

```

El reporte de cobertura JaCoCo se genera en:

```
target/site/jacoco/index.html
```


## 📄 Documentación de API Y Respaldo
- Puedes acceder desde la aplicación en ejecución a traves de Swagger
- Tambien existen los respaldos en la carpeta respaldos

```
http://localhost:8081/swagger-ui.html
```

## 🐳 Docker (opcional)

Una vez finalizado el desarrollo local, puedes crear una imagen Docker:

```
docker build -t gestion-versiones-producto .
```

Y correrla junto con PostgreSQL usando docker-compose.yml.



## 🩺 Observabilidad
El endpoint /actuator/health está disponible para verificación de estado:

```
http://localhost:8081/actuator/health
```

## 📁 Estructura del proyecto

```
com.dutreras.gestion_versiones_producto
├── core
├── controller
├── dto
├── exception
├── mapper
├── model
├── repository
├── security
├── service
│   ├── interfaces
│   └── impl
└── util
└── respaldos


```

## 🕒 Tiempo estimado
Este proyecto está diseñado para desarrollarse en aproximadamente 8 horas efectivas.

## 🧑 Autor
Desarrollado por: [Dutreras](https://github.com/dutreras369)
