
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.util.List;

public class MeczePane extends VBox {
    public MeczePane(List<Druzyna> druzyny, List<Mecz> mecze, TableView<Druzyna> tabela) {
        ComboBox<Druzyna> cbGospodarze = new ComboBox<>();
        ComboBox<Druzyna> cbGoscie = new ComboBox<>();
        TextField tfBramkiGospodarze = new TextField();
        TextField tfBramkiGoscie = new TextField();
        Button btnDodajMecz = new Button("Zapisz wynik meczu");

        cbGospodarze.getItems().addAll(druzyny);
        cbGoscie.getItems().addAll(druzyny);

        btnDodajMecz.setOnAction(e -> {
            Druzyna gospodarz = cbGospodarze.getValue();
            Druzyna gosc = cbGoscie.getValue();
            try {
                int brGosp = Integer.parseInt(tfBramkiGospodarze.getText());
                int brGosc = Integer.parseInt(tfBramkiGoscie.getText());
                if (gospodarz != null && gosc != null && gospodarz != gosc) {
                    MeczPilkaNozna mecz = new MeczPilkaNozna(gospodarz, gosc);
                    mecz.rozegranoMecz(brGosp, brGosc);
                    mecze.add(mecz);
                    tabela.refresh();
                }
            } catch (NumberFormatException ex) {
                System.out.println("Błąd formatu bramek");
            }
        });

        this.setSpacing(10);
        this.setPadding(new javafx.geometry.Insets(10));
        this.getChildren().addAll(new Label("Gospodarze"), cbGospodarze, tfBramkiGospodarze,
                                  new Label("Goście"), cbGoscie, tfBramkiGoscie, btnDodajMecz);
    }
}
