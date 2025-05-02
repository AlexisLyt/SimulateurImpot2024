package com.kerware.simulateur;

public class CalculateurDecote {
    private final double tauxDecote = 0.4525;
    private final double seuilDecoteDeclarantSeul = 1929;
    private final double decoteMaxDeclarantSeul = 873;

    private final double seuilDecoteDeclarantCouple = 3191;
    private final double decoteMaxDeclarantCouple = 1444;

    /**
     * Calcule la décote en fonction du montant de l'impot et du nombrte de parts des déclarants
     * @param montantImpot le montant de l'impot
     * @param nbPartsDeclarants le nombre de parts des déclarants
     * @return le montant de l'impot
     */
    public  double calculerDecote(double montantImpot, double nbPartsDeclarants) {
        double decote = 0;

        if (nbPartsDeclarants == 1) {
            if (montantImpot < seuilDecoteDeclarantSeul) decote = decoteMaxDeclarantSeul - montantImpot * tauxDecote;
        } else if (nbPartsDeclarants == 2) {
            if (montantImpot < seuilDecoteDeclarantCouple) decote = decoteMaxDeclarantCouple - montantImpot * tauxDecote;
        }

        decote = Math.round(Math.max(0, Math.min(decote, montantImpot)));
        return decote;
    }
}
