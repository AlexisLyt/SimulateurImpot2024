package com.kerware.simulateur;

import java.util.List;

public class Bareme {
    /**
     * Retourne une liste contenant toutes les tranches d'impositions pour
     * les revenus imposables
     *
     * @return la liste de tranches d'imposistions
     */
    public static List<TrancheImposition> getTranchesRevenusImposables() {
        return List.of(
                new TrancheImposition(0, 11294, 0.0),
                new TrancheImposition(11294, 28797, 0.11),
                new TrancheImposition(28797, 82341, 0.30),
                new TrancheImposition(82341, 177106, 0.41),
                new TrancheImposition(177106, Integer.MAX_VALUE, 0.45)
        );
    }
    /**
     * Retourne une liste contenant toutes les tranches d'impositions pour
     * la contribution exceptionnelle sur les hauts revenus (CEHR) pour les celibataires
     *
     * @return la liste de tranches d'imposistions
     */
    public static List<TrancheImposition> getCEHRCelibataire() {
        return List.of(
                new TrancheImposition(0, 250000, 0.0),
                new TrancheImposition(250000, 500000, 0.03),
                new TrancheImposition(500000, 1000000, 0.04),
                new TrancheImposition(1000000, Integer.MAX_VALUE, 0.04)
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
                new TrancheImposition(0, 250000, 0.0),
                new TrancheImposition(250000, 500000, 0.0),
                new TrancheImposition(500000, 1000000, 0.03),
                new TrancheImposition(1000000, Integer.MAX_VALUE, 0.04)
        );
    }
}
