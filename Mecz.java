
public abstract class Mecz {
    protected Druzyna gospodarze;
    protected Druzyna goscie;
    protected int bramkiGospodarzy;
    protected int bramkiGosci;

    public Mecz(Druzyna gospodarze, Druzyna goscie) {
        this.gospodarze = gospodarze;
        this.goscie = goscie;
    }

    public abstract void rozegranoMecz(int bramkiGospodarzy, int bramkiGosci);
}
