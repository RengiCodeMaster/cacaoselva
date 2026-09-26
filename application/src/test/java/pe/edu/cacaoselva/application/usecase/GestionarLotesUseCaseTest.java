package pe.edu.cacaoselva.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pe.edu.cacaoselva.application.dto.DatosLote;
import pe.edu.cacaoselva.application.exception.DatosLoteInvalidosException;
import pe.edu.cacaoselva.application.exception.LoteNoEncontradoException;
import pe.edu.cacaoselva.application.port.LoteRepository;
import pe.edu.cacaoselva.domain.model.EstadoLote;
import pe.edu.cacaoselva.domain.model.Lote;

@ExtendWith(MockitoExtension.class)
class GestionarLotesUseCaseTest {

    @Mock
    private LoteRepository loteRepository;

    private CrearLoteUseCase crearLoteUseCase;
    private ActualizarLoteUseCase actualizarLoteUseCase;
    private EliminarLoteUseCase eliminarLoteUseCase;

    @BeforeEach
    void configurar() {
        crearLoteUseCase = new CrearLoteUseCase(loteRepository);
        actualizarLoteUseCase = new ActualizarLoteUseCase(loteRepository);
        eliminarLoteUseCase = new EliminarLoteUseCase(loteRepository);
    }

    @Test
    void debeCrearLoteConSocioNormalizado() {
        DatosLote datos = datos("  Nueva socia  ");
        when(loteRepository.save(any(Lote.class)))
                .thenAnswer(invocacion -> {
                    Lote lote = invocacion.getArgument(0);
                    return new Lote(11, lote.socio(), lote.pesoKg(), lote.estado());
                });

        Lote creado = crearLoteUseCase.crear(datos);

        assertEquals(11, creado.id());
        assertEquals("Nueva socia", creado.socio());
    }

    @Test
    void debeRechazarDatosInvalidosAlCrear() {
        DatosLote datos = new DatosLote(" ", BigDecimal.ZERO, null);

        assertThrows(DatosLoteInvalidosException.class,
                () -> crearLoteUseCase.crear(datos));
        verifyNoInteractions(loteRepository);
    }

    @Test
    void debeActualizarLoteExistente() {
        Lote existente = new Lote(3, "Rosa", new BigDecimal("95.25"), EstadoLote.PENDIENTE);
        when(loteRepository.findById(3)).thenReturn(Optional.of(existente));
        when(loteRepository.save(any(Lote.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

        Lote actualizado = actualizarLoteUseCase.actualizar(3,
                new DatosLote("Rosa Quispe", new BigDecimal("100.00"), EstadoLote.LIQUIDADO));

        assertEquals(3, actualizado.id());
        assertEquals(EstadoLote.LIQUIDADO, actualizado.estado());
        verify(loteRepository).save(actualizado);
    }

    @Test
    void debeFallarAlActualizarLoteInexistente() {
        when(loteRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(LoteNoEncontradoException.class,
                () -> actualizarLoteUseCase.actualizar(999, datos("Socio")));
        verify(loteRepository, never()).save(any());
    }

    @Test
    void debeEliminarLoteExistente() {
        Lote existente = new Lote(2, "Luis", new BigDecimal("80.00"), EstadoLote.LIQUIDADO);
        when(loteRepository.findById(2)).thenReturn(Optional.of(existente));

        eliminarLoteUseCase.eliminar(2);

        verify(loteRepository).deleteById(2);
    }

    @Test
    void debeFallarAlEliminarLoteInexistente() {
        when(loteRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(LoteNoEncontradoException.class,
                () -> eliminarLoteUseCase.eliminar(999));
        verify(loteRepository, never()).deleteById(any());
    }

    private DatosLote datos(String socio) {
        return new DatosLote(socio, new BigDecimal("42.50"), EstadoLote.PENDIENTE);
    }
}
