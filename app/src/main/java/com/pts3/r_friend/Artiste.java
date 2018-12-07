package com.pts3.r_friend;

public class Artiste {

    private String id;
    private String nom;

    public Artiste(String id, String nom) {
        this.id=id;
        this.nom = nom;
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
