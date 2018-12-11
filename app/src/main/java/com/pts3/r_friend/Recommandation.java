package com.pts3.r_friend;

public class Recommandation {

    private String destinataire;
    private String emetteur;
    private String titre;
    private String type;
    private String sortie;


    public Recommandation(String destinataire, String emetteur, String titre, String type, String sortie) {
        this.destinataire = destinataire;
        this.emetteur = emetteur;
        this.titre = titre;
        this.type = type;
        this.sortie = sortie;
    }

    public String getDestinataire() {
        return destinataire;
    }

    public String getEmetteur() {
        return emetteur;
    }

    public String getTitre() {
        return titre;
    }

    public String getType() {
        return type;
    }

    public String getSortie() {
        return sortie;
    }
}
