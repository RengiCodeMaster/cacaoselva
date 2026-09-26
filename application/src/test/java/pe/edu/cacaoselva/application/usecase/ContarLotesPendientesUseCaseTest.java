package pe.edu.cacaoselva.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pe.edu.cacaoselva.application.port.LoteRepository;
import pe.edu.cacaoselva.domain.model.EstadoLote;
import pe.edu.cacaoselva.domain.model.Lote;

@ExtendWith(MockitoExtension.class)
class ContarLotesPendientesUseCaseTest {

    @Mock
    private LoteRepository loteRepository;

    @InjectMocks
    private ContarLotesPendientesUseCase contarLotesPendientesUseCase;

    @Test
    void conLosDatosInicialesDebeDevolverDos() {
        when(loteRepository.findAll()).thenReturn(List.of(
                new Lote(1, "Ana", new BigDecimal("120.5"), EstadoLote.PENDIENTE),
                new Lote(2, "Luis", new BigDecimal("80"), EstadoLote.LIQUIDADO),
                new Lote(3, "Rosa", new BigDecimal("95.25"), EstadoLote.PENDIENTE)));

        long pendientes = contarLotesPendientesUseCase.contar();

        assertEquals(2, pendientes);
        verify(loteRepository).findAll();
    }

    @Test
    void debeDevolverCeroCuandoNoHayLotesPendientes() {
        when(loteRepository.findAll()).thenReturn(List.of(
                new Lote(2, "Luis", new BigDecimal("80"), EstadoLote.LIQUIDADO)));

        assertEquals(0, contarLotesPendientesUseCase.contar());
    }
}
