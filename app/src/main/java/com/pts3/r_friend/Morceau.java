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

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public String getNomAlbum() {
        return nomAlbum;
    }

    public void setNomAlbum(String nomAlbum) {
        this.nomAlbum = nomAlbum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Integer getDuree() {
        return duree;
    }

    public void setDuree(Integer duree) {
        this.duree = duree;
    }

    public String getArtiste() {
        return artiste;
    }

    public void setArtiste(String artiste) {
        this.artiste = artiste;
    }
}
