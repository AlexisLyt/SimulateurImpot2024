package com.kerware.simulateur;

public class SimulateurImpot {

    private double abattement;
    private double nbPartsFoyer;

    public double calculer(int revenuNetDeclarant1, int revenuNetDeclarant2, SituationFamiliale situationFamiliale,
                           int nbEnfants, int nbEnfantsHandicapes, boolean parentIsole) {

        validateInputs(revenuNetDeclarant1, revenuNetDeclarant2, situationFamiliale, nbEnfants, nbEnfantsHandicapes, parentIsole);

        //abattement
        CalculateurAbattement calculateurAbattement = new CalculateurAbattement();
        if(situationFamiliale == SituationFamiliale.MARIE || situationFamiliale == SituationFamiliale.PACSE){
            abattement = calculateurAbattement.calculeAbattementCouple(revenuNetDeclarant1,revenuNetDeclarant2);
        } else{
            abattement = calculateurAbattement.calculeAbattement(revenuNetDeclarant1);
        }

        double revenuFiscal = Math.max(0, revenuNetDeclarant1 + revenuNetDeclarant2 - abattement);

        //parts
        CalculateurParts calculateurParts = new CalculateurParts();
        double nbPartsDeclarant = calculateurParts.getNbPartsDeclarants(situationFamiliale);
        nbPartsFoyer = calculateurParts.calculerParts(situationFamiliale, nbEnfants, nbEnfantsHandicapes, parentIsole);

        //tranches
        CalculateurTranche calculateurTranche = new CalculateurTranche();
        double impotDeclarant = calculateurTranche.calculerImpot(revenuFiscal / nbPartsDeclarant, Bareme.getTranchesRevenusImposables()) * nbPartsDeclarant;
        double impFoyer = calculateurTranche.calculerImpot(revenuFiscal / nbPartsFoyer, Bareme.getTranchesRevenusImposables()) * nbPartsFoyer;

        double baisse = impotDeclarant - impFoyer;
        double plafond = ((nbPartsFoyer - nbPartsDeclarant) / 0.5) * 1759;

        if (baisse > plafond) impFoyer = impotDeclarant - plafond;

        //decote
        CalculateurDecote calculateurDecote = new CalculateurDecote();
        double decote = calculateurDecote.calculerDecote(impFoyer, nbPartsDeclarant);

        double cehr = 0;
        if(situationFamiliale == SituationFamiliale.MARIE || situationFamiliale == SituationFamiliale.PACSE){
             cehr = calculateurTranche.calculerImpot(revenuFiscal,Bareme.getCEHRCouple());
        } else{
             cehr = calculateurTranche.calculerImpot(revenuFiscal,Bareme.getCEHRCelibataire());
        }

        /*double cehr = calculateurTranche.calculerImpot(
                revenuFiscal,
                (nbPartsDecl == 1) ? Bareme.getCEHRCelibataire() : Bareme.getCEHRCouple()
        );*/

        return Math.round(impFoyer - decote + cehr);
    }

    private void validateInputs(int revNetDecl1, int revNetDecl2, SituationFamiliale sitFam,
                                int nbEnfants, int nbEnfantsHandicapes, boolean parentIsole) {

        if (revNetDecl1 < 0 || revNetDecl2 < 0)
            throw new IllegalArgumentException("Revenus négatifs interdits");

        if (nbEnfants < 0 || nbEnfants > 7)
            throw new IllegalArgumentException("Nombre d'enfants invalide (0-7)");

        if (nbEnfantsHandicapes < 0 || nbEnfantsHandicapes > nbEnfants)
            throw new IllegalArgumentException("Nombre d'enfants handicapés invalide");

        if (sitFam == null)
            throw new IllegalArgumentException("Situation familiale obligatoire");

        if ((sitFam == SituationFamiliale.MARIE || sitFam == SituationFamiliale.PACSE) && parentIsole)
            throw new IllegalArgumentException("Un parent isolé ne peut pas être marié/pacsé");

        if ((sitFam == SituationFamiliale.CELIBATAIRE || sitFam == SituationFamiliale.DIVORCE || sitFam == SituationFamiliale.VEUF)
                && revNetDecl2 > 0)
            throw new IllegalArgumentException("Déclarant 2 ne doit pas avoir de revenu dans cette situation");
    }
}
