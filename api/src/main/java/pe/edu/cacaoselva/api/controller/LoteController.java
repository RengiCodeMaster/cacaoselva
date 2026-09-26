package pe.edu.cacaoselva.api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.cacaoselva.api.dto.LoteRequest;
import pe.edu.cacaoselva.api.dto.LoteResponse;
import pe.edu.cacaoselva.api.mapper.LoteMapper;
import pe.edu.cacaoselva.application.usecase.ActualizarLoteUseCase;
import pe.edu.cacaoselva.application.usecase.BuscarLotePorIdUseCase;
import pe.edu.cacaoselva.application.usecase.CrearLoteUseCase;
import pe.edu.cacaoselva.application.usecase.EliminarLoteUseCase;
import pe.edu.cacaoselva.application.usecase.ListarLotesUseCase;

@RestController
@RequestMapping("/lotes")
public class LoteController {

    private final ListarLotesUseCase listarLotesUseCase;
    private final BuscarLotePorIdUseCase buscarLotePorIdUseCase;
    private final CrearLoteUseCase crearLoteUseCase;
    private final ActualizarLoteUseCase actualizarLoteUseCase;
    private final EliminarLoteUseCase eliminarLoteUseCase;

    public LoteController(ListarLotesUseCase listarLotesUseCase,
                          BuscarLotePorIdUseCase buscarLotePorIdUseCase,
                          CrearLoteUseCase crearLoteUseCase,
                          ActualizarLoteUseCase actualizarLoteUseCase,
                          EliminarLoteUseCase eliminarLoteUseCase) {
        this.listarLotesUseCase = listarLotesUseCase;
        this.buscarLotePorIdUseCase = buscarLotePorIdUseCase;
        this.crearLoteUseCase = crearLoteUseCase;
        this.actualizarLoteUseCase = actualizarLoteUseCase;
        this.eliminarLoteUseCase = eliminarLoteUseCase;
    }

    @GetMapping
    public List<LoteResponse> listar() {
        return listarLotesUseCase.listar().stream()
                .map(LoteMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public LoteResponse buscar(@PathVariable Integer id) {
        return LoteMapper.toResponse(buscarLotePorIdUseCase.buscar(id));
    }

    @PostMapping
    public ResponseEntity<LoteResponse> crear(@RequestBody LoteRequest request) {
        LoteResponse response = LoteMapper.toResponse(
                crearLoteUseCase.crear(LoteMapper.toDatos(request)));
        return ResponseEntity.created(URI.create("/lotes/" + response.id())).body(response);
    }

    @PutMapping("/{id}")
    public LoteResponse actualizar(@PathVariable Integer id, @RequestBody LoteRequest request) {
        return LoteMapper.toResponse(
                actualizarLoteUseCase.actualizar(id, LoteMapper.toDatos(request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        eliminarLoteUseCase.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
