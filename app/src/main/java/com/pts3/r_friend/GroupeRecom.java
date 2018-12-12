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

public class GroupeRecom extends Recommandation {

    private String nom;
    private String photo;
    private String[] albums = {};

    public GroupeRecom(String destinataire, String emetteur, int nbLikes, int nbAppuis, String nom) {
        super(destinataire, emetteur, nbLikes, nbAppuis);
        this.nom = nom;
        //rechercher photo de l'artiste depuisson nom
        //rechercher les albums depuis le nom de l'artiste
    }

    public void displayCenterOfRecom(MainActivity c, LinearLayout[] ll) {
        Point size = c.getSize();
        TextView tv = new TextView(c);
        tv.setText(this.getEmetteur() + " recommande un groupe" + (getDestinataire().equals(getRecomendedToEveryone()) ? "" : " à "+this.getDestinataire()));
        tv.setTypeface(null, Typeface.BOLD_ITALIC);
        ll[0].addView(tv);
        ll[0].addView(ll[1]);
        TextView tv2 = new TextView(c);
        tv2.setText("Nom : " + this.getNom());
        TextView tv3 = new TextView(c);
        tv3.setText("Nombre d'albums : " + this.getAlbums().length);
        ll[2].addView(tv2);
        ll[2].addView(tv3);
        ImageView imageView = new ImageView(c);
        Picasso.with(c).load("http://i.imgur.com/DvpvklR.png").into(imageView);
        ll[1].addView(imageView,new LinearLayout.LayoutParams(size.x/5,size.y/5));
        ll[1].addView(ll[2]);
    }

    public String getNom() {
        return nom;
    }

    public String getPhoto() {
        return photo;
    }

    public String[] getAlbums() {
        return albums;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setAlbums(String[] albums) {
        this.albums = albums;
    }
}
