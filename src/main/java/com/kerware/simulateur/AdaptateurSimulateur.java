package com.kerware.simulateur;

public class AdaptateurSimulateur implements ICalculateurImpot {

    private final SimulateurImpot simulateurImpot = new SimulateurImpot();

    private double result = 0;

    private int revenusNetDecl1 = 0;
    private int revenusNetDecl2 = 0;
    private SituationFamiliale situationFamiliale;
    private int nbEnfantsACharge;
    private int nbEnfantsSituationHandicap;
    private boolean parentIsole;

    /**
     * Définir le revenu net du déclarant 1
     * @param rn le revenu net du déclarant 1
     */
    @Override
    public void setRevenusNetDeclarant1(int rn) {
        this.revenusNetDecl1 = rn;
    }
    /**
     * Définir le revenu net du déclarant 2
     * @param rn le revenu net du déclarant 2
     */
    @Override
    public void setRevenusNetDeclarant2(int rn) {
        this.revenusNetDecl2 = rn;
    }
    /**
     * Définir la situation familiale
     * @param sf la situation familiale
     */
    @Override
    public void setSituationFamiliale(SituationFamiliale sf) {
        this.situationFamiliale = sf;
    }
    /**
     * Définir le nombre d'enfants à charge
     * @param nbe le nombre d'enfants à charge
     */
    @Override
    public void setNbEnfantsACharge(int nbe) {
        this.nbEnfantsACharge = nbe;
    }
    /**
     * Définir le nombre d'enfants handicapés
     * @param nbesh le nombre d'enfants handicapés
     */
    @Override
    public void setNbEnfantsSituationHandicap(int nbesh) {
        this.nbEnfantsSituationHandicap = nbesh;
    }
    /**
     * Définir si le parent est isolé
     * @param pi si le parent est isolé
     */
    @Override
    public void setParentIsole(boolean pi) {
        this.parentIsole = pi;
    }
    /**
     * Calcul l'impôt sur le revenu net
     */
    @Override
    public void calculImpotSurRevenuNet() {
         result = simulateurImpot.calculer(
                 revenusNetDecl1,
                 revenusNetDecl2,
                 situationFamiliale,
                 nbEnfantsACharge,
                 nbEnfantsSituationHandicap,
                 parentIsole);
    }

    /**
     * Récupérer l'abattement
     * @return l'abattement
     */
    @Override
    public int getAbattement() {
        return (int)simulateurImpot.getAbattement();
    }
    /**
     * Récupérer le plafond de l'avantage fiscal
     * @return le plafond de l'avantage fiscal
     */
    @Override
    public double getNbPartsFoyerFiscal() {
        return simulateurImpot.getNbPartsFoyer();
    }
    /**
     * Récupérer le montant de l'impôt sur le revenu net
     * @return le montant de l'impôt sur le revenu net
     */
    @Override
    public int getImpotSurRevenuNet() {
        return (int)result;
    }
}
