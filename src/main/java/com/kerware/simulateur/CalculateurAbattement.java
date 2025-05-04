package com.kerware.simulateur;

public class CalculateurAbattement {

    private static final double TAUX_ABATTEMENT = 0.1;
    private static final int ABATTEMENT_MAXIMAL = 14171;
    private static final int ABATTEMENT_MINIMAL = 495;

    /**
     * Calcule l'abattement applicable à un revenu net entré en paramètre
     * @param revenuNetDeclarant le revenu net du déclarant
     * @return l'abattement calculé
     */
    public int calculeAbattement(int revenuNetDeclarant){
        int abattement = (int) Math.round(revenuNetDeclarant * TAUX_ABATTEMENT);
        abattement = Math.min(abattement, ABATTEMENT_MAXIMAL);
        abattement = Math.max(abattement, ABATTEMENT_MINIMAL);
        return abattement;

    }

    /**
     * Calcule l'abattement applicable aux revenus d'un couple
     * @param revenuNetDeclarant1 le revenu net du déclarant 1
     * @param revenuNetDeclarant2 le revenu net du déclarant 2
     * @return l'abattement calculé pour un déclarant en couple
     */
    public int calculeAbattementCouple(int revenuNetDeclarant1,int revenuNetDeclarant2){
        return calculeAbattement(revenuNetDeclarant1) + calculeAbattement(revenuNetDeclarant2);
    }
}
