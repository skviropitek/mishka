
public class MeczPilkaNozna extends Mecz {
    public MeczPilkaNozna(Druzyna gospodarze, Druzyna goscie) {
        super(gospodarze, goscie);
    }

    @Override
    public void rozegranoMecz(int bramkiGospodarzy, int bramkiGosci) {
        this.bramkiGospodarzy = bramkiGospodarzy;
        this.bramkiGosci = bramkiGosci;
        gospodarze.dodajWynik(bramkiGospodarzy, bramkiGosci);
        goscie.dodajWynik(bramkiGosci, bramkiGospodarzy);
    }
}
