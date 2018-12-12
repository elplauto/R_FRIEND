package com.pts3.r_friend;


import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CreationRecommandationActivity extends AppCompatActivity {

    DeezerManager deezerManager;
    Spinner spinner;
    android.support.v7.widget.SearchView.SearchAutoComplete searchAutoComplete;
    TextView tv1;
    TextView tv2;
    TextView tv3;
    TextView tv4;
    ImageView imageViewRecommandation;
    Boolean recherchePrecise;
    Button btnRecommander;
    Object recommandable;
    FirebaseDatabase database;
    int selectedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_recommandation);

        database = FirebaseDatabase.getInstance();

        spinner = (Spinner) findViewById(R.id.spinner);
        String choix[] = {"Musique","Album","Artiste"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 ,choix);
        spinner.setAdapter(adapter);

        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);

        imageViewRecommandation = findViewById(R.id.imageViewRecommandation);
        imageViewRecommandation.setVisibility(View.INVISIBLE);


        btnRecommander = findViewById(R.id.btnRecommander);
        btnRecommander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv1.getText().equals("Titre : ---") || tv1.getText().equals("Nom : ---")) {
                    Toast.makeText(getApplicationContext(), "Veuillez choisir un objet à recommander", Toast.LENGTH_SHORT).show();
                } else {
                    ajouterRecommandation();
                }
            }
        });

        recherchePrecise = false;

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner.getSelectedItem().toString().equals("Musique")) {
                    tv1.setText("Titre : ---");
                    tv2.setText("Album : ---");
                    tv3.setText("Duree : ---");
                    tv4.setText("Artiste : ---");
                } else if (spinner.getSelectedItem().toString().equals("Album")) {
                    tv1.setText("Nom : ---");
                    tv2.setText("Nombre de titres : ---");
                    tv3.setText("Artiste : ---");
                    tv4.setText("");
                } else if (spinner.getSelectedItem().toString().equals("Artiste")) {
                    tv1.setText("Nom : ---");
                    tv2.setText("Nombre d'album : ---");
                    tv3.setText("");
                    tv4.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        deezerManager = new DeezerManager(this,getResources().getString(R.string.app_id));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the search menu action bar.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_creation_recommandation, menu);
        // Get the search menu.
        MenuItem searchMenu = menu.findItem(R.id.search_view);

        // Get SearchView object.
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) searchMenu.getActionView();

        // Get SearchView autocomplete object.
        searchAutoComplete = (android.support.v7.widget.SearchView.SearchAutoComplete)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setBackgroundColor(Color.BLUE);
        searchAutoComplete.setTextColor(Color.GREEN);
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.holo_blue_light);

        // Create a new ArrayAdapter and add data to search auto complete object.
        String dataArr[] = {};
        ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, dataArr);
        searchAutoComplete.setAdapter(newsAdapter);

        // Listen to search view item on click event.
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                String queryString=(String)adapterView.getItemAtPosition(itemIndex);
                searchAutoComplete.setText(queryString);
                recherchePrecise = true;
                String typeRecherche=spinner.getSelectedItem().toString();
                selectedIndex = itemIndex;
                if (typeRecherche.equals("Musique")) {
                    deezerManager.rechercheMusique(queryString);
                } else if (typeRecherche.equals("Album")) {
                    deezerManager.rechercheAlbum(queryString);
                } else if (typeRecherche.equals("Artiste")) {
                    deezerManager.rechercheArtiste(queryString);
                }
            }
        });

        // Below event is triggered when submit search query.
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                String typeRecherche=spinner.getSelectedItem().toString();
                if (typeRecherche.equals("Musique")) {
                    deezerManager.rechercheMusique(s);
                } else if (typeRecherche.equals("Album")) {
                    deezerManager.rechercheAlbum(s);
                } else if (typeRecherche.equals("Artiste")) {
                    deezerManager.rechercheArtiste(s);
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void rechercheMusiqueReponse(List<Musique> musiques) {
        if (recherchePrecise && musiques.size()!=0) {
            Musique musique = musiques.get(0);
            for (Musique each : musiques) {
                if (each.getTitre().equals(searchAutoComplete.getText().toString())) {
                    musique=each;
                }
            }
            tv1.setText("Titre : " + musique.getTitre());
            tv2.setText("Album : " + musique.getNomAlbum());
            tv3.setText("Duree : " + musique.getDuree());
            tv4.setText("Artiste : " + musique.getArtiste());
            Picasso.with(this).load(musique.getPictureURL()).into(imageViewRecommandation);
            imageViewRecommandation.setVisibility(View.VISIBLE);
            recommandable = musique;
            recherchePrecise=false;
        } else if (!recherchePrecise) {
            ArrayList<String> propositions = new ArrayList<>();
            for (Musique musique : musiques) {
                propositions.add(musique.getTitre());
            }
            ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, propositions);
            searchAutoComplete.setAdapter(newsAdapter);
        }
    }

    public void rechercheAlbumReponse(List<com.pts3.r_friend.Album> albums) {
        if (recherchePrecise && albums.size()!=0) {
            Album album = albums.get(0);
            for (Album each : albums) {
                if (each.getTitre().equals(searchAutoComplete.getText().toString())) {
                    album=each;
                 }
            }
            tv1.setText("Nom : " + album.getTitre());
            tv2.setText("Nombre de titres : " + album.getNbTrack());
            tv3.setText("Artiste : " + album.getArtiste());
            tv4.setText("");
            Picasso.with(this).load(album.getPictureURL()).into(imageViewRecommandation);
            imageViewRecommandation.setVisibility(View.VISIBLE);
            recommandable=album;
            recherchePrecise=false;
        } else if (!recherchePrecise) {
            ArrayList<String> propositions = new ArrayList<>();
            for (com.pts3.r_friend.Album album : albums) {
                propositions.add(album.getTitre());
            }
            ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, propositions);
            searchAutoComplete.setAdapter(newsAdapter);
        }
    }

    public void rechercheArtisteReponse(List<Artiste> artistes) {
        if (recherchePrecise && artistes.size()!=0) {
            Artiste artiste = artistes.get(0);
            for (Artiste each : artistes) {
                if (each.getNom().equals(searchAutoComplete.getText().toString())) {
                    artiste = each;
                }
            }
            tv1.setText("Nom : " + artiste.getNom());
            tv2.setText("Nombre d'album : " + artiste.getNbAlbums());
            tv3.setText("");
            tv4.setText("");
            Picasso.with(this).load(artiste.getPictureURL()).into(imageViewRecommandation);
            imageViewRecommandation.setVisibility(View.VISIBLE);
            recommandable = artiste;
            recherchePrecise=false;
        } else if (!recherchePrecise) {
            ArrayList<String> propositions = new ArrayList<>();
            for (Artiste artiste : artistes) {
                propositions.add(artiste.getNom());
            }
            ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, propositions);
            searchAutoComplete.setAdapter(newsAdapter);
        }
    }

    private void ajouterRecommandation() {
        DatabaseReference root = database.getReference();
        DatabaseReference recommandation;
        if (spinner.getSelectedItem().toString().equals("Musique")) {
            Musique musique = (Musique) recommandable;
            ajouterMusique(musique);
            recommandation = root.child("recommandations").child("recommandationsMusique").push();
            recommandation.child("emetteur").setValue("");
            recommandation.child("destinataire").setValue("");
            recommandation.child("idMusique").setValue(musique.getId());
            recommandation.child("nbLikes").setValue(0);
            recommandation.child("nbAppuis").setValue(0);
            recommandation.child("dateRecommandation").setValue(new GregorianCalendar().getTime().toString());
        } else if (spinner.getSelectedItem().toString().equals("Album")) {
            Album album = (Album) recommandable;
            ajouterAlbum(album);
            recommandation = root.child("recommandations").child("recommandationsAlbum").push();
            recommandation.child("emetteur").setValue("");
            recommandation.child("destinataire").setValue("");
            recommandation.child("idAlbum").setValue(album.getId());
            recommandation.child("nbLikes").setValue(0);
            recommandation.child("nbAppuis").setValue(0);
            recommandation.child("dateRecommandation").setValue(new GregorianCalendar().getTime().toString());
        } else if (spinner.getSelectedItem().toString().equals("Artiste")) {
            Artiste artiste = (Artiste) recommandable;
            ajouterArtiste(artiste);
            recommandation = root.child("recommandations").child("recommandationsArtiste").push();
            recommandation.child("emetteur").setValue("");
            recommandation.child("destinataire").setValue("");
            recommandation.child("idArtiste").setValue(artiste.getId());
            recommandation.child("nbLikes").setValue(0);
            recommandation.child("nbAppuis").setValue(0);
            recommandation.child("dateRecommandation").setValue(new GregorianCalendar().getTime().toString());
        }
        Toast.makeText(getApplicationContext(), "Votre recommandation a bien été créée", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void ajouterMusique(Musique musique) {
        DatabaseReference refMusique = database.getReference("musiques");
        DatabaseReference ref = refMusique.child(musique.getId());
        ref.child("artiste").setValue(musique.getArtiste());
        ref.child("duree").setValue(musique.getDuree());
        ref.child("titre").setValue(musique.getTitre());
        ref.child("album").setValue(musique.getNomAlbum());
        ref.child("pictureURL").setValue(musique.getPictureURL());
    }

    public void ajouterAlbum(Album album) {
        DatabaseReference refMusiques = database.getReference("albums");
        DatabaseReference ref = refMusiques.child(album.getId());
        ref.child("artiste").setValue(album.getArtiste());
        ref.child("titre").setValue(album.getTitre());
        ref.child("nbTrack").setValue(album.getNbTrack());
        ref.child("pictureURL").setValue(album.getPictureURL());
    }

    public void ajouterArtiste(Artiste artiste) {
        DatabaseReference refMusiques = database.getReference("artistes");
        DatabaseReference ref = refMusiques.child(artiste.getId());
        ref.child("nom").setValue(artiste.getNom());
        ref.child("nbAlbums").setValue(artiste.getNbAlbums());
        ref.child("pictureURL").setValue(artiste.getPictureURL());
    }
}