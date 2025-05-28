package com.dutreras.gestion_versiones_producto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Entity
@Table(name = "productos", uniqueConstraints = {
        @UniqueConstraint(columnNames = "nombre")
})
public class Producto {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 60)
    @NotBlank
    @Size(min = 3, max = 60)
    private String nombre;

    @Column(length = 255)
    @Size(max = 255)
    private String descripcion;

    // Getters y setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre != null ? nombre.trim().toLowerCase() : null;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
