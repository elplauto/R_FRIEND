package com.pts3.r_friend;

import android.graphics.Point;
import android.graphics.Typeface;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MorceauRecom extends Recommandation {

    private String artiste;
    private String duree;
    private String titre;
    private String nomAlbum;
    private String idMorceau;

    public MorceauRecom(String destinataire, String emetteur, int nbLikes, int nbAppuis, String picture, String artiste, String duree, String titre, String nomAlbum, String idMorceau) {
        super(destinataire, emetteur, nbLikes, nbAppuis, picture);
        this.artiste = artiste;
        this.duree = duree;
        this.titre = titre;
        this.nomAlbum = nomAlbum;
        this.idMorceau=idMorceau;
    }

    public String getIdMorceau() {
        return idMorceau;
    }

    public String getArtiste() {
        return artiste;
    }

    public String getDuree() {
        return duree;
    }

    public String getTitre() {
        return titre;
    }

    public String getNomAlbum() {
        return nomAlbum;
    }

    public void setArtiste(String artiste) {
        this.artiste = artiste;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setNomAlbum(String nomAlbum) {
        this.nomAlbum = nomAlbum;
    }

}
