package com.pts3.r_friend;

public class Utilisateur extends CreationCompteActivity {
    String adresseMail;
    String pseudo;
    String mdp;

    public Utilisateur(String pAdresseMail,String pPseudo, String pMdp){
        adresseMail=pAdresseMail;
        pseudo=pPseudo;
        mdp=pMdp;
    }

    public String getAdresseMail() {
        return adresseMail;
    }

    public void setAdresseMail(String adresseMail) {
        this.adresseMail = adresseMail;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }


}
