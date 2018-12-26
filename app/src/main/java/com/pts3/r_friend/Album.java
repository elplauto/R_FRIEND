package com.pts3.r_friend;

public class Album {

    private String id;
    private String titre;
    private String artiste;
    private Integer nbTrack;
    private String pictureURL;

    public Album(String id, String titre, String artiste, Integer nbTrack, String pictureURL) {
        this.id=id;
        this.titre = titre;
        this.artiste = artiste;
        this.nbTrack = nbTrack;
        this.pictureURL=pictureURL;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public String getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public String getArtiste() {
        return artiste;
    }

    public Integer getNbTrack() {
        return nbTrack;
    }
}
