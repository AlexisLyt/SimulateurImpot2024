package com.kerware.simulateur;

import java.util.List;

public class CalculateurTranche {
    /**
     * Retourne le montant de l'impot calculé à partir du revenu imposable
     * et de la liste des tranches
     * @param revenu le revenu imposable du déclarant
     * @param tranches la liste des tranches
     * @return le montant de l'impot
     */

    public  double calculerImpot(double revenu, List<TrancheImposition> tranches) {
        double impot = 0;
        for (TrancheImposition tranche : tranches) {
            if (revenu > tranche.revenuMin()) {
                double revenuDansTranche =
                        Math.min(revenu, tranche.revenuMax()) - tranche.revenuMin();
                impot += revenuDansTranche * tranche.tauxImposition();
            } else {
                break;
            }
        }
        return impot;
    }
}
