import java.io.Serializable;

public class Druzyna extends AbstractEntity implements Serializable {
    private final String nazwa;
    private final String trener;
    private int punkty;
    private int bramkiZdobyte;
    private int bramkiStracone;

    public Druzyna(String nazwa, String trener) {
        this.nazwa = nazwa;
        this.trener = trener;
    }

    public Druzyna(String nazwa) {
        this(nazwa, "Unknown");
    }

    public Druzyna(String nazwa, String trener, int initialPoints) {
        this(nazwa, trener);
        this.punkty = initialPoints;
    }

    public String getNazwa() { return nazwa; }
    public String getTrener() { return trener; }
    public int getPunkty() { return punkty; }
    public int getBramkiZdobyte() { return bramkiZdobyte; }
    public int getBramkiStracone() { return bramkiStracone; }
    public int getRoznicaBramkowa() { return bramkiZdobyte - bramkiStracone; }

    public void dodajWynik(int zdobyte, int stracone) throws InvalidScoreException {
        if (zdobyte < 0 || stracone < 0) {
            throw new InvalidScoreException("Количество голов не может быть отрицательным");
        }
        bramkiZdobyte += zdobyte;
        bramkiStracone += stracone;
        if (zdobyte > stracone) punkty += 3;
        else if (zdobyte == stracone) punkty += 1;
    }

    @Override
    public String getSummary() {
        return String.format("%s — %d pkt (%d:%d)", nazwa, punkty, bramkiZdobyte, bramkiStracone);
    }

    public void resetStats() {
        this.punkty = 0;
        this.bramkiZdobyte = 0;
        this.bramkiStracone = 0;
    }

    @Override
    public String toString() {
        return nazwa;
    }
}
