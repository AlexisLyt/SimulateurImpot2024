package com.kerware.simulateur;

import java.util.List;

public class Bareme {

    private static final int SEPTRANCHE1 = 11294;
    private static final int SEPTRANCHE2 = 28797;
    private static final int SEPTRANCHE3 = 82341;
    private static final int SEPTRANCHE4 = 177106;
    private static final double TXIMPOT1 = 0.11;
    private static final double TXIMPOT2 = 0.30;
    private static final double TXIMPOT3 = 0.41;
    private static final double TXIMPOT4 = 0.45;

    /**
     * Retourne une liste contenant toutes les tranches d'impositions pour
     * les revenus imposables
     *
     * @return la liste de tranches d'imposistions
     */
    public static List<TrancheImposition> getTranchesRevenusImposables() {
        return List.of(
                new TrancheImposition(0, SEPTRANCHE1, 0.0),
                new TrancheImposition(SEPTRANCHE1, SEPTRANCHE2, TXIMPOT1),
                new TrancheImposition(SEPTRANCHE2, SEPTRANCHE3, TXIMPOT2),
                new TrancheImposition(SEPTRANCHE3, SEPTRANCHE4, TXIMPOT3),
                new TrancheImposition(SEPTRANCHE4, Integer.MAX_VALUE, TXIMPOT4)
        );
    }

    private static final int SEPTRANCHECEHR1 = 250_000;
    private static final int SEPTRANCHECEHR2 = 500_000;
    private static final int SEPTRANCHECEHR3 = 1_000_000;
    private static final double TXIMPOTCEHR1 = 0.03;
    private static final double TXIMPOTCEHR2 = 0.04;

    /**
     * Retourne une liste contenant toutes les tranches d'impositions pour
     * la contribution exceptionnelle sur les hauts revenus (CEHR) pour les celibataires
     *
     * @return la liste de tranches d'imposistions
     */
    public static List<TrancheImposition> getCEHRCelibataire() {
        return List.of(
                new TrancheImposition(0, SEPTRANCHECEHR1, 0.0),
                new TrancheImposition(SEPTRANCHECEHR1, SEPTRANCHECEHR2, TXIMPOTCEHR1),
                new TrancheImposition(SEPTRANCHECEHR2, SEPTRANCHECEHR3, TXIMPOTCEHR2),
                new TrancheImposition(SEPTRANCHECEHR3, Integer.MAX_VALUE, TXIMPOTCEHR2)
        );
    }



    /**
     * Retourne une liste contenant toutes les tranches d'impositions pour
     * la contribution exceptionnelle sur les hauts revenus (CEHR) pour les couples
     *
     * @return la liste de tranches d'imposistions
     */
    public static List<TrancheImposition> getCEHRCouple() {
        return List.of(
                new TrancheImposition(0, SEPTRANCHECEHR1, 0.0),
                new TrancheImposition(SEPTRANCHECEHR1, SEPTRANCHECEHR2, 0.0),
                new TrancheImposition(SEPTRANCHECEHR2, SEPTRANCHECEHR3, TXIMPOTCEHR1),
                new TrancheImposition(SEPTRANCHECEHR3, Integer.MAX_VALUE, TXIMPOTCEHR2)
        );
    }
}
