package com.kerware.simulateur;

public class SimulateurImpot {

    private double plafondAvantageFiscal = 1759;

    private double abattement;
    private double nbPartsFoyer;

    public double getAbattement(){
        return abattement;
    }

    public double getNbPartsFoyer(){
        return nbPartsFoyer;
    }

    /**
     * Calcule le montant de l'impot a payer
     * @param revenuNetDeclarant1 les revenus net du déclarant 1
     * @param revenuNetDeclarant2 les revenus net du déclarant 2
     * @param situationFamiliale la situation familiale des déclarants
     * @param nbEnfants le nombre d'enfants
     * @param nbEnfantsHandicapes le nombre d'enfants handicapés
     * @param parentIsole si le parent est isolé
     * @return le montant de l'impot a payer
     */
    public double calculer(int revenuNetDeclarant1, int revenuNetDeclarant2, SituationFamiliale situationFamiliale,
                           int nbEnfants, int nbEnfantsHandicapes, boolean parentIsole) {

        validateInputs(revenuNetDeclarant1, revenuNetDeclarant2, situationFamiliale, nbEnfants, nbEnfantsHandicapes, parentIsole);
        System.out.println("--------------------------------------------------");
        System.out.println( "Revenu net declarant1 : " + revenuNetDeclarant1);
        System.out.println( "Revenu net declarant2 : " + revenuNetDeclarant2);
        System.out.println( "Situation familiale : " + situationFamiliale.name() );

        //abattement
        // EXIGENCE : EXG_IMPOT_02
        CalculateurAbattement calculateurAbattement = new CalculateurAbattement();
        if(situationFamiliale == SituationFamiliale.MARIE || situationFamiliale == SituationFamiliale.PACSE){
            abattement = calculateurAbattement.calculeAbattementCouple(revenuNetDeclarant1,revenuNetDeclarant2);
        } else{
            abattement = calculateurAbattement.calculeAbattement(revenuNetDeclarant1);
        }
        System.out.println( "Abattement : " + abattement );
        // revenu fiscal de référence
        double revenuFiscal = Math.max(0, revenuNetDeclarant1 + revenuNetDeclarant2 - abattement);
        System.out.println( "Revenu fiscal de référence : " + revenuFiscal);

        //parts
        // EXIG : EXG_IMPOT_03
        CalculateurParts calculateurParts = new CalculateurParts();
        double nbPartsDeclarant = calculateurParts.getNbPartsDeclarants(situationFamiliale);
        nbPartsFoyer = calculateurParts.calculerParts(situationFamiliale, nbEnfants, nbEnfantsHandicapes, parentIsole);
        System.out.println( "Nombre d'enfants  : " + nbEnfants);
        System.out.println( "Nombre d'enfants handicapés : " + nbEnfantsHandicapes);
        System.out.println( "Parent isolé : " + parentIsole);
        System.out.println( "Nombre de parts : " + nbPartsFoyer );

        //tranches
        // Calcul impôt des declarants
        // EXIGENCE : EXG_IMPOT_04
        // revenu imposable
        CalculateurTranche calculateurTranche = new CalculateurTranche();
        double impotDeclarant = calculateurTranche.calculerImpot(revenuFiscal / nbPartsDeclarant, Bareme.getTranchesRevenusImposables()) * nbPartsDeclarant;
        double impFoyer = calculateurTranche.calculerImpot(revenuFiscal / nbPartsFoyer, Bareme.getTranchesRevenusImposables()) * nbPartsFoyer;
        System.out.println( "Impôt brut des déclarants : " + impotDeclarant);
        System.out.println( "Impôt brut du foyer fiscal complet : " + impFoyer );

        // Vérification de la baisse d'impôt autorisée
        // EXIGENCE : EXG_IMPOT_05
        // baisse impot
        double baisse = impotDeclarant - impFoyer;
        // Plafond de baisse maximal par demi part
        double plafond = ((nbPartsFoyer - nbPartsDeclarant) / 0.5) * plafondAvantageFiscal;
        System.out.println( "Baisse d'impôt : " + baisse );
        System.out.println( "Plafond de baisse autorisée " + plafond );


        if (baisse > plafond) impFoyer = impotDeclarant - plafond;
        System.out.println( "Impôt brut après plafonnement avant decote : " + impFoyer );

        //decote
        // EXIGENCE : EXG_IMPOT_06
        CalculateurDecote calculateurDecote = new CalculateurDecote();
        double decote = calculateurDecote.calculerDecote(impFoyer, nbPartsDeclarant);

        // EXIGENCE : EXG_IMPOT_07 :
        // Contribution exceptionnelle sur les hauts revenus
        double cehr = 0;
        if(situationFamiliale == SituationFamiliale.MARIE || situationFamiliale == SituationFamiliale.PACSE){
             cehr = calculateurTranche.calculerImpot(revenuFiscal,Bareme.getCEHRCouple());
        } else{
             cehr = calculateurTranche.calculerImpot(revenuFiscal,Bareme.getCEHRCelibataire());
        }
        System.out.println( "Contribution exceptionnelle sur les hauts revenus : " + cehr);

        System.out.println( "Impôt sur le revenu net final : " + Math.round(impFoyer - decote + cehr) + " " + (impFoyer - decote + cehr) );
        return Math.round(impFoyer - decote + cehr);
    }

    /**
     * Verifie la validité des informations entrée
     * @param revenuNetDeclarant1 les revenus net du déclarant 1
     * @param revenuNetDeclarant2 les revenus net du déclarant 2
     * @param situationFamiliale la situation familiale des déclarants
     * @param nbEnfants le nombre d'enfants
     * @param nbEnfantsHandicapes le nombre d'enfants handicapés
     * @param parentIsole si le parent est isolé
     * @return le montant de l'impot a payer
     */
    private void validateInputs(int revenuNetDeclarant1, int revenuNetDeclarant2, SituationFamiliale situationFamiliale,
                                int nbEnfants, int nbEnfantsHandicapes, boolean parentIsole) {

        if (revenuNetDeclarant1 < 0 || revenuNetDeclarant2 < 0)
            throw new IllegalArgumentException("Revenus négatifs interdits");

        if (nbEnfants < 0 || nbEnfants > 7)
            throw new IllegalArgumentException("Nombre d'enfants invalide (0-7)");

        if (nbEnfantsHandicapes < 0 || nbEnfantsHandicapes > nbEnfants)
            throw new IllegalArgumentException("Nombre d'enfants handicapés invalide");

        if (situationFamiliale == null)
            throw new IllegalArgumentException("Situation familiale obligatoire");

        if ((situationFamiliale == SituationFamiliale.MARIE || situationFamiliale == SituationFamiliale.PACSE) && parentIsole)
            throw new IllegalArgumentException("Un parent isolé ne peut pas être marié/pacsé");

        if ((situationFamiliale == SituationFamiliale.CELIBATAIRE || situationFamiliale == SituationFamiliale.DIVORCE || situationFamiliale == SituationFamiliale.VEUF)
                && revenuNetDeclarant2 > 0)
            throw new IllegalArgumentException("Déclarant 2 ne doit pas avoir de revenu dans cette situation");
    }
}
