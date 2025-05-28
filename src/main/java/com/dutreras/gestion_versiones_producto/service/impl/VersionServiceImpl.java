package com.dutreras.gestion_versiones_producto.service.impl;

import com.dutreras.gestion_versiones_producto.dto.VersionRequest;
import com.dutreras.gestion_versiones_producto.dto.VersionResponse;
import com.dutreras.gestion_versiones_producto.exception.FechaInvalidaException;
import com.dutreras.gestion_versiones_producto.mapper.VersionMapper;
import com.dutreras.gestion_versiones_producto.model.Producto;
import com.dutreras.gestion_versiones_producto.model.Version;
import com.dutreras.gestion_versiones_producto.repository.ProductoRepository;
import com.dutreras.gestion_versiones_producto.repository.VersionRepository;
import com.dutreras.gestion_versiones_producto.service.interfaces.VersionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VersionServiceImpl implements VersionService {

    private final VersionRepository versionRepository;
    private final ProductoRepository productoRepository;

    public VersionServiceImpl(VersionRepository versionRepository, ProductoRepository productoRepository) {
        this.versionRepository = versionRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public VersionResponse crear(UUID productoId, VersionRequest request) {
        Producto producto = productoRepository.findById(productoId)
            .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

        // ✅ Validación: fecha de lanzamiento no puede ser anterior a hoy
        if (request.getFechaLanzamiento().isBefore(LocalDate.now())) {
            throw new FechaInvalidaException("La fecha de lanzamiento no puede ser anterior a hoy");
        }

        Version version = new Version();
        version.setProducto(producto);
        VersionMapper.updateEntity(version, request);
        version = versionRepository.save(version);
        return VersionMapper.toResponse(version);
    }


    @Override
    public VersionResponse obtenerPorId(UUID versionId) {
        Version version = versionRepository.findById(versionId)
                .orElseThrow(() -> new EntityNotFoundException("Versión no encontrada"));
        return VersionMapper.toResponse(version);
    }

    @Override
    public List<VersionResponse> listarPorProducto(UUID productoId) {
        if (!productoRepository.existsById(productoId)) {
            throw new EntityNotFoundException("Producto no encontrado");
        }

        return versionRepository.findByProductoId(productoId).stream()
                .map(VersionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public VersionResponse actualizar(UUID versionId, VersionRequest request) {
        Version version = versionRepository.findById(versionId)
                .orElseThrow(() -> new EntityNotFoundException("Versión no encontrada"));
        VersionMapper.updateEntity(version, request);
        version = versionRepository.save(version);
        return VersionMapper.toResponse(version);
    }

    @Override
    public void eliminar(UUID versionId) {
        if (!versionRepository.existsById(versionId)) {
            throw new EntityNotFoundException("Versión no encontrada");
        }
        versionRepository.deleteById(versionId);
    }
}
