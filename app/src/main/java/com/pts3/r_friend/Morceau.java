package com.pts3.r_friend;

public class Morceau {

    private String id;
    private String titre;
    private Integer duree;
    private String artiste;
    private String nomAlbum;
    private String pictureURL;

    public Morceau(String id, String titre, Integer duree, String artiste, String nomAlbum, String pictureURL) {
        this.id=id;
        this.titre = titre;
        this.duree = duree;
        this.artiste = artiste;
        this.nomAlbum = nomAlbum;
        this.pictureURL = pictureURL;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public String getNomAlbum() {
        return nomAlbum;
    }

    public String getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public Integer getDuree() {
        return duree;
    }

    public String getArtiste() {
        return artiste;
    }
}
