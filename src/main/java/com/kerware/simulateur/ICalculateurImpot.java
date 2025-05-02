package com.kerware.simulateur;

public interface ICalculateurImpot {

    void setRevenusNetDeclarant1(int rn);
    void setRevenusNetDeclarant2(int rn);
    void setSituationFamiliale( SituationFamiliale sf );
    void setNbEnfantsACharge( int nbe );
    void setNbEnfantsSituationHandicap( int nbesh );
    void setParentIsole( boolean pi );

    void calculImpotSurRevenuNet();

    int getAbattement();
    double getNbPartsFoyerFiscal();

    int getImpotSurRevenuNet();

}
