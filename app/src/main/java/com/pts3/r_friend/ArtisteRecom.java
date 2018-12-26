package com.pts3.r_friend;

import android.graphics.Point;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ArtisteRecom extends Recommandation {

    private String nom;
    private Integer nbAlbums;

    public ArtisteRecom(String destinataire, String emetteur, int nbLikes, int nbAppuis, String picture, String nom, Integer nbAlbums) {
        super(destinataire, emetteur, nbLikes, nbAppuis, picture);
        this.nom = nom;
        this.nbAlbums = nbAlbums;
    }

    public String getNom() {
        return nom;
    }

    public Integer getNbAlbums() {
        return nbAlbums;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNbAlbums(Integer nbAlbums) {
        this.nbAlbums = nbAlbums;
    }
}
