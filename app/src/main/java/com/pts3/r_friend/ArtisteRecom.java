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

import java.util.Date;

public class ArtisteRecom extends Recommandation {

    private String nom;
    private Integer nbAlbums;

    public ArtisteRecom(String destinataire, String emetteur, int nbLikes, int nbAppuis, String picture, Date date, String nom, Integer nbAlbums) {
        super(destinataire, emetteur, nbLikes, nbAppuis, picture, date);
        this.nom = nom;
        this.nbAlbums = nbAlbums;
    }

    public void displayCenterOfRecom(MainActivity context, LinearLayout[] ll) {
        Point size = context.getSize();
        TextView tv = new TextView(context);
        tv.setText(this.getEmetteur() + " recommande un artiste" + (getDestinataire().equals(getRecomendedToEveryone()) ? "" : " à "+this.getDestinataire()));
        tv.setTypeface(null, Typeface.BOLD_ITALIC);
        ll[0].addView(tv);
        ll[0].addView(ll[1]);
        TextView tv2 = new TextView(context);
        tv2.setText("Nom : " + this.getNom());
        TextView tv3 = new TextView(context);
        tv3.setText("Nombre d'albums : " + this.getNbAlbums());
        ll[2].addView(tv2);
        ll[2].addView(tv3);
        ImageView imageView = new ImageView(context);
        Picasso.with(context).load(this.getPicture()).into(imageView);
        ll[1].addView(imageView,new LinearLayout.LayoutParams(size.x/5,size.y/5));
        ll[1].addView(ll[2]);
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
