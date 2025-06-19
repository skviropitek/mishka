import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.io.File;
import java.io.IOException;

public class TurniejPilkiNoznejApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Turniej Piłkarski");

        LeagueManager manager = new LeagueManager();
        TableView<Druzyna> tabelaDruzyn = new TableView<>();

        MenuBar menuBar = createMenuBar(primaryStage, manager, tabelaDruzyn);

        TabPane tabs = new TabPane();
        tabs.getTabs().addAll(
                new Tab("Drużyny",  new DruzynyPane(manager, tabelaDruzyn)),
                new Tab("Mecze",    new MeczePane(manager, tabelaDruzyn)),
                new Tab("Tabela",   new TabelaPane(manager, tabelaDruzyn)),
                new Tab("Historia", new HistoryPane(manager, tabelaDruzyn))
        );
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        VBox root = new VBox(menuBar, tabs);
        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private MenuBar createMenuBar(Stage primaryStage,
                                  LeagueManager manager,
                                  TableView<Druzyna> tabela) {
        Menu fileMenu = new Menu("Файл");

        MenuItem miSave = new MenuItem("Сохранить...");
        miSave.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Сохранить турнир");
            File file = fc.showSaveDialog(primaryStage);
            if (file != null) {
                try {
                    manager.saveToFile(file.getAbsolutePath());
                    showInfo("Сохранено в " + file.getName());
                } catch (IOException ex) {
                    showError("Ошибка сохранения: " + ex.getMessage());
                }
            }
        });

        MenuItem miLoad = new MenuItem("Загрузить...");
        miLoad.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Загрузить турнир");
            File file = fc.showOpenDialog(primaryStage);
            if (file != null) {
                try {
                    manager.loadFromFile(file.getAbsolutePath());
                    // После загрузки пересчитываем статистику и обновляем таблицу
                    manager.recalculateStats();
                    tabela.getItems().setAll(manager.getSortedTeams());
                    showInfo("Загружено из " + file.getName());
                } catch (Exception ex) {
                    showError("Ошибка загрузки: " + ex.getMessage());
                }
            }
        });

        fileMenu.getItems().addAll(miSave, miLoad);
        return new MenuBar(fileMenu);
    }

    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }
}
