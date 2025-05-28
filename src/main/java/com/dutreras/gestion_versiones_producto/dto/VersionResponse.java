package com.dutreras.gestion_versiones_producto.dto;

import com.dutreras.gestion_versiones_producto.model.EstadoVersion;

import java.time.LocalDate;
import java.util.UUID;

public class VersionResponse {

    private UUID id;
    private String numeroVersion;
    private LocalDate fechaLanzamiento;
    private EstadoVersion estado;
    private String notas;

    // Getters y setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNumeroVersion() {
        return numeroVersion;
    }

    public void setNumeroVersion(String numeroVersion) {
        this.numeroVersion = numeroVersion;
    }

    public LocalDate getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public void setFechaLanzamiento(LocalDate fechaLanzamiento) {
        this.fechaLanzamiento = fechaLanzamiento;
    }

    public EstadoVersion getEstado() {
        return estado;
    }

    public void setEstado(EstadoVersion estado) {
        this.estado = estado;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }
}
