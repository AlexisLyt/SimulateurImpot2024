package com.kerware.simulateur;

public class CalculateurParts {

    private final int limitePremiersEnfantsACharge = 2;
    private final double partPremierEnfant = 0.5;
    private final double partEnfant = 1.0;
    private final double partEnfantHandicape = 0.5;

    private final double partDeclarant = 1.0;
    private final double partDeclarantEnCouple = 2.0;

    private final double partEnfantsParentIsole = 0.5;
    private final double partEnfantsParentVeuf = 1.0;

    /**
     * retourne le nombre de part lié à la situation familiale
     * @param situation la situation familiale des déclarants
     * @return nb parts
     */
    public double getNbPartsDeclarants(SituationFamiliale situation) {
        return switch (situation) {
            case MARIE, PACSE -> partDeclarantEnCouple;
            case CELIBATAIRE, DIVORCE, VEUF -> partDeclarant;
        };
    }

    /**
     * Calcule le nombre de part d'un déclarant
     * @param situation la situation familiale des déclarants
     * @param nbEnfants le nombre d'enfants
     * @param nbEnfantsHandicapes le nombre d'enfants handicapés
     * @param parentIsole si le parent est isolé
     * @return le nombre de parts final
     */
    public double calculerParts(SituationFamiliale situation, int nbEnfants, int nbEnfantsHandicapes, boolean parentIsole) {
        //parts déclarants
        double parts = getNbPartsDeclarants(situation);

        //parts enfants à charge
        if(nbEnfants > 0 && nbEnfants <= limitePremiersEnfantsACharge){
            parts += nbEnfants * partPremierEnfant;
        } else if( nbEnfants > limitePremiersEnfantsACharge){
            parts += (partPremierEnfant * limitePremiersEnfantsACharge) + (partEnfant * (nbEnfants - limitePremiersEnfantsACharge ));
        }

        // parent isolé
        if (parentIsole && nbEnfants > 0) {
            parts += partEnfantsParentIsole;
        }
        // Veuf avec enfant
        if (situation == SituationFamiliale.VEUF && nbEnfants > 0) {
            parts += partEnfantsParentVeuf;
        }

        // enfant handicapé
        if(nbEnfantsHandicapes > 0){
            parts += nbEnfantsHandicapes * partEnfantHandicape;
        }

        return parts;
    }


}
