import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LeagueManager implements Persistable {
    private final Map<String, Druzyna> druzynyMap    = new HashMap<>();
    private final ObservableList<Druzyna> teamsList   = FXCollections.observableArrayList();
    private final ObservableList<Mecz>    matchesList = FXCollections.observableArrayList();

    public ObservableList<Druzyna> getTeamsList()   { return teamsList; }
    public ObservableList<Mecz>    getMatchesList() { return matchesList; }

    /** Возвращает список команд, отсортированный по очкам и разнице мячей. */
    public List<Druzyna> getSortedTeams() {
        return teamsList.stream()
                .sorted(
                        Comparator.comparingInt(Druzyna::getPunkty).reversed()
                                .thenComparingInt(Druzyna::getRoznicaBramkowa).reversed()
                )
                .collect(Collectors.toList());
    }

    /** Добавить новую команду. */
    public void addTeam(String nazwa, String trener) throws TeamAlreadyExistsException {
        if (druzynyMap.containsKey(nazwa)) {
            throw new TeamAlreadyExistsException("Команда «" + nazwa + "» уже существует");
        }
        Druzyna d = new Druzyna(nazwa, trener);
        druzynyMap.put(nazwa, d);
        teamsList.add(d);
    }

    /** Добавить матч в историю (не обновляет таблицу). */
    public void addMatch(
            Druzyna gosp, Druzyna gosc,
            int brG, int brO,
            int kartG, int kartO,
            List<Boolean> seqG, List<Boolean> seqO
    ) throws InvalidMatchException, InvalidScoreException {
        if (gosp == null || gosc == null) {
            throw new InvalidMatchException("Обе команды должны быть выбраны");
        }
        if (gosp.equals(gosc)) {
            throw new InvalidMatchException("Нельзя играть с самим собой");
        }
        MeczPilkaNozna m = new MeczPilkaNozna(gosp, gosc);
        m.rozegranoMecz(brG, brO, kartG, kartO, seqG, seqO);
        matchesList.add(m);
    }

    /** Пересчитать статистику всех команд по истории матчей. */
    public void recalculateStats() {
        // Сбросить у всех команд
        for (Druzyna d : teamsList) {
            d.resetStats();
        }
        // Применить каждый матч
        for (Mecz m : matchesList) {
            if (m instanceof MeczPilkaNozna mpn) {
                try {
                    mpn.getGospodarze()
                            .dodajWynik(mpn.getBramkiGospodarzy(), mpn.getBramkiGosci());
                    mpn.getGoscie()
                            .dodajWynik(mpn.getBramkiGosci(), mpn.getBramkiGospodarzy());
                } catch (InvalidScoreException ex) {
                    // Данных некорректных быть не должно, игнорируем
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public void saveToFile(String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(new ArrayList<>(teamsList));
            oos.writeObject(new ArrayList<>(matchesList));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void loadFromFile(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            List<Druzyna> loadedTeams   = (List<Druzyna>) ois.readObject();
            List<Mecz>    loadedMatches = (List<Mecz>)    ois.readObject();
            druzynyMap.clear();
            teamsList.clear();
            for (Druzyna d : loadedTeams) {
                druzynyMap.put(d.getNazwa(), d);
                teamsList.add(d);
            }
            matchesList.clear();
            matchesList.addAll(loadedMatches);
        }
    }
}
