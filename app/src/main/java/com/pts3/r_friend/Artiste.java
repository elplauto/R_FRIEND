package com.pts3.r_friend;

public class Artiste {

    private String id;
    private String nom;
    private Integer nbAlbums;
    private String pictureURL;

    public Artiste(String id, String nom, Integer nbAlbums, String pictureURL) {
        this.id=id;
        this.nom = nom;
        this.nbAlbums=nbAlbums;
        this.pictureURL=pictureURL;
    }

    public Integer getNbAlbums() {
        return nbAlbums;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }
}
