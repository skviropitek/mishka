import java.util.List;

public class MeczPilkaNozna extends Mecz {
    public MeczPilkaNozna(Druzyna gospodarze, Druzyna goscie) {
        super(gospodarze, goscie);
    }

    @Override
    public void rozegranoMecz(
            int brG, int brO,
            int kartG, int kartO,
            List<Boolean> seqG, List<Boolean> seqO
    ) throws InvalidScoreException {
        // Проверка корректности серии пенальти
        if (seqG == null || seqO == null ||
                seqG.size() != seqO.size() || seqG.size() > 5) {
            throw new InvalidScoreException("Серия пенальти должна быть из 1–5 бросков");
        }
        // Записываем данные
        this.bramkiGospodarzy   = brG;
        this.bramkiGosci        = brO;
        this.kartkiGospodarzy   = kartG;
        this.kartkiGosci        = kartO;
        this.penaltySeqGospodarzy = List.copyOf(seqG);
        this.penaltySeqGosci      = List.copyOf(seqO);

        // Обновляем статистику по обычным голам
        gospodarze.dodajWynik(brG, brO);
        goscie.   dodajWynik(brO, brG);
    }

    @Override
    public String getSummary() {
        String s = String.format(
                "%s %d:%d %s (kartki %d:%d)",
                gospodarze, bramkiGospodarzy, bramkiGosci, goscie,
                kartkiGospodarzy, kartkiGosci
        );
        if (!penaltySeqGospodarzy.isEmpty()) {
            int scoredG = (int) penaltySeqGospodarzy.stream().filter(b->b).count();
            int scoredO = (int) penaltySeqGosci.stream().filter(b->b).count();
            s += String.format(" [karne %d:%d]", scoredG, scoredO);
        }
        return s;
    }
}
