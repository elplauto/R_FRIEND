package com.pts3.r_friend;

public class Album {

    private String id;
    private String titre;
    private String artiste;
    private String nbTrack;
    private String pictureURL;

    public Album(String id, String titre, String artiste, String nbTrack, String pictureURL) {
        this.id=id;
        this.titre = titre;
        this.artiste = artiste;
        this.nbTrack = nbTrack;
        this.pictureURL=pictureURL;
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

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getArtiste() {
        return artiste;
    }

    public void setArtiste(String artiste) {
        this.artiste = artiste;
    }

    public String getNbTrack() {
        return nbTrack;
    }

    public void setNbTrack(String nbTrack) {
        this.nbTrack = nbTrack;
    }
}
