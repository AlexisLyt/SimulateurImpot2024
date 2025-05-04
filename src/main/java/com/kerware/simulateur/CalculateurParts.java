package com.kerware.simulateur;

public class CalculateurParts {

    private static final int LIMITE_PREMIERS_ENFANT_A_CHARGE = 2;
    private static final double PART_PREMIER_ENFANT = 0.5;
    private static final double PART_ENFANT = 1.0;
    private static final double PART_ENFANT_HANDICAPE = 0.5;

    private static final double PART_DECLARANT = 1.0;
    private static final double PART_DECLARANT_EN_COUPLE = 2.0;

    private static final double PART_ENFANTS_PARENT_ISOLE = 0.5;
    private static final double PART_ENFANTS_PARENT_VEUF = 1.0;

    /**
     * Retourne le nombre de parts lié à la situation familiale
     * @param situation la situation familiale des déclarants
     * @return nb parts
     */
    public double getNbPartsDeclarants(SituationFamiliale situation) {
        return switch (situation) {
            case MARIE, PACSE -> PART_DECLARANT_EN_COUPLE;
            case CELIBATAIRE, DIVORCE, VEUF -> PART_DECLARANT;
        };
    }

    /**
     * Calcule le nombre de parts d'un déclarant
     * @param situation la situation familiale des déclarants
     * @param nbEnfants le nombre d'enfants
     * @param nbEnfantsHandicapes le nombre d'enfants handicapés
     * @param parentIsole si le parent est isolé
     * @return le nombre de parts final
     */
    public double calculerParts(
            SituationFamiliale situation,
            int nbEnfants,
            int nbEnfantsHandicapes,
            boolean parentIsole) {
        //parts déclarants
        double parts = getNbPartsDeclarants(situation);

        //parts enfants à charge
        if(nbEnfants > 0 && nbEnfants <= LIMITE_PREMIERS_ENFANT_A_CHARGE){
            parts += nbEnfants * PART_PREMIER_ENFANT;
        } else if( nbEnfants > LIMITE_PREMIERS_ENFANT_A_CHARGE){
            parts += (PART_PREMIER_ENFANT * LIMITE_PREMIERS_ENFANT_A_CHARGE)
                    + (PART_ENFANT * (nbEnfants - LIMITE_PREMIERS_ENFANT_A_CHARGE));
        }

        // parent isolé
        if (parentIsole && nbEnfants > 0) {
            parts += PART_ENFANTS_PARENT_ISOLE;
        }
        // Veuf avec enfant
        if (situation == SituationFamiliale.VEUF && nbEnfants > 0) {
            parts += PART_ENFANTS_PARENT_VEUF;
        }

        // enfant handicapé
        if(nbEnfantsHandicapes > 0){
            parts += nbEnfantsHandicapes * PART_ENFANT_HANDICAPE;
        }

        return parts;
    }


}
