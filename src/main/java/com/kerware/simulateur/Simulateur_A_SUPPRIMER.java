package com.kerware.simulateur;

/**
 *  Cette classe permet de simuler le calcul de l'impôt sur le revenu
 *  en France pour l'année 2024 sur les revenus de l'année 2023 pour
 *  des cas simples de contribuables célibataires, mariés, divorcés, veufs
 *  ou pacsés avec ou sans enfants à charge ou enfants en situation de handicap
 *  et parent isolé.
 * <p>
 *  EXEMPLE DE CODE DE TRES MAUVAISE QUALITE FAIT PAR UN DEBUTANT
 * <p>
 *  Pas de lisibilité, pas de commentaires, pas de tests
 *  Pas de documentation, pas de gestion des erreurs
 *  Pas de logique métier, pas de modularité
 *  Pas de gestion des exceptions, pas de gestion des logs
 *  Principe "Single Responsability" non respecté
 *  Pas de traçabilité vers les exigences métier
 * <p>
 *  Pourtant, ce code fonctionne correctement.
 *  Il s'agit d'un "legacy" code qui est difficile à maintenir
 *  L'auteur n'a pas fourni de tests unitaires.
 **/

public class Simulateur_A_SUPPRIMER {


    private final int[] limites = new int[6];

    private final double[] taux = new double[5];

    private final int[] limitesCEHR = new int[5];

    private final double[] tauxCEHRCelibataire = new double[4];

    private final double[] tauxCEHRCouple = new double[4];

    // abattement
    private double abt = 0;

    // nombre de parts des déclarants
    private double nbPtsDecl = 0;
    // nombre de parts du foyer fiscal
    private double nbPts = 0;

    // impôt du foyer fiscal
    private double mImp = 0;

    // Getters pour adapter le code legacy pour les tests unitaires


    public double getAbattement() {
        return abt;
    }

    public double getNbParts() {
        return nbPts;
    }

    public double getImpotNet() {
        return mImp;
    }


    // Fonction de calcul de l'impôt sur le revenu net en France en 2024 sur les revenus 2023

    public void calculImpot(int revNetDecl1, int revNetDecl2, SituationFamiliale sitFam, int nbEnfants, int nbEnfantsHandicapes, boolean parentIsol) {

        // Préconditions
        if ( revNetDecl1  < 0 || revNetDecl2 < 0 ) {
            throw new IllegalArgumentException("Le revenu net ne peut pas être négatif");
        }

        if ( nbEnfants < 0 ) {
            throw new IllegalArgumentException("Le nombre d'enfants ne peut pas être négatif");
        }

        if ( nbEnfantsHandicapes < 0 ) {
            throw new IllegalArgumentException("Le nombre d'enfants handicapés ne peut pas être négatif");
        }

        if ( sitFam == null ) {
            throw new IllegalArgumentException("La situation familiale ne peut pas être null");
        }

        if ( nbEnfantsHandicapes > nbEnfants ) {
            throw new IllegalArgumentException("Le nombre d'enfants handicapés ne peut pas être supérieur au nombre d'enfants");
        }

        if ( nbEnfants > 7 ) {
            throw new IllegalArgumentException("Le nombre d'enfants ne peut pas être supérieur à 7");
        }

        if ( parentIsol && ( sitFam == SituationFamiliale.MARIE || sitFam == SituationFamiliale.PACSE ) ) {
            throw new IllegalArgumentException("Un parent isolé ne peut pas être marié ou pacsé");
        }

        boolean seul = sitFam == SituationFamiliale.CELIBATAIRE || sitFam == SituationFamiliale.DIVORCE || sitFam == SituationFamiliale.VEUF;
        if (  seul && revNetDecl2 > 0 ) {
            throw new IllegalArgumentException("Un célibataire, un divorcé ou un veuf ne peut pas avoir de revenu pour le déclarant 2");
        }

        // Initialisation des variables

        // revenu net

        // nb enfants
        // nb enfants handicapés
        // parent isolé

        // Les limites des tranches de revenus imposables
        int l00 = 0;
        int l01 = 11294;
        int l02 = 28797;
        int l03 = 82341;
        int l04 = 177106;
        int l05 = Integer.MAX_VALUE;
        limites[0] = l00;
        limites[1] = l01;
        limites[2] = l02;
        limites[3] = l03;
        limites[4] = l04;
        limites[5] = l05;

        // Les taux d'imposition par tranche
        double t00 = 0.0;
        double t01 = 0.11;
        double t02 = 0.3;
        double t03 = 0.41;
        double t04 = 0.45;
        taux[0] = t00;
        taux[1] = t01;
        taux[2] = t02;
        taux[3] = t03;
        taux[4] = t04;

        // Les limites des tranches pour la contribution exceptionnelle sur les hauts revenus
        int lce00 = 0;
        int lce01 = 250000;
        int lce02 = 500000;
        int lce03 = 1000000;
        int lce04 = Integer.MAX_VALUE;
        limitesCEHR[0] = lce00;
        limitesCEHR[1] = lce01;
        limitesCEHR[2] = lce02;
        limitesCEHR[3] = lce03;
        limitesCEHR[4] = lce04;

        // Les taux de la contribution exceptionnelle sur les hauts revenus pour les celibataires
        double tce00 = 0.0;
        tauxCEHRCelibataire[0] = tce00;
        double tce01 = 0.03;
        tauxCEHRCelibataire[1] = tce01;
        double tce02 = 0.04;
        tauxCEHRCelibataire[2] = tce02;
        double tce03 = 0.04;
        tauxCEHRCelibataire[3] = tce03;

        // Les taux de la contribution exceptionnelle sur les hauts revenus pour les couples
        double tce00C = 0.0;
        tauxCEHRCouple[0] = tce00C;
        double tce01C = 0.0;
        tauxCEHRCouple[1] = tce01C;
        double tce02C = 0.03;
        tauxCEHRCouple[2] = tce02C;
        double tce03C = 0.04;
        tauxCEHRCouple[3] = tce03C;

        System.out.println("--------------------------------------------------");
        System.out.println( "Revenu net declarant1 : " + revNetDecl1);
        System.out.println( "Revenu net declarant2 : " + revNetDecl2);
        System.out.println( "Situation familiale : " + sitFam.name() );

       /* // Abattement
        // EXIGENCE : EXG_IMPOT_02
        double tAbt = 0.1;
        long abt1 = Math.round(revNetDecl1 * tAbt);
        long abt2 = Math.round(revNetDecl2 * tAbt);

        // Abattement
        int lAbtMax = 14171;
        if (abt1 > lAbtMax) {
            abt1 = lAbtMax;
        }
        if ( sitFam == SituationFamiliale.MARIE || sitFam == SituationFamiliale.PACSE ) {
            if (abt2 > lAbtMax) {
                abt2 = lAbtMax;
            }
        }

        int lAbtMin = 495;
        if (abt1 < lAbtMin) {
            abt1 = lAbtMin;
        }

        if ( sitFam == SituationFamiliale.MARIE || sitFam == SituationFamiliale.PACSE ) {
            if (abt2 < lAbtMin) {
                abt2 = lAbtMin;
            }
        }

        abt = abt1 + abt2;
        System.out.println( "Abattement : " + abt );*/

        // revenu fiscal de référence
        double rFRef = revNetDecl1 + revNetDecl2 - abt;
        if ( rFRef < 0 ) {
            rFRef = 0;
        }

        System.out.println( "Revenu fiscal de référence : " + rFRef);


        // parts déclarants
        // EXIG : EXG_IMPOT_03
        switch ( sitFam ) {
            case CELIBATAIRE, DIVORCE, VEUF:
                nbPtsDecl = 1;
                break;
            case MARIE, PACSE:
                nbPtsDecl = 2;
                break;
        }

        System.out.println( "Nombre d'enfants  : " + nbEnfants);
        System.out.println( "Nombre d'enfants handicapés : " + nbEnfantsHandicapes);

        // parts enfants à charge
        if ( nbEnfants <= 2 ) {
            nbPts = nbPtsDecl + nbEnfants * 0.5;
        } else {
            nbPts = nbPtsDecl+  1.0 + ( nbEnfants - 2 );
        }

        // parent isolé

        System.out.println( "Parent isolé : " + parentIsol);

        if (parentIsol) {
            if ( nbEnfants > 0 ){
                nbPts = nbPts + 0.5;
            }
        }

        // Veuf avec enfant
        if ( sitFam == SituationFamiliale.VEUF && nbEnfants > 0 ) {
            nbPts = nbPts + 1;
        }

        // enfant handicapé
        nbPts = nbPts + nbEnfantsHandicapes * 0.5;

        System.out.println( "Nombre de parts : " + nbPts );

        // EXIGENCE : EXG_IMPOT_07 :
        // Contribution exceptionnelle sur les hauts revenus
        // Contribution exceptionnelle sur les hauts revenus
        double contribExceptionnelle = 0;
        int i = 0;
        do {
            if ( rFRef >= limitesCEHR[i] && rFRef < limitesCEHR[i+1] ) {
                if ( nbPtsDecl == 1 ) {
                    contribExceptionnelle += ( rFRef - limitesCEHR[i] ) * tauxCEHRCelibataire[i];
                } else {
                    contribExceptionnelle += ( rFRef - limitesCEHR[i] ) * tauxCEHRCouple[i];
                }
                break;
            } else {
                if ( nbPtsDecl == 1 ) {
                    contribExceptionnelle += ( limitesCEHR[i+1] - limitesCEHR[i] ) * tauxCEHRCelibataire[i];
                } else {
                    contribExceptionnelle += ( limitesCEHR[i+1] - limitesCEHR[i] ) * tauxCEHRCouple[i];
                }
            }
            i++;
        } while( i < 5);

        contribExceptionnelle = Math.round(contribExceptionnelle);
        System.out.println( "Contribution exceptionnelle sur les hauts revenus : " + contribExceptionnelle);

        // Calcul impôt des declarants
        // EXIGENCE : EXG_IMPOT_04
        // revenu imposable
        double rImposable = rFRef / nbPtsDecl;

        // impôt des déclarants
        double mImpDecl = 0;

        i = 0;
        do {
            if ( rImposable >= limites[i] && rImposable < limites[i+1] ) {
                mImpDecl += ( rImposable - limites[i] ) * taux[i];
                break;
            } else {
                mImpDecl += ( limites[i+1] - limites[i] ) * taux[i];
            }
            i++;
        } while( i < 5);

        mImpDecl = mImpDecl * nbPtsDecl;
        mImpDecl = Math.round(mImpDecl);

        System.out.println( "Impôt brut des déclarants : " + mImpDecl);

        // Calcul impôt foyer fiscal complet
        // EXIGENCE : EXG_IMPOT_04
        rImposable =  rFRef / nbPts;
        mImp = 0;
        i = 0;

        do {
            if ( rImposable >= limites[i] && rImposable < limites[i+1] ) {
                mImp += ( rImposable - limites[i] ) * taux[i];
                break;
            } else {
                mImp += ( limites[i+1] - limites[i] ) * taux[i];
            }
            i++;
        } while( i < 5);

        mImp = mImp * nbPts;
        mImp = Math.round( mImp );

        System.out.println( "Impôt brut du foyer fiscal complet : " + mImp );

        // Vérification de la baisse d'impôt autorisée
        // EXIGENCE : EXG_IMPOT_05
        // baisse impot

        double baisseImpot = mImpDecl - mImp;

        System.out.println( "Baisse d'impôt : " + baisseImpot );

        // dépassement plafond
        double ecartPts = nbPts - nbPtsDecl;

        // Plafond de baisse maximal par demi part
        double plafDemiPart = 1759;
        double plafond = (ecartPts / 0.5) * plafDemiPart;

        System.out.println( "Plafond de baisse autorisée " + plafond );

        if ( baisseImpot >= plafond ) {
            mImp = mImpDecl - plafond;
        }

        System.out.println( "Impôt brut après plafonnement avant decote : " + mImp );

        // Calcul de la decote
        // EXIGENCE : EXG_IMPOT_06

        // decote
        double decote = getDecote();

        System.out.println( "Decote : " + decote);

        mImp = mImp - decote;

        mImp += contribExceptionnelle;

        mImp = Math.round( mImp );

        System.out.println( "Impôt sur le revenu net final : " + mImp );
    }

    private double getDecote() {
        double decote = 0;
        // decote
        double tauxDecote = 0.4525;
        if ( nbPtsDecl == 1 ) {
            double seuilDecoteDeclarantSeul = 1929;
            if ( mImp < seuilDecoteDeclarantSeul) {
                double decoteMaxDeclarantSeul = 873;
                decote = decoteMaxDeclarantSeul - ( mImp  * tauxDecote);
            }
        }
        if (  nbPtsDecl == 2 ) {
            double seuilDecoteDeclarantCouple = 3191;
            if ( mImp < seuilDecoteDeclarantCouple) {
                double decoteMaxDeclarantCouple = 1444;
                decote =  decoteMaxDeclarantCouple - ( mImp  * tauxDecote);
            }
        }
        decote = Math.round(decote);

        if ( mImp <= decote) {
            decote = mImp;
        }
        return decote;
    }


}
