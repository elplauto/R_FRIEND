package com.pts3.r_friend;

import android.graphics.Point;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class AlbumRecom extends Recommandation {

    private String groupe;
    private String nbTracks;
    private String titre;
    private String[] titresMusiques;


    public AlbumRecom(String destinataire, String emetteur, int nbLikes, int nbAppuis, String picture, String groupe, String nbTracks, String titre) {
        super(destinataire, emetteur, nbLikes, nbAppuis, picture);
        this.groupe = groupe;
        this.nbTracks = nbTracks;
        this.titre = titre;
        //rechercher les musiques de l'album en consultant la bdd grâce au titre et à l'artiste de l'album
    }

    public void displayCenterOfRecom(MainActivity c, LinearLayout[] ll) {
        Point size = c.getSize();
        TextView tv = new TextView(c);
        tv.setText(this.getEmetteur() + " recommande un album" + (getDestinataire().equals(getRecomendedToEveryone()) ? "" : " à "+this.getDestinataire()));
        tv.setTypeface(null, Typeface.BOLD_ITALIC);
        ll[0].addView(tv);
        ll[0].addView(ll[1]);
        TextView tv2 = new TextView(c);
        tv2.setText("Titre : " + this.getTitre() + " (" + this.getNbTracks() + " titre" + (Integer.parseInt(this.getNbTracks()) > 1 ? "s" : "") + ")");
        TextView tv3 = new TextView(c);
        tv3.setText("Artiste : " + this.getGroupe());
        ll[2].addView(tv2);
        ll[2].addView(tv3);
        ImageView imageView = new ImageView(c);
        Picasso.with(c).load(this.getPicture()).into(imageView);
        ll[1].addView(imageView,new LinearLayout.LayoutParams(size.x/5,size.y/5));
        ll[1].addView(ll[2]);
    }

    public String getGroupe() {
        return groupe;
    }

    public String getNbTracks() {
        return nbTracks;
    }

    public String getTitre() {
        return titre;
    }

    public String[] getTitresMusiques() {
        return titresMusiques;
    }

    public void setGroupe(String groupe) {
        this.groupe = groupe;
    }

    public void setNbTracks(String nbTracks) {
        this.nbTracks = nbTracks;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setTitresMusiques(String[] titresMusiques) {
        this.titresMusiques = titresMusiques;
    }
}
