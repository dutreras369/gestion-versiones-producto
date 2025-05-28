package com.dutreras.gestion_versiones_producto.mapper;

import com.dutreras.gestion_versiones_producto.dto.VersionRequest;
import com.dutreras.gestion_versiones_producto.dto.VersionResponse;
import com.dutreras.gestion_versiones_producto.model.Version;

public class VersionMapper {

    public static VersionResponse toResponse(Version version) {
        VersionResponse dto = new VersionResponse();
        dto.setId(version.getId());
        dto.setNumeroVersion(version.getNumeroVersion());
        dto.setFechaLanzamiento(version.getFechaLanzamiento());
        dto.setEstado(version.getEstado());
        dto.setNotas(version.getNotas());
        return dto;
    }

    public static void updateEntity(Version version, VersionRequest request) {
        version.setNumeroVersion(request.getNumeroVersion());
        version.setFechaLanzamiento(request.getFechaLanzamiento());
        version.setEstado(request.getEstado());
        version.setNotas(request.getNotas());
    }
}
