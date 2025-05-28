package com.dutreras.gestion_versiones_producto.service.impl;

import com.dutreras.gestion_versiones_producto.dto.VersionRequest;
import com.dutreras.gestion_versiones_producto.dto.VersionResponse;
import com.dutreras.gestion_versiones_producto.exception.FechaInvalidaException;
import com.dutreras.gestion_versiones_producto.mapper.VersionMapper;
import com.dutreras.gestion_versiones_producto.model.EstadoVersion;
import com.dutreras.gestion_versiones_producto.model.Producto;
import com.dutreras.gestion_versiones_producto.model.Version;
import com.dutreras.gestion_versiones_producto.repository.ProductoRepository;
import com.dutreras.gestion_versiones_producto.repository.VersionRepository;
import com.dutreras.gestion_versiones_producto.service.impl.VersionServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VersionServiceImplTest {

    @Mock
    private VersionRepository versionRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private VersionServiceImpl versionService;

    @Test
    void givenFechaLanzamientoAnterior_whenCrear_thenThrowException() {
        UUID productoId = UUID.randomUUID();
        VersionRequest request = new VersionRequest();
        request.setFechaLanzamiento(LocalDate.now().minusDays(1));
        request.setNumeroVersion("1.0.0");
        request.setEstado(EstadoVersion.RELEASE);

        when(productoRepository.findById(productoId)).thenReturn(Optional.of(new Producto()));

        FechaInvalidaException ex = assertThrows(FechaInvalidaException.class, () ->
                versionService.crear(productoId, request));

        assertEquals("La fecha de lanzamiento no puede ser anterior a hoy", ex.getMessage());
    }

    @Test
    void givenProductoValido_whenCrearVersion_thenSuccess() {
        UUID productoId = UUID.randomUUID();
        VersionRequest request = new VersionRequest();
        request.setNumeroVersion("1.2.3");
        request.setFechaLanzamiento(LocalDate.now());
        request.setEstado(EstadoVersion.BETA);

        Producto producto = new Producto();
        producto.setId(productoId);

        Version version = new Version();
        version.setId(UUID.randomUUID());
        version.setNumeroVersion("1.2.3");
        version.setFechaLanzamiento(LocalDate.now());
        version.setEstado(EstadoVersion.BETA);
        version.setProducto(producto);

        when(productoRepository.findById(productoId)).thenReturn(Optional.of(producto));
        when(versionRepository.save(any())).thenReturn(version);

        VersionResponse response = versionService.crear(productoId, request);

        assertEquals("1.2.3", response.getNumeroVersion());
        assertEquals(EstadoVersion.BETA, response.getEstado());
    }

    @Test
    void givenVersionExistente_whenObtenerPorId_thenSuccess() {
        UUID id = UUID.randomUUID();
        Version version = new Version();
        version.setId(id);
        version.setNumeroVersion("1.0.0");

        when(versionRepository.findById(id)).thenReturn(Optional.of(version));

        VersionResponse response = versionService.obtenerPorId(id);

        assertEquals("1.0.0", response.getNumeroVersion());
    }

    @Test
    void givenVersionInexistente_whenObtenerPorId_thenThrow() {
        UUID id = UUID.randomUUID();
        when(versionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> versionService.obtenerPorId(id));
    }

    @Test
    void givenProductoExistente_whenListarPorProducto_thenSuccess() {
        UUID productoId = UUID.randomUUID();
        Version v1 = new Version();
        v1.setNumeroVersion("0.1.0");

        Version v2 = new Version();
        v2.setNumeroVersion("0.2.0");

        when(productoRepository.existsById(productoId)).thenReturn(true);
        when(versionRepository.findByProductoId(productoId)).thenReturn(List.of(v1, v2));

        List<VersionResponse> lista = versionService.listarPorProducto(productoId);

        assertEquals(2, lista.size());
    }

    @Test
    void givenProductoInexistente_whenListarPorProducto_thenThrow() {
        UUID productoId = UUID.randomUUID();
        when(productoRepository.existsById(productoId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> versionService.listarPorProducto(productoId));
    }

    @Test
    void givenVersionExistente_whenActualizar_thenSuccess() {
        UUID id = UUID.randomUUID();
        VersionRequest request = new VersionRequest();
        request.setNumeroVersion("2.0.0");
        request.setFechaLanzamiento(LocalDate.now());
        request.setEstado(EstadoVersion.RELEASE);

        Version existente = new Version();
        existente.setId(id);
        existente.setNumeroVersion("1.0.0");

        when(versionRepository.findById(id)).thenReturn(Optional.of(existente));
        when(versionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        VersionResponse response = versionService.actualizar(id, request);

        assertEquals("2.0.0", response.getNumeroVersion());
        assertEquals(EstadoVersion.RELEASE, response.getEstado());
    }

    @Test
    void givenVersionInexistente_whenActualizar_thenThrow() {
        UUID id = UUID.randomUUID();
        VersionRequest request = new VersionRequest();
        when(versionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> versionService.actualizar(id, request));
    }

    @Test
    void givenVersionExistente_whenEliminar_thenSuccess() {
        UUID id = UUID.randomUUID();
        when(versionRepository.existsById(id)).thenReturn(true);
        doNothing().when(versionRepository).deleteById(id);

        assertDoesNotThrow(() -> versionService.eliminar(id));
        verify(versionRepository).deleteById(id);
    }

    @Test
    void givenVersionInexistente_whenEliminar_thenThrow() {
        UUID id = UUID.randomUUID();
        when(versionRepository.existsById(id)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> versionService.eliminar(id));
    }
}

