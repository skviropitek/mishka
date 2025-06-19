import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class TabelaPane extends VBox {
    public TabelaPane(LeagueManager manager, TableView<Druzyna> tabela) {
        tabela.getColumns().clear();

        // Название команды
        TableColumn<Druzyna, String> colNazwa = new TableColumn<>("Дrużyna");
        colNazwa.setCellValueFactory(
                data -> new ReadOnlyStringWrapper(data.getValue().getNazwa())
        );

        // Очки
        TableColumn<Druzyna, Integer> colPunkty = new TableColumn<>("Punkty");
        colPunkty.setCellValueFactory(
                data -> new ReadOnlyObjectWrapper<>(data.getValue().getPunkty())
        );

        // Забитые голы
        TableColumn<Druzyna, Integer> colPlus = new TableColumn<>("Bramki+");
        colPlus.setCellValueFactory(
                data -> new ReadOnlyObjectWrapper<>(data.getValue().getBramkiZdobyte())
        );

        // Пропущенные голы
        TableColumn<Druzyna, Integer> colMinus = new TableColumn<>("Bramki–");
        colMinus.setCellValueFactory(
                data -> new ReadOnlyObjectWrapper<>(data.getValue().getBramkiStracone())
        );

        // --- Убираем разницу и добавляем число матчей ---
        TableColumn<Druzyna, Integer> colMatches = new TableColumn<>("Mecze");
        colMatches.setCellValueFactory(data -> {
            Druzyna d = data.getValue();
            long count = manager.getMatchesList().stream()
                    .filter(m -> m.getGospodarze().equals(d) || m.getGoscie().equals(d))
                    .count();
            return new ReadOnlyObjectWrapper<>((int) count);
        });

        tabela.getColumns().addAll(colNazwa, colPunkty, colPlus, colMinus, colMatches);
        tabela.getItems().setAll(manager.getSortedTeams());

        setPadding(new Insets(10));
        getChildren().add(tabela);
    }
}
