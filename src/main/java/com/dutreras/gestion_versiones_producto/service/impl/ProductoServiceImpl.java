package com.dutreras.gestion_versiones_producto.service.impl;

import com.dutreras.gestion_versiones_producto.dto.ProductoRequest;
import com.dutreras.gestion_versiones_producto.dto.ProductoResponse;
import com.dutreras.gestion_versiones_producto.mapper.ProductoMapper;
import com.dutreras.gestion_versiones_producto.model.Producto;
import com.dutreras.gestion_versiones_producto.repository.ProductoRepository;
import com.dutreras.gestion_versiones_producto.service.interfaces.ProductoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public ProductoResponse crear(ProductoRequest request) {
        if (productoRepository.findByNombreIgnoreCase(request.getNombre()).isPresent()) {
            throw new DataIntegrityViolationException("Nombre de producto duplicado");
        }

        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto = productoRepository.save(producto); // esto se ejecuta solo si no es duplicado

        return ProductoMapper.toResponse(producto);
    }

    @Override
    public ProductoResponse obtenerPorId(UUID id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
        return ProductoMapper.toResponse(producto);
    }

    @Override
    public List<ProductoResponse> listarTodos() {
        return productoRepository.findAll().stream()
                .map(ProductoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductoResponse actualizar(UUID id, ProductoRequest request) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto = productoRepository.save(producto);
        return ProductoMapper.toResponse(producto);
    }

    @Override
    public void eliminar(UUID id) {
        if (!productoRepository.existsById(id)) {
            throw new EntityNotFoundException("Producto no encontrado");
        }
        productoRepository.deleteById(id);
    }
}
