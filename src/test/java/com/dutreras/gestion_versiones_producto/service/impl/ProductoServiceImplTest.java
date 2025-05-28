package com.dutreras.gestion_versiones_producto.service.impl;

import com.dutreras.gestion_versiones_producto.dto.ProductoRequest;
import com.dutreras.gestion_versiones_producto.dto.ProductoResponse;
import com.dutreras.gestion_versiones_producto.model.Producto;
import com.dutreras.gestion_versiones_producto.repository.ProductoRepository;
import com.dutreras.gestion_versiones_producto.service.impl.ProductoServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    @Test
    void givenNombreDuplicado_whenCrear_thenThrowException() {
        ProductoRequest request = new ProductoRequest();
        request.setNombre("Repetido");

        Mockito.when(productoRepository.findByNombreIgnoreCase("Repetido"))
                .thenReturn(Optional.of(new Producto()));

        assertThrows(DataIntegrityViolationException.class, () ->
                productoService.crear(request));
    }

    @Test
    void givenProductoValido_whenCrear_thenSuccess() {
        ProductoRequest request = new ProductoRequest();
        request.setNombre("NuevoProducto");
        request.setDescripcion("Descripción");

        Mockito.when(productoRepository.findByNombreIgnoreCase("NuevoProducto"))
                .thenReturn(Optional.empty());

        Producto saved = new Producto();
        saved.setId(UUID.randomUUID());
        saved.setNombre("NuevoProducto");
        saved.setDescripcion("Descripción");

        Mockito.when(productoRepository.save(any(Producto.class))).thenReturn(saved);

        ProductoResponse response = productoService.crear(request);

        assertEquals("NuevoProducto", response.getNombre());
        assertEquals("Descripción", response.getDescripcion());
    }

    @Test
    void givenProductoExistente_whenObtenerPorId_thenSuccess() {
        UUID id = UUID.randomUUID();
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre("Producto");

        when(productoRepository.findById(id)).thenReturn(Optional.of(producto));

        ProductoResponse response = productoService.obtenerPorId(id);

        assertEquals("Producto", response.getNombre());
    }

    @Test
    void givenProductoInexistente_whenObtenerPorId_thenThrow() {
        UUID id = UUID.randomUUID();
        when(productoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productoService.obtenerPorId(id));
    }

    @Test
    void whenListarTodos_thenReturnLista() {
        Producto p1 = new Producto();
        p1.setNombre("A");

        Producto p2 = new Producto();
        p2.setNombre("B");

        when(productoRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<ProductoResponse> lista = productoService.listarTodos();

        assertEquals(2, lista.size());
    }

    @Test
    void givenProductoExistente_whenActualizar_thenSuccess() {
        UUID id = UUID.randomUUID();
        ProductoRequest request = new ProductoRequest();
        request.setNombre("NuevoNombre");
        request.setDescripcion("NuevaDesc");

        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre("Antiguo");
        producto.setDescripcion("Antigua");

        when(productoRepository.findById(id)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ProductoResponse response = productoService.actualizar(id, request);

        assertEquals("NuevoNombre", response.getNombre());
        assertEquals("NuevaDesc", response.getDescripcion());
    }

    @Test
    void givenProductoInexistente_whenActualizar_thenThrow() {
        UUID id = UUID.randomUUID();
        when(productoRepository.findById(id)).thenReturn(Optional.empty());

        ProductoRequest request = new ProductoRequest();
        request.setNombre("x");

        assertThrows(EntityNotFoundException.class, () -> productoService.actualizar(id, request));
    }

    @Test
    void givenProductoExistente_whenEliminar_thenSuccess() {
        UUID id = UUID.randomUUID();
        when(productoRepository.existsById(id)).thenReturn(true);
        doNothing().when(productoRepository).deleteById(id);

        assertDoesNotThrow(() -> productoService.eliminar(id));
        verify(productoRepository).deleteById(id);
    }

    @Test
    void givenProductoInexistente_whenEliminar_thenThrow() {
        UUID id = UUID.randomUUID();
        when(productoRepository.existsById(id)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> productoService.eliminar(id));
    }
    
    
}
