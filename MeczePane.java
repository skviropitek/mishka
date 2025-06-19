import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MeczePane extends VBox {
    public MeczePane(LeagueManager manager, TableView<Druzyna> tabela) {
        setSpacing(10);
        setPadding(new Insets(10));

        // 1) ComboBox для выбора команд
        ComboBox<Druzyna> cbGosp = new ComboBox<>(manager.getTeamsList());
        ComboBox<Druzyna> cbGosc = new ComboBox<>(manager.getTeamsList());

        // 2) Спиннеры для голов (0–20)
        Spinner<Integer> spGoleG = new Spinner<>(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 20, 0));
        spGoleG.setEditable(true);
        Spinner<Integer> spGoleO = new Spinner<>(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 20, 0));
        spGoleO.setEditable(true);

        // 3) Спиннеры для карточек (0–11)
        Spinner<Integer> spKartkiG = new Spinner<>(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 11, 0));
        spKartkiG.setEditable(true);
        Spinner<Integer> spKartkiO = new Spinner<>(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 11, 0));
        spKartkiO.setEditable(true);

        // 4) Пенальти
        CheckBox cbP = new CheckBox("Seria karnych");
        cbP.setDisable(true);

        // 5) Ряды ToggleButton для пенальти (5 бросков)
        HBox dotsG = new HBox(5), dotsO = new HBox(5);
        List<ToggleButton> btnsG = new ArrayList<>(), btnsO = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ToggleButton tg = makeDotButton();
            ToggleButton to = makeDotButton();
            btnsG.add(tg); dotsG.getChildren().add(tg);
            btnsO.add(to); dotsO.getChildren().add(to);
        }
        dotsG.setDisable(true);
        dotsO.setDisable(true);

        // 6) Кнопка «Zapisz wynik»
        Button btnZapisz = new Button("Zapisz wynik");

        // 7) Runnable для валидации
        final Runnable validate = () -> {
            // Проверяем выбор команд
            boolean teamsOk = cbGosp.getValue() != null
                    && cbGosc.getValue() != null
                    && !cbGosp.getValue().equals(cbGosc.getValue());

            // Получаем безопасно значения спиннеров
            Integer gG = (spGoleG.getValueFactory() != null
                    ? spGoleG.getValueFactory().getValue() : null);
            Integer gO = (spGoleO.getValueFactory() != null
                    ? spGoleO.getValueFactory().getValue() : null);
            Integer kG = (spKartkiG.getValueFactory() != null
                    ? spKartkiG.getValueFactory().getValue() : null);
            Integer kO = (spKartkiO.getValueFactory() != null
                    ? spKartkiO.getValueFactory().getValue() : null);

            boolean goalsOk = gG != null && gO != null;
            boolean cardsOk = kG != null && kO != null;

            // Пенальти только при ничье
            boolean draw = goalsOk && Objects.equals(gG, gO);
            cbP.setDisable(!draw);
            if (!draw) cbP.setSelected(false);

            // Ряды точек активны только если чекбокс включен
            dotsG.setDisable(!cbP.isSelected());
            dotsO.setDisable(!cbP.isSelected());

            // Активируем кнопку лишь если все базовые поля валидны
            btnZapisz.setDisable(!(teamsOk && goalsOk && cardsOk));
        };

        // 8) Привязываем validate ко всем контролам
        cbGosp.valueProperty().addListener((o, a, b) -> validate.run());
        cbGosc.valueProperty().addListener((o, a, b) -> validate.run());
        spGoleG.valueProperty().addListener((o, a, b) -> validate.run());
        spGoleO.valueProperty().addListener((o, a, b) -> validate.run());
        spKartkiG.valueProperty().addListener((o, a, b) -> validate.run());
        spKartkiO.valueProperty().addListener((o, a, b) -> validate.run());
        cbP.selectedProperty().addListener((o, a, b) -> validate.run());

        // Первоначальная проверка
        validate.run();

        // 9) Действие кнопки
        btnZapisz.setOnAction(e -> {
            // Собираем безопасные int-значения
            int brG = spGoleG.getValueFactory().getValue();
            int brO = spGoleO.getValueFactory().getValue();
            int ktG = spKartkiG.getValueFactory().getValue();
            int ktO = spKartkiO.getValueFactory().getValue();
            List<Boolean> seqG = btnsG.stream().map(ToggleButton::isSelected).toList();
            List<Boolean> seqO = btnsO.stream().map(ToggleButton::isSelected).toList();

            try {
                manager.addMatch(
                        cbGosp.getValue(), cbGosc.getValue(),
                        brG, brO, ktG, ktO, seqG, seqO
                );
                tabela.getItems().setAll(manager.getSortedTeams());
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK)
                        .showAndWait();
            }
        });

        // 10) Собираем UI
        getChildren().addAll(
                new Label("Gospodarze:"), cbGosp,
                new HBox(5, new Label("Gole:"), spGoleG),
                new Label("Goście:"),    cbGosc,
                new HBox(5, new Label("Gole:"), spGoleO),
                new HBox(10,
                        new Label("Kartki Gosp.:"), spKartkiG,
                        new Label("Gość:"),         spKartkiO
                ),
                cbP,
                new Label("Seria karnych:"),
                dotsG,
                dotsO,
                btnZapisz
        );
    }

    private ToggleButton makeDotButton() {
        ToggleButton b = new ToggleButton("○");
        b.setOnAction(e -> b.setText(b.isSelected() ? "●" : "○"));
        b.setPrefWidth(24);
        return b;
    }
}
