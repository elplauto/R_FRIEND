package com.pts3.r_friend;

public class Musique {

    private String id;
    private String titre;
    private String duree;
    private String artiste;
    private String nomAlbum;
    private String pictureURL;

    public Musique(String id, String titre, String duree, String artiste, String nomAlbum, String pictureURL) {
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

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public String getArtiste() {
        return artiste;
    }

    public void setArtiste(String artiste) {
        this.artiste = artiste;
    }
}
