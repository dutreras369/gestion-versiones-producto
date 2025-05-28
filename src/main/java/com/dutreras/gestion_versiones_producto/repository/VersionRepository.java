package com.dutreras.gestion_versiones_producto.repository;

import com.dutreras.gestion_versiones_producto.model.Version;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VersionRepository extends JpaRepository<Version, UUID> {
    List<Version> findByProductoId(UUID productoId);
}
