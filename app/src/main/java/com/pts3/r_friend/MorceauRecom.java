package com.pts3.r_friend;

import java.util.List;

public class MorceauRecom extends Recommandation {

    private String artiste;
    private String duree;
    private String titre;
    private String nomAlbum;
    private String idMorceau;

    public MorceauRecom(String idRecommandation,  Long dateRecommandation, String destinataire, String emetteur, String picture, List<String> likingUsers, List<String> supportingUsers, String artiste, String duree, String titre, String nomAlbum, String idMorceau) {
        super(idRecommandation, dateRecommandation, destinataire, emetteur, picture, likingUsers, supportingUsers);
        this.artiste = artiste;
        this.duree = duree;
        this.titre = titre;
        this.nomAlbum = nomAlbum;
        this.idMorceau=idMorceau;
    }

    public String getIdMorceau() {
        return idMorceau;
    }

    public String getArtiste() {
        return artiste;
    }

    public String getDuree() {
        return duree;
    }

    public String getTitre() {
        return titre;
    }

    public String getNomAlbum() {
        return nomAlbum;
    }
}
