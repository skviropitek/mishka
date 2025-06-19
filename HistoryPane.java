import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class HistoryPane extends VBox {
    public HistoryPane(LeagueManager manager, TableView<Druzyna> tabelaDruzyn) {
        setSpacing(10);
        setPadding(new Insets(10));

        TableView<Mecz> table = new TableView<>();
        ObservableList<Mecz> matches = manager.getMatchesList();
        table.setItems(matches);

        // колонки команд и статистики
        TableColumn<Mecz, String> colG = new TableColumn<>("Gospodarze");
        colG.setCellValueFactory(d ->
                new ReadOnlyStringWrapper(d.getValue().getGospodarze().getNazwa()));
        TableColumn<Mecz, String> colO = new TableColumn<>("Goście");
        colO.setCellValueFactory(d ->
                new ReadOnlyStringWrapper(d.getValue().getGoscie().getNazwa()));
        TableColumn<Mecz, Integer> colBG = new TableColumn<>("Gole G.");
        colBG.setCellValueFactory(d ->
                new ReadOnlyObjectWrapper<>(d.getValue().getBramkiGospodarzy()));
        TableColumn<Mecz, Integer> colBO = new TableColumn<>("Gole O.");
        colBO.setCellValueFactory(d ->
                new ReadOnlyObjectWrapper<>(d.getValue().getBramkiGosci()));
        TableColumn<Mecz, Integer> colKG = new TableColumn<>("Kartki G.");
        colKG.setCellValueFactory(d ->
                new ReadOnlyObjectWrapper<>(d.getValue().getKartkiGospodarzy()));
        TableColumn<Mecz, Integer> colKO = new TableColumn<>("Kartki O.");
        colKO.setCellValueFactory(d ->
                new ReadOnlyObjectWrapper<>(d.getValue().getKartkiGosci()));

        // колонка пенальти (две строки кружков)
        TableColumn<Mecz, Void> colPen = new TableColumn<>("Penalty");
        colPen.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Void v, boolean empty) {
                super.updateItem(v, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Mecz m = getTableView().getItems().get(getIndex());
                    List<Boolean> seqG = m.getPenaltySeqGospodarzy();
                    List<Boolean> seqO = m.getPenaltySeqGosci();
                    if (seqG.isEmpty() && seqO.isEmpty()) {
                        setGraphic(null);
                    } else {
                        VBox vb = new VBox(2);
                        HBox rowG = new HBox(2), rowO = new HBox(2);
                        for (boolean hit : seqG) {
                            rowG.getChildren().add(new Circle(6, hit ? Color.GREEN : Color.RED));
                        }
                        for (boolean hit : seqO) {
                            rowO.getChildren().add(new Circle(6, hit ? Color.GREEN : Color.RED));
                        }
                        vb.getChildren().addAll(rowG, rowO);
                        setGraphic(vb);
                    }
                }
            }
        });

        // колонка Edit
        TableColumn<Mecz, Void> colEdit = new TableColumn<>("Edit");
        colEdit.setCellFactory(tc -> new TableCell<>() {
            private final Button btn = new Button("✎");
            {
                btn.setOnAction(e -> {
                    Mecz m = getTableView().getItems().get(getIndex());
                    showEditDialog(m, manager, tabelaDruzyn, table);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        // колонка Delete
        TableColumn<Mecz, Void> colDel = new TableColumn<>("Del");
        colDel.setCellFactory(tc -> new TableCell<>() {
            private final Button btn = new Button("✖");
            {
                btn.setOnAction(e -> {
                    Mecz m = getTableView().getItems().get(getIndex());
                    manager.getMatchesList().remove(m);
                    manager.recalculateStats();
                    tabelaDruzyn.getItems().setAll(manager.getSortedTeams());
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        table.getColumns().addAll(colG, colO, colBG, colBO, colKG, colKO, colPen, colEdit, colDel);
        getChildren().add(table);
    }

    private void showEditDialog(Mecz baseM,
                                LeagueManager manager,
                                TableView<Druzyna> tabelaDruzyn,
                                TableView<Mecz> historyTable) {
        if (!(baseM instanceof MeczPilkaNozna m)) return;

        Stage dlg = new Stage();
        dlg.initModality(Modality.APPLICATION_MODAL);
        dlg.setTitle("Редактировать матч");

        Label lblG = new Label("Gospodarze: " + m.getGospodarze().getNazwa());
        Label lblO = new Label("Goście:    " + m.getGoscie().getNazwa());

        Spinner<Integer> spG = new Spinner<>(0, 20, m.getBramkiGospodarzy());
        Spinner<Integer> spO = new Spinner<>(0, 20, m.getBramkiGosci());
        Spinner<Integer> skG = new Spinner<>(0, 11, m.getKartkiGospodarzy());
        Spinner<Integer> skO = new Spinner<>(0, 11, m.getKartkiGosci());
        spG.setEditable(true); spO.setEditable(true);
        skG.setEditable(true); skO.setEditable(true);

        // чекбокс пенальти — только если счёт равный
        CheckBox cbP = new CheckBox("Seria karnych");
        boolean draw = spG.getValue().equals(spO.getValue());
        cbP.setDisable(!draw);

        List<Boolean> oldG = m.getPenaltySeqGospodarzy();
        List<Boolean> oldO = m.getPenaltySeqGosci();
        cbP.setSelected(draw && !oldG.isEmpty());

        HBox rowG = new HBox(5), rowO = new HBox(5);
        List<ToggleButton> btnsG = new ArrayList<>(), btnsO = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ToggleButton bg = makeDotButton();
            ToggleButton bo = makeDotButton();
            if (i < oldG.size() && oldG.get(i)) bg.setSelected(true);
            if (i < oldO.size() && oldO.get(i)) bo.setSelected(true);
            btnsG.add(bg); btnsO.add(bo);
            rowG.getChildren().add(bg);
            rowO.getChildren().add(bo);
        }
        // отключаем ряды, если чекбокс недоступен
        rowG.setDisable(!cbP.isSelected());
        rowO.setDisable(!cbP.isSelected());

        // при изменении голов — обновлять доступность пенальти
        Runnable checkDraw = () -> {
            boolean nowDraw = spG.getValue().equals(spO.getValue());
            cbP.setDisable(!nowDraw);
            if (!nowDraw) {
                cbP.setSelected(false);
            }
            rowG.setDisable(!cbP.isSelected());
            rowO.setDisable(!cbP.isSelected());
        };
        spG.valueProperty().addListener((o, a, b) -> checkDraw.run());
        spO.valueProperty().addListener((o, a, b) -> checkDraw.run());
        cbP.selectedProperty().addListener((o, a, b) -> {
            rowG.setDisable(!b);
            rowO.setDisable(!b);
        });

        Button btnSave = new Button("Сохранить");
        btnSave.setOnAction(e -> {
            List<Boolean> seqG = btnsG.stream().map(ToggleButton::isSelected).toList();
            List<Boolean> seqO = btnsO.stream().map(ToggleButton::isSelected).toList();
            try {
                m.rozegranoMecz(
                        spG.getValue(), spO.getValue(),
                        skG.getValue(), skO.getValue(),
                        seqG, seqO
                );
                manager.recalculateStats();
                tabelaDruzyn.getItems().setAll(manager.getSortedTeams());
                historyTable.refresh();
                dlg.close();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).showAndWait();
            }
        });

        VBox vb = new VBox(10,
                lblG, new HBox(5, new Label("Gole:"), spG),
                lblO, new HBox(5, new Label("Gole:"), spO),
                new HBox(10, new Label("Kartki Gosp.:"), skG, new Label("Gość:"), skO),
                cbP, new Label("Seria karnych:"), rowG, rowO,
                btnSave
        );
        vb.setPadding(new Insets(10));
        dlg.setScene(new Scene(vb));
        dlg.showAndWait();
    }

    private ToggleButton makeDotButton() {
        ToggleButton b = new ToggleButton("○");
        b.setOnAction(e -> b.setText(b.isSelected() ? "●" : "○"));
        b.setPrefWidth(24);
        return b;
    }
}
