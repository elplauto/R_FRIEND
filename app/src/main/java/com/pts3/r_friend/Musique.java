package com.pts3.r_friend;

public class Musique {

    private String id;
    private String titre;
    private String duree;
    private String artiste;

    public Musique(String id, String titre, String duree, String artiste) {
        this.id=id;
        this.titre = titre;
        this.duree = duree;
        this.artiste = artiste;
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
