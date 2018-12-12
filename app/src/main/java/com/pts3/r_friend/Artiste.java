package com.pts3.r_friend;

public class Artiste {

    private String id;
    private String nom;
    private String nbAlbums;
    private String pictureURL;

    public Artiste(String id, String nom, String nbAlbums, String pictureURL) {
        this.id=id;
        this.nom = nom;
        this.nbAlbums=nbAlbums;
        this.pictureURL=pictureURL;
    }

    public String getNbAlbums() {
        return nbAlbums;
    }

    public void setNbAlbums(String nbAlbums) {
        this.nbAlbums = nbAlbums;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
