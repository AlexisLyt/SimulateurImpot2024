package com.kerware.simulateur;

public class SimulateurImpot {

    private static final double PLAFOND_AVANTAGE_FISCAL = 1759;
    private static final double PLAFOND_CST = 0.5;

    private double abattement;
    private double nbPartsFoyer;

    /**
     * Renvoie l'abattement
     * @return l'abattement
     */
    public double getAbattement(){
        return abattement;
    }

    /**
     * Renvoie le nombre de parts du foyer
     * @return le nombre de parts du foyer
     */
    public double getNbPartsFoyer(){
        return nbPartsFoyer;
    }

    private double calculerAbattement(
            int revenuNetDeclarant1,
            int revenuNetDeclarant2,
            SituationFamiliale situationFamiliale) {
        CalculateurAbattement calculateurAbattement = new CalculateurAbattement();
        if (situationFamiliale == SituationFamiliale.MARIE ||
                situationFamiliale == SituationFamiliale.PACSE) {
            return calculateurAbattement.calculeAbattementCouple(
                    revenuNetDeclarant1, revenuNetDeclarant2);
        } else {
            return calculateurAbattement.calculeAbattement(revenuNetDeclarant1);
        }
    }

    private double calculerNbParts(
            SituationFamiliale situationFamiliale,
            int nbEnfants, int nbEnfantsHandicapes, boolean parentIsole) {
        CalculateurParts calculateurParts = new CalculateurParts();
        return calculateurParts.calculerParts(
                situationFamiliale, nbEnfants, nbEnfantsHandicapes, parentIsole);
    }

    private double calculerImpotBrut(double revenuFiscal, double nbParts, double nbPartsDeclarant) {
        CalculateurTranche calculateurTranche = new CalculateurTranche();
        return calculateurTranche.calculerImpot(
                revenuFiscal / nbParts, Bareme.getTranchesRevenusImposables()) * nbPartsDeclarant;
    }

    private double appliquerPlafondBaisse(
            double impotDeclarant, double impFoyer,
            double nbPartsParFoyer, double nbPartsDeclarant) {
        double baisse = impotDeclarant - impFoyer;
        double plafond = ((nbPartsParFoyer - nbPartsDeclarant) / PLAFOND_CST)
                * PLAFOND_AVANTAGE_FISCAL;
        if (baisse > plafond) {
            return impotDeclarant - plafond;
        }
        return impFoyer;
    }

    private double calculerCEHR(double revenuFiscal, SituationFamiliale situationFamiliale) {
        CalculateurTranche calculateurTranche = new CalculateurTranche();
        if (situationFamiliale == SituationFamiliale.MARIE ||
                situationFamiliale == SituationFamiliale.PACSE) {
            return calculateurTranche.calculerImpot(revenuFiscal, Bareme.getCEHRCouple());
        } else {
            return calculateurTranche.calculerImpot(revenuFiscal, Bareme.getCEHRCelibataire());
        }
    }
    /**
     * Calcule le montant de l'impot à payer
     * @param revenuNetDeclarant1 les revenus nets du déclarant 1
     * @param revenuNetDeclarant2 les revenus nets du déclarant 2
     * @param situationFamiliale la situation familiale des déclarants
     * @param nbEnfants le nombre d'enfants
     * @param nbEnfantsHandicapes le nombre d'enfants handicapés
     * @param parentIsole si le parent est isolé
     * @return le montant de l'impot a payer
     */
    public double calculer(int revenuNetDeclarant1, int revenuNetDeclarant2,
                           SituationFamiliale situationFamiliale,
                           int nbEnfants, int nbEnfantsHandicapes, boolean parentIsole) {

        validateInputs(
                revenuNetDeclarant1, revenuNetDeclarant2, situationFamiliale,
                nbEnfants, nbEnfantsHandicapes, parentIsole);

        abattement = calculerAbattement(
                revenuNetDeclarant1, revenuNetDeclarant2, situationFamiliale);
        double revenuFiscal = Math.max(0, revenuNetDeclarant1 + revenuNetDeclarant2 - abattement);

        nbPartsFoyer = calculerNbParts(
                situationFamiliale, nbEnfants, nbEnfantsHandicapes, parentIsole);
        double nbPartsDeclarant = new CalculateurParts().getNbPartsDeclarants(situationFamiliale);

        double impotDeclarant = calculerImpotBrut(revenuFiscal, nbPartsDeclarant, nbPartsDeclarant);
        double impFoyer = calculerImpotBrut(revenuFiscal, nbPartsFoyer, nbPartsFoyer);

        impFoyer = appliquerPlafondBaisse(impotDeclarant, impFoyer, nbPartsFoyer, nbPartsDeclarant);

        double decote = new CalculateurDecote().calculerDecote(impFoyer, nbPartsDeclarant);
        double cehr = calculerCEHR(revenuFiscal, situationFamiliale);

        return Math.round(impFoyer - decote + cehr);
    }

    private static final int LIMITE_NB_ENFANTS = 7;

    /**
     * Verifie la validité des informations entrée
     * @param revenuNetDeclarant1 les revenus nets du déclarant 1
     * @param revenuNetDeclarant2 les revenus nets du déclarant 2
     * @param situationFamiliale la situation familiale des déclarants
     * @param nbEnfants le nombre d'enfants
     * @param nbEnfantsHandicapes le nombre d'enfants handicapés
     * @param parentIsole si le parent est isolé
     */
    private void validateInputs(int revenuNetDeclarant1, int revenuNetDeclarant2,
                                SituationFamiliale situationFamiliale,
                                int nbEnfants, int nbEnfantsHandicapes, boolean parentIsole) {

        if (revenuNetDeclarant1 < 0 || revenuNetDeclarant2 < 0) {
            throw new IllegalArgumentException("Revenus négatifs interdits");
        }

        if (nbEnfants < 0 || nbEnfants > LIMITE_NB_ENFANTS) {
            throw new IllegalArgumentException("Nombre d'enfants invalide (0-7)");
        }

        if (nbEnfantsHandicapes < 0 || nbEnfantsHandicapes > nbEnfants) {
            throw new IllegalArgumentException("Nombre d'enfants handicapés invalide");
        }

        if (situationFamiliale == null) {
            throw new IllegalArgumentException("Situation familiale obligatoire");
        }

        if ((situationFamiliale == SituationFamiliale.MARIE ||
                situationFamiliale == SituationFamiliale.PACSE) && parentIsole) {
            throw new IllegalArgumentException("Un parent isolé ne peut pas être marié/pacsé");
        }

        if ((situationFamiliale == SituationFamiliale.CELIBATAIRE ||
                situationFamiliale == SituationFamiliale.DIVORCE ||
                situationFamiliale == SituationFamiliale.VEUF)
                && revenuNetDeclarant2 > 0) {
            throw new IllegalArgumentException(
                    "Déclarant 2 ne doit pas avoir de revenu dans cette situation");
        }
    }
}
