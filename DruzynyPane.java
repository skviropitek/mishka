
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.util.List;

public class DruzynyPane extends VBox {
    public DruzynyPane(List<Druzyna> druzyny, TableView<Druzyna> tabela) {
        Label lblNazwa = new Label("Nazwa drużyny:");
        TextField tfNazwa = new TextField();
        Label lblTrener = new Label("Trener:");
        TextField tfTrener = new TextField();
        Button btnDodaj = new Button("Dodaj drużynę");

        btnDodaj.setOnAction(e -> {
            String nazwa = tfNazwa.getText();
            String trener = tfTrener.getText();
            if (!nazwa.isEmpty() && !trener.isEmpty()) {
                druzyny.add(new Druzyna(nazwa, trener));
                tfNazwa.clear();
                tfTrener.clear();
                tabela.refresh();
            }
        });

        this.setSpacing(10);
        this.setPadding(new javafx.geometry.Insets(10));
        this.getChildren().addAll(lblNazwa, tfNazwa, lblTrener, tfTrener, btnDodaj);
    }
}
