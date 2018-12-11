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
    private int nbTracks;
    private String titre;
    private String imgAlbum;    //path de l'image
    private String[] titresMusiques;


    public AlbumRecom(String destinataire, String emetteur, int nbLikes, int nbAppuis, boolean isLiked, boolean isSupported, String groupe, int nbTracks, String titre, String imgAlbum) {
        super(destinataire, emetteur, nbLikes, nbAppuis, isLiked, isSupported);
        this.groupe = groupe;
        this.nbTracks = nbTracks;
        this.titre = titre;
        this.imgAlbum = imgAlbum;
        //rechercher les musiques de l'album en consultant la bdd grâce au titre et à l'artiste de l'album
    }

    public LinearLayout toLinearLayout(final MainActivity c) {
        Point size = c.getSize();
        LinearLayout ll = new LinearLayout(c);
        LinearLayout ll2 = new LinearLayout(c);
        LinearLayout ll3 = new LinearLayout(c);
        LinearLayout ll4 = new LinearLayout(c);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll2.setOrientation(LinearLayout.HORIZONTAL);
        ll3.setOrientation(LinearLayout.VERTICAL);
        ll4.setOrientation(LinearLayout.HORIZONTAL);
        ll.setBackgroundColor(c.getResources().getColor(R.color.colorAccent));
        TextView tv = new TextView(c);
        tv.setText(this.getEmetteur() + " recommande un album à " + this.getDestinataire());
        tv.setTypeface(null, Typeface.BOLD_ITALIC);
        ll.addView(tv);
        ll.addView(ll2);
        TextView tv2 = new TextView(c);
        tv2.setText("Titre : " + this.getTitre() + " (" + this.getNbTracks() + " titres)");
        TextView tv3 = new TextView(c);
        tv3.setText("Groupe : " + this.getGroupe());
        ll3.addView(tv2);
        ll3.addView(tv3);
        ImageView imageView = new ImageView(c);
        Picasso.with(c).load("http://i.imgur.com/DvpvklR.png").into(imageView);
        ll2.addView(imageView,new LinearLayout.LayoutParams(size.x/5,size.y/5));
        ll2.addView(ll3);
        final ImageButton imageButton = new ImageButton(c);
        if (!isLiked())
            imageButton.setBackgroundResource(R.drawable.coeur_vide);
        else
            imageButton.setBackgroundResource(R.drawable.coeur_rouge);
        //espace
        ll4.addView(new Space(c),new LinearLayout.LayoutParams(10,1)); //(size.x*95/100-size.x/5)/3
        //coeur
        ll4.addView(imageButton,new LinearLayout.LayoutParams(size.x/10,size.x/10));
        //espace
        espace(ll4, c);
        //nbLikes
        setTvLikes(new TextView(c));
        displayNbLikes();
        int h = size.x/10;
        ll4.addView(getTvLikes(), new LinearLayout.LayoutParams(h*3, h/2));
        //espace
        espace(ll4, c); //(size.x*95/100-size.x/5)/3
        final ImageButton imageButton2 = new ImageButton(c);
        if (!isSupported())
            imageButton2.setBackgroundResource(R.drawable.one_white);
        else
            imageButton2.setBackgroundResource(R.drawable.one_green);
        //+1
        ll4.addView(imageButton2,new LinearLayout.LayoutParams(h, h));
        ll.addView(ll4);
        //espace
        espace(ll4, c);
        //nbAppuis
        setTvAppuis(new TextView(c));
        displayNbAppuis();
        ll4.addView(getTvAppuis(), new LinearLayout.LayoutParams(h*3, h/2));

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLiked()) {
                    setLiked(false);
                    imageButton.setBackgroundResource(R.drawable.coeur_vide);
                    setNbLikes(getNbLikes()-1);
                    displayNbLikes();
                }
                else {
                    setLiked(true);
                    imageButton.setBackgroundResource(R.drawable.coeur_rouge);
                    setNbLikes(getNbLikes()+1);
                    displayNbLikes();
                }
            }
        });

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSupported()){ //si la recommandation était appuyée est que l'utilisateur rappuie sur +1 alors elle n'est plus appuyée
                    setSupported(false);
                    imageButton2.setBackgroundResource(R.drawable.one_white);
                    setNbAppuis(getNbAppuis()-1);
                    displayNbAppuis();
                }
                else { //Si elle n'était pas appuyée elle le devient
                    setSupported(true);
                    imageButton2.setBackgroundResource(R.drawable.one_green);
                    setNbAppuis(getNbAppuis()+1);
                    displayNbAppuis();
                }
            }
        });
        return ll;
    }

    public String getGroupe() {
        return groupe;
    }

    public int getNbTracks() {
        return nbTracks;
    }

    public String getTitre() {
        return titre;
    }

    public String getImgAlbum() {
        return imgAlbum;
    }

    public String[] getTitresMusiques() {
        return titresMusiques;
    }

    public void setGroupe(String groupe) {
        this.groupe = groupe;
    }

    public void setNbTracks(int nbTracks) {
        this.nbTracks = nbTracks;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setImgAlbum(String imgAlbum) {
        this.imgAlbum = imgAlbum;
    }

    public void setTitresMusiques(String[] titresMusiques) {
        this.titresMusiques = titresMusiques;
    }
}
