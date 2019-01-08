package com.pts3.r_friend;

import android.graphics.Point;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AlbumRecom extends Recommandation {

    private String artiste;
    private Integer nbTracks;
    private String titre;
    private String[] titresMorceaux;
    private String idAlbum;


    public AlbumRecom(String idRecommandation,  Long dateRecommandation, String destinataire, String emetteur, String picture, List<String> likingUsers, List<String> supportingUsers, List<Commentaire> commentaires, String artiste, Integer nbTracks, String titre, String idAlbum) {
        super(idRecommandation, dateRecommandation, destinataire, emetteur, picture, likingUsers, supportingUsers,commentaires);
        this.artiste = artiste;
        this.nbTracks = nbTracks;
        this.titre = titre;
        this.idAlbum=idAlbum;
        //rechercher les musiques de l'album en consultant la bdd grâce au titre et à l'artiste de l'album
    }

    public String getArtiste() {
        return artiste;
    }

    public Integer getNbTracks() {
        return nbTracks;
    }

    public String getTitre() {
        return titre;
    }

    public String[] getTitresMorceaux() {
        return titresMorceaux;
    }

    public void setArtiste(String artiste) {
        this.artiste = artiste;
    }

    public void setNbTracks(Integer nbTracks) {
        this.nbTracks = nbTracks;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setTitresMorceaux(String[] titresMorceaux) {
        this.titresMorceaux = titresMorceaux;
    }

    public String getIdAlbum() {
        return idAlbum;
    }

    @Override
    public boolean contains(String text) {
        text=text.toLowerCase();
        if (getDestinataire().toLowerCase().contains(text)
                || getArtiste().toLowerCase().contains(text)
                || getTitre().toLowerCase().contains(text)
                || getEmetteur().toLowerCase().contains(text))
            return true;
        return false;
    }
}
