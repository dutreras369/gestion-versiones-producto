package com.dutreras.gestion_versiones_producto.mapper;

import com.dutreras.gestion_versiones_producto.dto.ProductoRequest;
import com.dutreras.gestion_versiones_producto.dto.ProductoResponse;
import com.dutreras.gestion_versiones_producto.model.Producto;

public class ProductoMapper {

    public static Producto toEntity(ProductoRequest request) {
        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        return producto;
    }

    public static ProductoResponse toResponse(Producto producto) {
        ProductoResponse response = new ProductoResponse();
        response.setId(producto.getId());
        response.setNombre(producto.getNombre());
        response.setDescripcion(producto.getDescripcion());
        return response;
    }
}
