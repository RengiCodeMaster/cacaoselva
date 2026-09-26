package pe.edu.cacaoselva.desktop;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import pe.edu.cacaoselva.application.port.LoteCommandPort;
import pe.edu.cacaoselva.application.port.LoteQueryPort;
import pe.edu.cacaoselva.desktop.config.DesktopConfig;
import pe.edu.cacaoselva.desktop.controller.LotesController;

public class CacaoSelvaDesktopApplication extends Application {

    private static final String TITULO = "CacaoSelva - Lotes";
    private static final String VISTA_LOTES = "/pe/edu/cacaoselva/desktop/view/lotes-view.fxml";

    @Override
    public void start(Stage stage) throws IOException {
        LoteQueryPort loteQueryPort = DesktopConfig.loteQueryPort();
        LoteCommandPort loteCommandPort = DesktopConfig.loteCommandPort();
        Parent raiz = cargarVista(loteQueryPort, loteCommandPort);
        stage.setTitle(TITULO);
        stage.setMinWidth(760);
        stage.setMinHeight(560);
        stage.setScene(new Scene(raiz, 900, 640));
        stage.show();
    }

    private Parent cargarVista(LoteQueryPort loteQueryPort, LoteCommandPort loteCommandPort) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(VISTA_LOTES));
        loader.setControllerFactory(tipo -> {
            if (tipo == LotesController.class) {
                return new LotesController(loteQueryPort, loteCommandPort);
            }
            throw new IllegalStateException("Controlador no soportado: " + tipo.getName());
        });
        return loader.load();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
