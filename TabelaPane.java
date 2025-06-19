
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.util.List;

public class TabelaPane extends VBox {
    public TabelaPane(TableView<Druzyna> tabela, List<Druzyna> druzyny) {
        TableColumn<Druzyna, String> colNazwa = new TableColumn<>("Drużyna");
        colNazwa.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getNazwa()));

        TableColumn<Druzyna, Integer> colPunkty = new TableColumn<>("Punkty");
        colPunkty.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getPunkty()));

        TableColumn<Druzyna, Integer> colBramki = new TableColumn<>("Bramki+");
        colBramki.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getBramkiZdobyte()));

        TableColumn<Druzyna, Integer> colStracone = new TableColumn<>("Bramki-");
        colStracone.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getBramkiStracone()));

        TableColumn<Druzyna, Integer> colRoznica = new TableColumn<>("Różnica");
        colRoznica.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getRoznicaBramkowa()));

        tabela.getColumns().addAll(colNazwa, colPunkty, colBramki, colStracone, colRoznica);
        tabela.getItems().setAll(druzyny);

        this.setPadding(new javafx.geometry.Insets(10));
        this.getChildren().add(tabela);
    }
}
