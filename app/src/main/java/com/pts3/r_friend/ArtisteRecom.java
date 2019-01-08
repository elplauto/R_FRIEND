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

    public ArtisteRecom(String idRecommandation,  Long dateRecommandation, String destinataire, String emetteur, String picture, List<String> likingUsers, List<String> supportingUsers,List<Commentaire> commentaires, String nom, Integer nbAlbums) {
        super(idRecommandation, dateRecommandation, destinataire, emetteur, picture, likingUsers, supportingUsers,commentaires);
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

    @Override
    public boolean contains(String text) {
        text = text.toLowerCase();
        if (getDestinataire().toLowerCase().contains(text)
                || getNom().toLowerCase().contains(text)
                || getEmetteur().toLowerCase().contains(text))
            return true;
        return false;
    }
}
