
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;

public class TurniejPilkiNoznejApp extends Application {

    private final List<Druzyna> listaDruzyn = new ArrayList<>();
    private final List<Mecz> listaMeczy = new ArrayList<>();
    private final TableView<Druzyna> tabelaDruzyn = new TableView<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Turniej Piłkarski");

        TabPane tabPane = new TabPane();

        Tab tabDruzyny = new Tab("Drużyny", new DruzynyPane(listaDruzyn, tabelaDruzyn));
        Tab tabMecze = new Tab("Mecze", new MeczePane(listaDruzyn, listaMeczy, tabelaDruzyn));
        Tab tabTabela = new Tab("Tabela", new TabelaPane(tabelaDruzyn, listaDruzyn));

        tabPane.getTabs().addAll(tabDruzyny, tabMecze, tabTabela);

        primaryStage.setScene(new Scene(tabPane, 800, 600));
        primaryStage.show();
    }
}
