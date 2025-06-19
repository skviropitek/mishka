import java.io.Serializable;
import java.util.List;

public abstract class Mecz implements Reportable, Serializable {
    protected Druzyna gospodarze;
    protected Druzyna goscie;
    protected int bramkiGospodarzy;
    protected int bramkiGosci;
    protected int kartkiGospodarzy;
    protected int kartkiGosci;
    protected List<Boolean> penaltySeqGospodarzy;
    protected List<Boolean> penaltySeqGosci;

    public Mecz(Druzyna gospodarze, Druzyna goscie) {
        this.gospodarze = gospodarze;
        this.goscie = goscie;
    }

    /**
     * @param bramkiGospodarzy liczba голов в основное время
     * @param bramkiGosci      то же для гостей
     * @param kartkiGospodarzy карточки хозяев
     * @param kartkiGosci      карточки гостей
     * @param seqGospodarzy    серия пенальти хозяев (size≤5)
     * @param seqGosci         то же для гостей
     */
    public abstract void rozegranoMecz(
            int bramkiGospodarzy, int bramkiGosci,
            int kartkiGospodarzy, int kartkiGosci,
            List<Boolean> seqGospodarzy, List<Boolean> seqGosci
    ) throws InvalidScoreException;

    public Druzyna getGospodarze() { return gospodarze; }
    public Druzyna getGoscie()    { return goscie; }
    public int getBramkiGospodarzy() { return bramkiGospodarzy; }
    public int getBramkiGosci()      { return bramkiGosci; }
    public int getKartkiGospodarzy() { return kartkiGospodarzy; }
    public int getKartkiGosci()      { return kartkiGosci; }
    public List<Boolean> getPenaltySeqGospodarzy() { return penaltySeqGospodarzy; }
    public List<Boolean> getPenaltySeqGosci()      { return penaltySeqGosci; }

    @Override
    public abstract String getSummary();
}
