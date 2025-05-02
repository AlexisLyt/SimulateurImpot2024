package com.kerware.simulateur;

public class CalculateurAbattement {

    private final double TauxAbatement = 0.1;
    private final int AbattementMaximal = 14171;
    private final int AbattementMinimal = 495;

    /**
     * Calcule l'abattement applicable à un revenu net entré en paramètre
     * @param revenuNetDeclarant le revenu net du déclarant
     * @return l'abattement calculé
     */
    public int calculeAbattement(int revenuNetDeclarant){
        int abattement = (int) Math.round(revenuNetDeclarant * TauxAbatement);
        abattement = Math.min(abattement, AbattementMaximal);
        abattement = Math.max(abattement, AbattementMinimal);
        return abattement;

    }

    /**
     * calcule l'abattement applicable aux revenus d'un couple
     * @param revenuNetDeclarant1
     * @param revenuNetDeclarant2
     * @return l'abattement calculé pour un déclarant en couple
     */
    public int calculeAbattementCouple(int revenuNetDeclarant1,int revenuNetDeclarant2){
        return calculeAbattement(revenuNetDeclarant1) + calculeAbattement(revenuNetDeclarant2);
    }
}
