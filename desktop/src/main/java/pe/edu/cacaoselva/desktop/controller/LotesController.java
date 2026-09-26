package pe.edu.cacaoselva.desktop.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import pe.edu.cacaoselva.application.dto.DatosLote;
import pe.edu.cacaoselva.application.dto.LoteDto;
import pe.edu.cacaoselva.application.port.LoteCommandPort;
import pe.edu.cacaoselva.application.port.LoteQueryPort;
import pe.edu.cacaoselva.desktop.adapter.LoteVistaAdapter;
import pe.edu.cacaoselva.desktop.dto.LoteVista;
import pe.edu.cacaoselva.domain.model.EstadoLote;

public class LotesController {

    private static final String TODOS = "TODOS";

    private final LoteQueryPort loteQueryPort;
    private final LoteCommandPort loteCommandPort;
    private final ObservableList<LoteVista> lotes = FXCollections.observableArrayList();

    private boolean ocupado;

    @FXML private Button consultarButton;
    @FXML private Button guardarButton;
    @FXML private Button eliminarButton;
    @FXML private TextField filtroSocioField;
    @FXML private ComboBox<String> filtroEstadoCombo;
    @FXML private TextField socioField;
    @FXML private TextField pesoField;
    @FXML private ComboBox<EstadoLote> estadoCombo;
    @FXML private TableView<LoteVista> lotesTable;
    @FXML private TableColumn<LoteVista, Integer> idColumn;
    @FXML private TableColumn<LoteVista, String> socioColumn;
    @FXML private TableColumn<LoteVista, String> pesoColumn;
    @FXML private TableColumn<LoteVista, String> estadoColumn;
    @FXML private Label estadoLabel;

    public LotesController(LoteQueryPort loteQueryPort, LoteCommandPort loteCommandPort) {
        this.loteQueryPort = loteQueryPort;
        this.loteCommandPort = loteCommandPort;
    }

    @FXML
    private void initialize() {
        configurarTabla();
        configurarFiltros();
        estadoCombo.setItems(FXCollections.observableArrayList(EstadoLote.values()));
        estadoCombo.setValue(EstadoLote.PENDIENTE);
        lotesTable.getSelectionModel().selectedItemProperty()
                .addListener((observable, anterior, seleccionado) -> seleccionar(seleccionado));
        actualizarBotones();
        Platform.runLater(this::onConsultar);
    }

    private void configurarTabla() {
        idColumn.setCellValueFactory(celda -> new SimpleObjectProperty<>(celda.getValue().id()));
        socioColumn.setCellValueFactory(celda -> new SimpleStringProperty(celda.getValue().socio()));
        pesoColumn.setCellValueFactory(celda -> new SimpleStringProperty(celda.getValue().peso()));
        estadoColumn.setCellValueFactory(celda -> new SimpleStringProperty(celda.getValue().estado()));
        lotesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private void configurarFiltros() {
        filtroEstadoCombo.setItems(FXCollections.observableArrayList(
                TODOS, EstadoLote.PENDIENTE.name(), EstadoLote.LIQUIDADO.name()));
        filtroEstadoCombo.setValue(TODOS);
        FilteredList<LoteVista> filtrados = new FilteredList<>(lotes);
        filtrados.predicateProperty().bind(Bindings.createObjectBinding(
                () -> this::coincideConFiltros,
                filtroSocioField.textProperty(), filtroEstadoCombo.valueProperty()));
        lotesTable.setItems(filtrados);
    }

    private boolean coincideConFiltros(LoteVista lote) {
        String texto = filtroSocioField.getText() == null
                ? ""
                : filtroSocioField.getText().trim().toLowerCase();
        String estado = filtroEstadoCombo.getValue();
        boolean coincideSocio = lote.socio().toLowerCase().contains(texto);
        boolean coincideEstado = estado == null || TODOS.equals(estado) || estado.equals(lote.estado());
        return coincideSocio && coincideEstado;
    }

    @FXML
    private void onConsultar() {
        cambiarOcupado(true, "Consultando lotes...");
        CompletableFuture.supplyAsync(loteQueryPort::obtenerLotes)
                .whenComplete((resultado, error) -> Platform.runLater(() -> mostrarConsulta(resultado, error)));
    }

    @FXML
    private void onNuevo() {
        lotesTable.getSelectionModel().clearSelection();
        limpiarFormulario();
        estadoLabel.setText("Nuevo lote");
    }

    @FXML
    private void onGuardar() {
        DatosLote datos;
        try {
            datos = leerFormulario();
        } catch (IllegalArgumentException excepcion) {
            estadoLabel.setText(excepcion.getMessage());
            return;
        }

        LoteVista seleccionado = lotesTable.getSelectionModel().getSelectedItem();
        cambiarOcupado(true, seleccionado == null ? "Creando lote..." : "Actualizando lote...");
        CompletableFuture<LoteDto> operacion = seleccionado == null
                ? CompletableFuture.supplyAsync(() -> loteCommandPort.crear(datos))
                : CompletableFuture.supplyAsync(() -> loteCommandPort.actualizar(seleccionado.id(), datos));
        operacion.whenComplete((resultado, error) ->
                Platform.runLater(() -> mostrarGuardado(resultado, error)));
    }

    @FXML
    private void onEliminar() {
        LoteVista seleccionado = lotesTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null || !confirmarEliminacion(seleccionado)) {
            return;
        }
        cambiarOcupado(true, "Eliminando lote...");
        CompletableFuture.runAsync(() -> loteCommandPort.eliminar(seleccionado.id()))
                .whenComplete((resultado, error) ->
                        Platform.runLater(() -> mostrarEliminacion(seleccionado, error)));
    }

    private DatosLote leerFormulario() {
        String socio = socioField.getText() == null ? "" : socioField.getText().trim();
        if (socio.isBlank()) {
            throw new IllegalArgumentException("Ingrese el nombre del socio");
        }
        try {
            BigDecimal peso = new BigDecimal(pesoField.getText().trim());
            return new DatosLote(socio, peso, estadoCombo.getValue());
        } catch (NullPointerException | NumberFormatException excepcion) {
            throw new IllegalArgumentException("Ingrese un peso numerico valido");
        }
    }

    private void mostrarConsulta(List<LoteDto> resultado, Throwable error) {
        cambiarOcupado(false, "");
        if (error != null) {
            mostrarError(error);
            return;
        }
        lotes.setAll(LoteVistaAdapter.toVista(resultado));
        estadoLabel.setText(resultado.size() + " lotes disponibles");
    }

    private void mostrarGuardado(LoteDto resultado, Throwable error) {
        cambiarOcupado(false, "");
        if (error != null) {
            mostrarError(error);
            return;
        }
        LoteVista vista = LoteVistaAdapter.toVista(resultado);
        lotes.removeIf(lote -> lote.id().equals(vista.id()));
        lotes.add(vista);
        FXCollections.sort(lotes, (a, b) -> Integer.compare(a.id(), b.id()));
        onNuevo();
        estadoLabel.setText("Lote " + vista.id() + " guardado correctamente");
    }

    private void mostrarEliminacion(LoteVista eliminado, Throwable error) {
        cambiarOcupado(false, "");
        if (error != null) {
            mostrarError(error);
            return;
        }
        lotes.removeIf(lote -> lote.id().equals(eliminado.id()));
        onNuevo();
        estadoLabel.setText("Lote " + eliminado.id() + " eliminado");
    }

    private void seleccionar(LoteVista lote) {
        if (lote != null) {
            socioField.setText(lote.socio());
            pesoField.setText(lote.peso());
            estadoCombo.setValue(EstadoLote.valueOf(lote.estado()));
        }
        actualizarBotones();
    }

    private boolean confirmarEliminacion(LoteVista lote) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Eliminar lote");
        alerta.setHeaderText("Eliminar el lote " + lote.id());
        alerta.setContentText("Esta accion no se puede deshacer.");
        return alerta.showAndWait().filter(ButtonType.OK::equals).isPresent();
    }

    private void limpiarFormulario() {
        socioField.clear();
        pesoField.clear();
        estadoCombo.setValue(EstadoLote.PENDIENTE);
        actualizarBotones();
    }

    private void cambiarOcupado(boolean valor, String mensaje) {
        ocupado = valor;
        consultarButton.setDisable(valor);
        guardarButton.setDisable(valor);
        actualizarBotones();
        if (!mensaje.isBlank()) {
            estadoLabel.setText(mensaje);
        }
    }

    private void actualizarBotones() {
        eliminarButton.setDisable(ocupado || lotesTable.getSelectionModel().getSelectedItem() == null);
    }

    private void mostrarError(Throwable error) {
        Throwable causa = error.getCause() == null ? error : error.getCause();
        estadoLabel.setText("Error: " + causa.getMessage());
    }
}
