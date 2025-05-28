package com.dutreras.gestion_versiones_producto.dto;

import com.dutreras.gestion_versiones_producto.model.EstadoVersion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public class VersionRequest {

    @NotBlank
    @Pattern(regexp = "^(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)$", message = "Debe cumplir formato SemVer (X.Y.Z)")
    private String numeroVersion;

    @NotNull
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate fechaLanzamiento;

    @NotNull
    private EstadoVersion estado;

    private String notas;

    // Getters y setters

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
