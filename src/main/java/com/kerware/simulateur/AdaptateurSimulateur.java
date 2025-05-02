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


    @Override
    public void setRevenusNetDeclarant1(int rn) {
        this.revenusNetDecl1 = rn;
    }

    @Override
    public void setRevenusNetDeclarant2(int rn) {
        this.revenusNetDecl2 = rn;
    }

    @Override
    public void setSituationFamiliale(SituationFamiliale sf) {
        this.situationFamiliale = sf;
    }

    @Override
    public void setNbEnfantsACharge(int nbe) {
        this.nbEnfantsACharge = nbe;
    }

    @Override
    public void setNbEnfantsSituationHandicap(int nbesh) {
        this.nbEnfantsSituationHandicap = nbesh;
    }

    @Override
    public void setParentIsole(boolean pi) {
        this.parentIsole = pi;
    }

    @Override
    public void calculImpotSurRevenuNet() {
         result = simulateurImpot.calculer(revenusNetDecl1, revenusNetDecl2 ,situationFamiliale, nbEnfantsACharge, nbEnfantsSituationHandicap, parentIsole);
    }

    @Override
    public int getAbattement() {
        return (int)simulateurImpot.getAbattement();
    }

    @Override
    public double getNbPartsFoyerFiscal() {
        return simulateurImpot.getNbPartsFoyer();
    }

    @Override
    public int getImpotSurRevenuNet() {
        return (int)result;
    }
}
