package com.dutreras.gestion_versiones_producto.service.interfaces;

import com.dutreras.gestion_versiones_producto.dto.ProductoRequest;
import com.dutreras.gestion_versiones_producto.dto.ProductoResponse;

import java.util.List;
import java.util.UUID;

public interface ProductoService {
    ProductoResponse crear(ProductoRequest request);
    ProductoResponse obtenerPorId(UUID id);
    List<ProductoResponse> listarTodos();
    ProductoResponse actualizar(UUID id, ProductoRequest request);
    void eliminar(UUID id);
}
