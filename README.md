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

## 🔐 Configuración del entorno

### 1. Variables de entorno

Para mantener la configuración sensible fuera del código, crea un archivo `.env` en la raíz del proyecto:



```
DB_PASSWORD=tu_contraseña_de_postgres

```


> Este archivo está en `.gitignore` y no debe subirse al repositorio.

### 2. Configuración en Spring Tool Suite (STS)

Si corres el proyecto desde STS:

1. Haz clic derecho sobre el proyecto → `Run As` → `Run Configurations`
2. Selecciona tu aplicación en `Java Application`
3. Ve a la pestaña **Environment**
4. Agrega una variable:
   - **Name**: `DB_PASSWORD`
   - **Value**: `tu_contraseña_de_postgres`

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


## 🧪 Pruebas
Para ejecutar las pruebas unitarias e integración:

```
mvn test

```

El reporte de cobertura JaCoCo se genera en:

```
target/site/jacoco/index.html
```


## 📄 Documentación de API
Cuando la aplicación esté en ejecución, accede a la documentación Swagger:

```
http://localhost:8080/swagger-ui.html
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
http://localhost:8080/actuator/health
```

## 📁 Estructura del proyecto

```
com.dutreras.gestion_versiones_producto
├── config
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

```

## 🕒 Tiempo estimado
Este proyecto está diseñado para desarrollarse en aproximadamente 8 horas efectivas.

## 🧑 Autor
Desarrollado por: [Dutreras](https://github.com/dutreras369)
