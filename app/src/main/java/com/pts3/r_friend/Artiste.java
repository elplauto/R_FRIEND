package com.pts3.r_friend;

public class Artiste {

    private String id;
    private String nom;
    private Integer nbAlbums;
    private String pictureURL;
    private String idTitre;

    public Artiste(String id, String nom, Integer nbAlbums, String pictureURL, String idTitre) {
        this.id=id;
        this.nom = nom;
        this.nbAlbums=nbAlbums;
        this.pictureURL=pictureURL;
        this.idTitre=idTitre;
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

    public String getIdTitre() {
        return idTitre;
    }
}
