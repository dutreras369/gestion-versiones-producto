package com.dutreras.gestion_versiones_producto.service.interfaces;

import com.dutreras.gestion_versiones_producto.dto.VersionRequest;
import com.dutreras.gestion_versiones_producto.dto.VersionResponse;

import java.util.List;
import java.util.UUID;

public interface VersionService {
    VersionResponse crear(UUID productoId, VersionRequest request);
    VersionResponse obtenerPorId(UUID versionId);
    List<VersionResponse> listarPorProducto(UUID productoId);
    VersionResponse actualizar(UUID versionId, VersionRequest request);
    void eliminar(UUID versionId);
}
