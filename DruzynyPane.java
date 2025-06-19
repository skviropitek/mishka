import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class DruzynyPane extends VBox {
    public DruzynyPane(LeagueManager manager, TableView<Druzyna> tabela) {
        Label lblNazwa = new Label("Nazwa drużyny:");
        TextField tfNazwa = new TextField();
        Label lblTrener = new Label("Trener:");
        TextField tfTrener = new TextField();
        Button btnDodaj = new Button("Dodaj drużynę");

        btnDodaj.setOnAction(e -> {
            try {
                manager.addTeam(tfNazwa.getText(), tfTrener.getText());
                tfNazwa.clear();
                tfTrener.clear();
                tabela.getItems().setAll(manager.getSortedTeams());
            } catch (Exception ex) {
                new Alert(Alert.AlertType.WARNING, ex.getMessage())
                        .showAndWait();
            }
        });

        setSpacing(10);
        setPadding(new Insets(10));
        getChildren().addAll(lblNazwa, tfNazwa, lblTrener, tfTrener, btnDodaj);
    }
}
