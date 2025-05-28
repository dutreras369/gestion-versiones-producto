package com.dutreras.gestion_versiones_producto.controller;

import com.dutreras.gestion_versiones_producto.dto.VersionRequest;
import com.dutreras.gestion_versiones_producto.dto.VersionResponse;
import com.dutreras.gestion_versiones_producto.service.interfaces.VersionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class VersionController {

    private final VersionService versionService;

    public VersionController(VersionService versionService) {
        this.versionService = versionService;
    }

    @PostMapping("/api/v1/productos/{productoId}/versiones")
    public ResponseEntity<VersionResponse> crear(@PathVariable UUID productoId,
                                                 @RequestBody @Valid VersionRequest request) {
        VersionResponse response = versionService.crear(productoId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/api/v1/productos/{productoId}/versiones")
    public List<VersionResponse> listarPorProducto(@PathVariable UUID productoId) {
        return versionService.listarPorProducto(productoId);
    }

    @GetMapping("/api/v1/versiones/{id}")
    public VersionResponse obtener(@PathVariable UUID id) {
        return versionService.obtenerPorId(id);
    }

    @PutMapping("/api/v1/versiones/{id}")
    public VersionResponse actualizar(@PathVariable UUID id,
                                      @RequestBody @Valid VersionRequest request) {
        return versionService.actualizar(id, request);
    }

    @DeleteMapping("/api/v1/versiones/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable UUID id) {
        versionService.eliminar(id);
    }
}
