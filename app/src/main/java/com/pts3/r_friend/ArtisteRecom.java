package com.pts3.r_friend;

import java.util.List;

public class ArtisteRecom extends Recommandation {

    private String nom;
    private Integer nbAlbums;
    private String idTitre;

    public ArtisteRecom(String idRecommandation,  Long dateRecommandation, String destinataire, String emetteur, String picture, List<String> likingUsers, List<String> supportingUsers,List<Commentaire> commentaires, String nom, Integer nbAlbums, String idTitre) {
        super(idRecommandation, dateRecommandation, destinataire, emetteur, picture, likingUsers, supportingUsers,commentaires);
        this.nom = nom;
        this.nbAlbums = nbAlbums;
        this.idTitre=idTitre;
    }

    public String getNom() {
        return nom;
    }

    public Integer getNbAlbums() {
        return nbAlbums;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNbAlbums(Integer nbAlbums) {
        this.nbAlbums = nbAlbums;
    }

    public String getIdTitre() {
        return idTitre;
    }

    @Override
    public boolean contains(String text) {
        text = text.toLowerCase();
        if (getDestinataire().toLowerCase().contains(text)
                || getNom().toLowerCase().contains(text)
                || getEmetteur().toLowerCase().contains(text))
            return true;
        return false;
    }

    @Override
    public String getNomObjet() {
        return this.nom;
    }
}
