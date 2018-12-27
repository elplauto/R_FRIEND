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

import java.util.List;

public class ArtisteRecom extends Recommandation {

    private String nom;
    private Integer nbAlbums;

    public ArtisteRecom(String idRecommandation, String destinataire, String emetteur, String picture, List<String> likingUsers, List<String> supportingUsers, String nom, Integer nbAlbums) {
        super(idRecommandation, destinataire, emetteur, picture, likingUsers, supportingUsers);
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
