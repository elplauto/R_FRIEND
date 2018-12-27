package com.pts3.r_friend;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class CreationRecommandationActivity extends AppCompatActivity {

    DeezerDataSearcher deezerDataSearcher;
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
    DatabaseReference root;
    int selectedIndex;
    String emetteur;
    EditText destinataire;
    Boolean destinataireExistant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_recommandation);

        database = FirebaseDatabase.getInstance();
        root = database.getReference();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        emetteur = prefs.getString("pseudo","");

        spinner = (Spinner) findViewById(R.id.spinner);
        String choix[] = {"Morceau","Album","Artiste"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 ,choix);
        spinner.setAdapter(adapter);

        destinataireExistant = true;

        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);

        destinataire = (EditText) findViewById(R.id.editTextDestinataire);
        destinataire.setTextColor(Color.argb(255,255,255,255));
        destinataire.setHintTextColor(Color.argb(255,255,255,255));

        destinataire.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                root.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(s.toString())) {
                            destinataire.setTextColor(Color.argb(255,13, 224, 55));
                            destinataireExistant = true;
                        }
                        else {
                            destinataire.setTextColor(Color.argb(255,224, 13, 13));
                            destinataireExistant=false;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                    }
                });
                if (s.toString().equals("")) {
                    destinataireExistant = true;
                } else if (s.toString().equals(emetteur)){
                    destinataire.setTextColor(Color.argb(255,224, 13, 13));
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        imageViewRecommandation = findViewById(R.id.imageViewRecommandation);
        imageViewRecommandation.setVisibility(View.INVISIBLE);


        btnRecommander = findViewById(R.id.btnRecommander);
        btnRecommander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv1.getText().equals("Titre : ---") || tv1.getText().equals("Nom : ---")) {
                    Toast.makeText(getApplicationContext(), "Veuillez choisir un objet à recommander", Toast.LENGTH_SHORT).show();
                } else if(!destinataireExistant) {
                    Toast.makeText(getApplicationContext(), "Destinataire inexistant", Toast.LENGTH_SHORT).show();
                } else if(destinataire.getText().toString().equals(emetteur)) {
                    Toast.makeText(getApplicationContext(), "Vous ne pouvez pas faire de recommandation à vous-même", Toast.LENGTH_SHORT).show();
                } else {
                    ajouterRecommandation();
                }
            }
        });

        recherchePrecise = false;

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner.getSelectedItem().toString().equals("Morceau")) {
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

        deezerDataSearcher = new DeezerDataSearcher(this,getResources().getString(R.string.app_id));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#26223C")));
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
        searchAutoComplete.setBackgroundColor(Color.parseColor("#403965"));
        searchAutoComplete.setTextColor(Color.parseColor("#B7D2E1"));
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.holo_blue_bright);

        // Create a new ArrayAdapter and add data to search auto complete object.
        String dataArr[] = {};
        ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, dataArr);
        searchAutoComplete.setAdapter(newsAdapter);

        // Listen to search view item on click event.
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                closeKeyboard();
                String queryString=(String)adapterView.getItemAtPosition(itemIndex);
                searchAutoComplete.setText(queryString);
                recherchePrecise = true;
                String typeRecherche=spinner.getSelectedItem().toString();
                selectedIndex = itemIndex;
                if (typeRecherche.equals("Morceau")) {
                    deezerDataSearcher.rechercheMorceau(queryString);
                } else if (typeRecherche.equals("Album")) {
                    deezerDataSearcher.rechercheAlbum(queryString);
                } else if (typeRecherche.equals("Artiste")) {
                    deezerDataSearcher.rechercheArtiste(queryString);
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
                if (typeRecherche.equals("Morceau")) {
                    deezerDataSearcher.rechercheMorceau(s);
                } else if (typeRecherche.equals("Album")) {
                    deezerDataSearcher.rechercheAlbum(s);
                } else if (typeRecherche.equals("Artiste")) {
                    deezerDataSearcher.rechercheArtiste(s);
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void rechercheMorceauReponse(List<Morceau> morceaux) {
        if (recherchePrecise && morceaux.size()!=0) {
            Morceau morceau = morceaux.get(0);
            for (Morceau each : morceaux) {
                if (each.getTitre().equals(searchAutoComplete.getText().toString())) {
                    morceau=each;
                }
            }
            tv1.setText("Titre : " + morceau.getTitre());
            tv2.setText("Album : " + morceau.getNomAlbum());
            tv3.setText("Duree : " + morceau.getDuree());
            tv4.setText("Artiste : " + morceau.getArtiste());
            Picasso.with(this).load(morceau.getPictureURL()).into(imageViewRecommandation);
            imageViewRecommandation.setVisibility(View.VISIBLE);
            recommandable = morceau;
            recherchePrecise=false;
        } else if (!recherchePrecise) {
            ArrayList<String> propositions = new ArrayList<>();
            for (Morceau morceau : morceaux) {
                propositions.add(morceau.getTitre());
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
        DatabaseReference recommandation;
        if (spinner.getSelectedItem().toString().equals("Morceau")) {
            Morceau morceau = (Morceau) recommandable;
            ajouterMorceau(morceau);
            recommandation = root.child("recommandations").child("recommandationsMorceau").push();
            recommandation.child("emetteur").setValue(emetteur);
            recommandation.child("destinataire").setValue(destinataire.getText().toString());
            recommandation.child("idMorceau").setValue(morceau.getId());
            recommandation.child("likingUsers");
            recommandation.child("supportingUsers");
            recommandation.child("dateRecommandation").setValue(new GregorianCalendar().getTime().toString());
        } else if (spinner.getSelectedItem().toString().equals("Album")) {
            Album album = (Album) recommandable;
            ajouterAlbum(album);
            recommandation = root.child("recommandations").child("recommandationsAlbum").push();
            recommandation.child("emetteur").setValue(emetteur);
            recommandation.child("destinataire").setValue(destinataire.getText().toString());
            recommandation.child("idAlbum").setValue(album.getId());
            recommandation.child("nbLikes").setValue(0);
            recommandation.child("nbAppuis").setValue(0);
            recommandation.child("dateRecommandation").setValue(new GregorianCalendar().getTime().toString());
        } else if (spinner.getSelectedItem().toString().equals("Artiste")) {
            Artiste artiste = (Artiste) recommandable;
            ajouterArtiste(artiste);
            recommandation = root.child("recommandations").child("recommandationsArtiste").push();
            recommandation.child("emetteur").setValue(emetteur);
            recommandation.child("destinataire").setValue(destinataire.getText().toString());
            recommandation.child("idArtiste").setValue(artiste.getId());
            recommandation.child("nbLikes").setValue(0);
            recommandation.child("nbAppuis").setValue(0);
            recommandation.child("dateRecommandation").setValue(new GregorianCalendar().getTime().toString());
        }
        Toast.makeText(getApplicationContext(), "Votre recommandation a bien été créée", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void ajouterMorceau(Morceau morceau) {
        DatabaseReference refMorceau = root.child("recommandables").child("morceaux");
        DatabaseReference ref = refMorceau.child(morceau.getId());
        ref.child("artiste").setValue(morceau.getArtiste());
        ref.child("duree").setValue(morceau.getDuree());
        ref.child("titre").setValue(morceau.getTitre());
        ref.child("album").setValue(morceau.getNomAlbum());
        ref.child("pictureURL").setValue(morceau.getPictureURL());
    }

    public void ajouterAlbum(Album album) {
        DatabaseReference refAlbums = root.child("recommandables").child("albums");
        DatabaseReference ref = refAlbums.child(album.getId());
        ref.child("artiste").setValue(album.getArtiste());
        ref.child("titre").setValue(album.getTitre());
        ref.child("nbTrack").setValue(album.getNbTrack());
        ref.child("pictureURL").setValue(album.getPictureURL());
    }

    public void ajouterArtiste(Artiste artiste) {
        DatabaseReference refArtistes = root.child("recommandables").child("artistes");
        DatabaseReference ref = refArtistes.child(artiste.getId());
        ref.child("nom").setValue(artiste.getNom());
        ref.child("nbAlbums").setValue(artiste.getNbAlbums());
        ref.child("pictureURL").setValue(artiste.getPictureURL());
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}