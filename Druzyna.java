
public class Druzyna {
    private final String nazwa;
    private final String trener;
    private int punkty;
    private int bramkiZdobyte;
    private int bramkiStracone;

    public Druzyna(String nazwa, String trener) {
        this.nazwa = nazwa;
        this.trener = trener;
    }

    public String getNazwa() { return nazwa; }
    public String getTrener() { return trener; }
    public int getPunkty() { return punkty; }
    public int getBramkiZdobyte() { return bramkiZdobyte; }
    public int getBramkiStracone() { return bramkiStracone; }
    public int getRoznicaBramkowa() { return bramkiZdobyte - bramkiStracone; }

    public void dodajWynik(int zdobyte, int stracone) {
        bramkiZdobyte += zdobyte;
        bramkiStracone += stracone;
        if (zdobyte > stracone) punkty += 3;
        else if (zdobyte == stracone) punkty += 1;
    }

    @Override
    public String toString() {
        return nazwa;
    }
}
