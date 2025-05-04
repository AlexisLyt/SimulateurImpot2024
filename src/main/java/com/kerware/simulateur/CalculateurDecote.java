package com.kerware.simulateur;

public class CalculateurDecote {

    private static final double TAUX_DECOTE = 0.4525;
    private static final double SEUIL_DECOTE_DECLARANT_SEUL = 1929;
    private static final double SEUIL_DECOTE_DECLARANT_COUPLE = 3191;
    private static final double DECOTE_MAX_DECLARANT_SEUL = 873;
    private static final double DECOTE_MAX_DECLARANT_COUPLE = 1444;

    /**
     * Calcule la décote en fonction du montant de l'impot et du nombrte de parts des déclarants
     * @param montantImpot le montant de l'impot
     * @param nbPartsDeclarants le nombre de parts des déclarants
     * @return le montant de l'impot
     */
    public  double calculerDecote(double montantImpot, double nbPartsDeclarants) {
        double decote = 0;
        if (nbPartsDeclarants == 1) {
            if (montantImpot < SEUIL_DECOTE_DECLARANT_SEUL) {
                decote = DECOTE_MAX_DECLARANT_SEUL - montantImpot * TAUX_DECOTE;
            }
        } else if (nbPartsDeclarants == 2) {
            if (montantImpot < SEUIL_DECOTE_DECLARANT_COUPLE) {
                decote = DECOTE_MAX_DECLARANT_COUPLE - montantImpot * TAUX_DECOTE;
            }
        }

        decote = Math.round(Math.max(0, Math.min(decote, montantImpot)));
        return decote;
    }
}
