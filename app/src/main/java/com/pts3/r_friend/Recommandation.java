package com.pts3.r_friend;

import android.content.Context;
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

public abstract class Recommandation {

    private String destinataire;
    private String emetteur;
    private int nbLikes;
    private int nbAppuis;
    private String picture;


    public Recommandation(String destinataire, String emetteur, int nbLikes, int nbAppuis, String picture) {
        this.destinataire = destinataire;
        this.emetteur = emetteur;
        this.nbLikes = nbLikes;
        this.nbAppuis = nbAppuis;
        this.picture = picture;
    }

        /*final ImageButton imageButton = new ImageButton(c);
        if (!isLiked())
            imageButton.setBackgroundResource(R.drawable.coeur_vide);
        else
            imageButton.setBackgroundResource(R.drawable.coeur_rouge);


        final ImageButton imageButton2 = new ImageButton(c);
        if (!isSupported())
            imageButton2.setBackgroundResource(R.drawable.one_white);
        else
            imageButton2.setBackgroundResource(R.drawable.one_green);





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
        });*/

    public String getDestinataire() {
        return destinataire;
    }

    public String getEmetteur() {
        return emetteur;
    }

    public int getNbLikes() {
        return nbLikes;
    }

    public int getNbAppuis() {
        return nbAppuis;
    }

    public String getPicture() {
        return picture;
    }

    public void setDestinataire(String destinataire) {
        this.destinataire = destinataire;
    }

    public void setEmetteur(String emetteur) {
        this.emetteur = emetteur;
    }

    public void setNbLikes(int nbLikes) {
        this.nbLikes = nbLikes;
    }

    public void setNbAppuis(int nbAppuis) {
        this.nbAppuis = nbAppuis;
    }

}
