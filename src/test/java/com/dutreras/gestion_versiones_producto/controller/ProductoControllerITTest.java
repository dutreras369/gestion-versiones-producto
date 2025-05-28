package com.dutreras.gestion_versiones_producto.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class ProductoControllerITTest {

	@Container
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14").withDatabaseName("test_db")
			.withUsername("postgres").withPassword("postgres");

	@DynamicPropertySource
	static void overrideProps(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgres::getJdbcUrl);
		registry.add("spring.datasource.username", postgres::getUsername);
		registry.add("spring.datasource.password", postgres::getPassword);
	}

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private String obtenerToken() throws Exception {
		String loginRequest = """
				    {
				      "username": "admin",
				      "password": "admin123"
				    }
				""";

		String response = mockMvc
				.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(loginRequest))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		JsonNode jsonNode = objectMapper.readTree(response);
		return "Bearer " + jsonNode.get("token").asText();
	}

	@Test
	void flujoCompleto_crearProductoYVersion() throws Exception {
		String token = obtenerToken();

		// 1. Crear producto
		String productoRequest = """
				    {
				      "nombre": "MiProductoTest",
				      "descripcion": "Probando flujo completo"
				    }
				""";

		String productoResponse = mockMvc
				.perform(post("/api/v1/productos").header(HttpHeaders.AUTHORIZATION, token)
						.contentType(MediaType.APPLICATION_JSON).content(productoRequest))
				.andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

		JsonNode productoJson = objectMapper.readTree(productoResponse);
		String productoId = productoJson.get("id").asText();

		// 2. Crear versión asociada
		String versionRequest = """
				    {
				      "numeroVersion": "1.0.0",
				      "fechaLanzamiento": "%s",
				      "estado": "RELEASE",
				      "notas": "Primera versión estable"
				    }
				""".formatted(LocalDate.now().toString());

		mockMvc.perform(post("/api/v1/productos/" + productoId + "/versiones").header(HttpHeaders.AUTHORIZATION, token)
				.contentType(MediaType.APPLICATION_JSON).content(versionRequest)).andExpect(status().isCreated())
				.andExpect(jsonPath("$.numeroVersion").value("1.0.0")).andExpect(jsonPath("$.estado").value("RELEASE"));
	}

	@Test
	void crearProductoDuplicado_debeRetornar409() throws Exception {
		String token = obtenerToken();

		String productoDuplicado = """
				    {
				      "nombre": "ProductoDuplicado",
				      "descripcion": "Prueba de duplicado"
				    }
				""";

		// 1. Primera creación exitosa
		mockMvc.perform(post("/api/v1/productos").header(HttpHeaders.AUTHORIZATION, token)
				.contentType(MediaType.APPLICATION_JSON).content(productoDuplicado)).andExpect(status().isCreated());

		// 2. Intento duplicado
		mockMvc.perform(post("/api/v1/productos").header(HttpHeaders.AUTHORIZATION, token)
				.contentType(MediaType.APPLICATION_JSON).content(productoDuplicado)).andExpect(status().isConflict());
	}

	@Test
	void givenFechaLanzamientoInvalida_whenCrearVersion_thenRetorna400() throws Exception {
		String token = obtenerToken();

		// Crear producto válido
		String productoRequest = """
				    {
				      "nombre": "ProductoFechaInvalida",
				      "descripcion": "Test fecha inválida"
				    }
				""";

		String productoResponse = mockMvc
				.perform(post("/api/v1/productos").header("Authorization", token)
						.contentType(MediaType.APPLICATION_JSON).content(productoRequest))
				.andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

		String productoId = objectMapper.readTree(productoResponse).get("id").asText();

		// Crear versión con fecha inválida (ayer)
		String versionRequest = """
				    {
				      "numeroVersion": "0.9.0",
				      "fechaLanzamiento": "%s",
				      "estado": "ALFA",
				      "notas": "Versión con fecha inválida"
				    }
				""".formatted(LocalDate.now().minusDays(1));

		mockMvc.perform(post("/api/v1/productos/" + productoId + "/versiones").header("Authorization", token)
				.contentType(MediaType.APPLICATION_JSON).content(versionRequest)).andExpect(status().isBadRequest());
	}

	@Test
	void givenNumeroVersionInvalido_whenCrearVersion_thenRetorna400() throws Exception {
		String token = obtenerToken();

		// Crear producto válido
		String productoRequest = """
				    {
				      "nombre": "ProductoVersionInvalida",
				      "descripcion": "Test número versión inválido"
				    }
				""";

		String productoResponse = mockMvc
				.perform(post("/api/v1/productos").header("Authorization", token)
						.contentType(MediaType.APPLICATION_JSON).content(productoRequest))
				.andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

		String productoId = objectMapper.readTree(productoResponse).get("id").asText();

		// Crear versión con número inválido
		String versionRequest = """
				    {
				      "numeroVersion": "version1",
				      "fechaLanzamiento": "%s",
				      "estado": "BETA",
				      "notas": "Número versión inválido"
				    }
				""".formatted(LocalDate.now());

		mockMvc.perform(post("/api/v1/productos/" + productoId + "/versiones").header("Authorization", token)
				.contentType(MediaType.APPLICATION_JSON).content(versionRequest)).andExpect(status().isBadRequest());
	}
	@Test
	void obtenerProductoPorId_debeRetornar200() throws Exception {
		String token = obtenerToken();

		String productoRequest = """
				    {
				      "nombre": "ProductoGET",
				      "descripcion": "Descripción GET"
				    }
				""";

		String productoResponse = mockMvc.perform(post("/api/v1/productos")
				.header(HttpHeaders.AUTHORIZATION, token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(productoRequest))
			.andExpect(status().isCreated())
			.andReturn().getResponse().getContentAsString();

		String productoId = objectMapper.readTree(productoResponse).get("id").asText();

		mockMvc.perform(get("/api/v1/productos/" + productoId)
				.header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.nombre").value("ProductoGET"));
	}

	@Test
	void actualizarProducto_debeRetornar200() throws Exception {
		String token = obtenerToken();

		String productoRequest = """
				    {
				      "nombre": "ProductoUpdate",
				      "descripcion": "Original"
				    }
				""";

		String productoResponse = mockMvc.perform(post("/api/v1/productos")
				.header(HttpHeaders.AUTHORIZATION, token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(productoRequest))
			.andExpect(status().isCreated())
			.andReturn().getResponse().getContentAsString();

		String productoId = objectMapper.readTree(productoResponse).get("id").asText();

		String productoUpdate = """
				    {
				      "nombre": "ProductoUpdate",
				      "descripcion": "Actualizado"
				    }
				""";

		mockMvc.perform(put("/api/v1/productos/" + productoId)
				.header(HttpHeaders.AUTHORIZATION, token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(productoUpdate))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.descripcion").value("Actualizado"));
	}
	
	@Test
	void eliminarProducto_debeRetornar204() throws Exception {
		String token = obtenerToken();

		String productoRequest = """
				    {
				      "nombre": "ProductoDelete",
				      "descripcion": "Para eliminar"
				    }
				""";

		String productoResponse = mockMvc.perform(post("/api/v1/productos")
				.header(HttpHeaders.AUTHORIZATION, token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(productoRequest))
			.andExpect(status().isCreated())
			.andReturn().getResponse().getContentAsString();

		String productoId = objectMapper.readTree(productoResponse).get("id").asText();

		mockMvc.perform(delete("/api/v1/productos/" + productoId)
				.header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(status().isNoContent());
	}


}
