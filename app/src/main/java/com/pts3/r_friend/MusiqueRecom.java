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

public class MusiqueRecom extends Recommandation {

    private String groupe;
    private String duree;
    private String titre;
    private String nomAlbum;

    public MusiqueRecom(String destinataire, String emetteur, int nbLikes, int nbAppuis, String picture, String groupe, String duree, String titre, String nomAlbum) {
        super(destinataire, emetteur, nbLikes, nbAppuis, picture);
        this.groupe = groupe;
        this.duree = duree;
        this.titre = titre;
        this.nomAlbum = nomAlbum;
    }

    public void displayCenterOfRecom(MainActivity c, LinearLayout[] ll) {
        Point size = c.getSize();
        TextView tv = new TextView(c);
        tv.setText(this.getEmetteur() + " recommande une musique" + (getDestinataire().equals(getRecomendedToEveryone()) ? "" : " à "+this.getDestinataire()));
        tv.setTypeface(null, Typeface.BOLD_ITALIC);
        ll[0].addView(tv);
        ll[0].addView(ll[1]);
        TextView tv2 = new TextView(c);
        tv2.setText("Titre : " + this.getTitre() + " (" + this.getDuree() + ")");
        TextView tv3 = new TextView(c);
        tv3.setText("Groupe : " + this.getGroupe());
        TextView tv4 = new TextView(c);
        tv4.setText("Album : " + this.getNomAlbum());
        ll[2].addView(tv2);
        ll[2].addView(tv3);
        ll[2].addView(tv4);
        ImageView imageView = new ImageView(c);
        Picasso.with(c).load(this.getPicture()).into(imageView);
        ll[1].addView(imageView,new LinearLayout.LayoutParams(size.x/5,size.y/5));
        ll[1].addView(ll[2]);
    }

    public String getGroupe() {
        return groupe;
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

    public void setGroupe(String groupe) {
        this.groupe = groupe;
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
