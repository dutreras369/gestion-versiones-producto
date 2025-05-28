package com.dutreras.gestion_versiones_producto.controller;

import com.dutreras.gestion_versiones_producto.dto.ProductoRequest;
import com.dutreras.gestion_versiones_producto.dto.ProductoResponse;
import com.dutreras.gestion_versiones_producto.service.interfaces.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@RequestBody @Valid ProductoRequest request) {
        ProductoResponse response = productoService.crear(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public List<ProductoResponse> listar() {
        return productoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ProductoResponse obtener(@PathVariable UUID id) {
        return productoService.obtenerPorId(id);
    }

    @PutMapping("/{id}")
    public ProductoResponse actualizar(@PathVariable UUID id, @RequestBody @Valid ProductoRequest request) {
        return productoService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable UUID id) {
        productoService.eliminar(id);
    }
}
