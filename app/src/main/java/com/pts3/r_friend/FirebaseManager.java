package com.pts3.r_friend;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseManager {

    MainActivity context;
    FirebaseDatabase database;

    public FirebaseManager(MainActivity context) {
        this.context=context;
        database = FirebaseDatabase.getInstance();
    }

    public void ajouterMusique(Musique musique) {

        DatabaseReference refMusique = database.getReference("musiques");
        DatabaseReference ref = refMusique.child(musique.getId());
        ref.child("artiste").setValue(musique.getArtiste());
        ref.child("duree").setValue(musique.getDuree());
        ref.child("titre").setValue(musique.getTitre());
    }

    public void ajouterAlbum(Album album) {

        DatabaseReference refMusiques = database.getReference("albums");
        DatabaseReference ref = refMusiques.child(album.getId());
        ref.child("artiste").setValue(album.getArtiste());
        ref.child("titre").setValue(album.getTitre());
        ref.child("nbTrack").setValue(album.getNbTrack());
    }

    public void ajouterArtiste(Artiste artiste) {

        DatabaseReference refMusiques = database.getReference("artistes");
        DatabaseReference ref = refMusiques.child(artiste.getId());
        ref.child("nom").setValue(artiste.getNom());
    }

    public void debug(String s) {
        Log.e("-----",s);
    }
}
