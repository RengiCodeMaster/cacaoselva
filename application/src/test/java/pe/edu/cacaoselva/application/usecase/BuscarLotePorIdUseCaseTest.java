package pe.edu.cacaoselva.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pe.edu.cacaoselva.application.exception.IdentificadorLoteInvalidoException;
import pe.edu.cacaoselva.application.exception.LoteNoEncontradoException;
import pe.edu.cacaoselva.application.port.LoteRepository;
import pe.edu.cacaoselva.domain.model.EstadoLote;
import pe.edu.cacaoselva.domain.model.Lote;

@ExtendWith(MockitoExtension.class)
class BuscarLotePorIdUseCaseTest {

    @Mock
    private LoteRepository loteRepository;

    @InjectMocks
    private BuscarLotePorIdUseCase buscarLotePorIdUseCase;

    @Test
    void debeDevolverUnLoteExistente() {
        Lote ana = new Lote(1, "Ana", new BigDecimal("120.5"), EstadoLote.PENDIENTE);
        when(loteRepository.findById(1)).thenReturn(Optional.of(ana));

        Lote resultado = buscarLotePorIdUseCase.buscar(1);

        assertEquals(ana, resultado);
    }

    @Test
    void debeLanzarExcepcionCuandoElLoteNoExiste() {
        when(loteRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(LoteNoEncontradoException.class,
                () -> buscarLotePorIdUseCase.buscar(999));
    }

    @Test
    void debeRechazarIdentificadorNulo() {
        assertThrows(IdentificadorLoteInvalidoException.class,
                () -> buscarLotePorIdUseCase.buscar(null));
        verifyNoInteractions(loteRepository);
    }

    @Test
    void debeRechazarIdentificadorCero() {
        assertThrows(IdentificadorLoteInvalidoException.class,
                () -> buscarLotePorIdUseCase.buscar(0));
        verifyNoInteractions(loteRepository);
    }

    @Test
    void debeRechazarIdentificadorNegativo() {
        assertThrows(IdentificadorLoteInvalidoException.class,
                () -> buscarLotePorIdUseCase.buscar(-5));
        verifyNoInteractions(loteRepository);
    }
}
