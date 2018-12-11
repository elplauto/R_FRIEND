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

    private TextView tvLikes;
    private TextView tvAppuis;
    private boolean isLiked;        //il faudra enregistrer une liste de tous les utilisateurs qui ont likés la recom
    private boolean isSupported;


    public Recommandation(String destinataire, String emetteur, int nbLikes, int nbAppuis, boolean isLiked, boolean isSupported) {
        this.destinataire = destinataire;
        this.emetteur = emetteur;
        this.nbLikes = nbLikes;
        this.nbAppuis = nbAppuis;
        this.isLiked = isLiked;
        this.isSupported = isSupported;
    }

    public void espace(LinearLayout ll, MainActivity c) {
        ll.addView(new Space(c),new LinearLayout.LayoutParams(10,1)); //(size.x*95/100-size.x/5)/3
    }

    public void displayNbLikes() {
        tvLikes.setText(""+this.getNbLikes());
    }

    public void displayNbAppuis() {
        tvAppuis.setText(""+this.getNbAppuis());
    }

    public abstract LinearLayout toLinearLayout(MainActivity c);

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

    public TextView getTvLikes() {
        return tvLikes;
    }

    public TextView getTvAppuis() {
        return tvAppuis;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public boolean isSupported() {
        return isSupported;
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

    public void setTvLikes(TextView tvLikes) {
        this.tvLikes = tvLikes;
    }

    public void setTvAppuis(TextView tvAppuis) {
        this.tvAppuis = tvAppuis;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public void setSupported(boolean supported) {
        isSupported = supported;
    }
}
