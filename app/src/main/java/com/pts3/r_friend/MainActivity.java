package com.pts3.r_friend;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Space;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    List<Recommandation> recommandations;
    ConstraintLayout fenetrePrincipale;
    ScrollView sv;
    LinearLayout ll;
    Point size;
    SwitchCompat switchRecommandationsPersos;
    SwitchCompat switchAlbums;
    SwitchCompat switchMorceaux;
    SwitchCompat switchArtistes;
    SearchView mainSearchView;

    DatabaseReference root;
    DataSnapshot dataSnapshotRecom;
    DataSnapshot dataSnapshotInfosSupp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recommandations= new ArrayList<>();
        Display display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        fenetrePrincipale = (ConstraintLayout) findViewById(R.id.fenetrePrincipale);

        root = FirebaseDatabase.getInstance().getReference();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackground(new ColorDrawable(Color.parseColor("#26223C")));
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getApplicationContext(),CreationRecommandationActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setScrollView();
    }

    public Point getSize() {
        return size;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);

     //   mainSearchView =( SearchView) searchItem.getActionView();

        /*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //votre code ici
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });*/

        switchRecommandationsPersos = findViewById(R.id.app_bar_switch_persos);
        switchMorceaux = findViewById(R.id.app_bar_switch_morceaux);
        switchAlbums = findViewById(R.id.app_bar_switch_albums);
        switchArtistes = findViewById(R.id.app_bar_switch_artistes);
        switchArtistes.setChecked(true);
        switchAlbums.setChecked(true);
        switchMorceaux.setChecked(true);
        switchAlbums.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                afficherRecommandation();
            }
        });
        switchMorceaux.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                afficherRecommandation();
            }
        });
        switchArtistes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                afficherRecommandation();
            }
        });
        remplirRecommandation();

        return super.onCreateOptionsMenu(menu);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        ImageView paramsIcon = findViewById(R.id.imageView);
        paramsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(getApplicationContext(),ConnexionActivity.class);
                 startActivity(intent);
            }
        });
        int id = item.getItemId();

        if (id == R.id.app_bar_switch_persos) {

            if (switchRecommandationsPersos.isChecked()) {
                switchRecommandationsPersos.setChecked(false);
            } else {
                switchRecommandationsPersos.setChecked(true);
            }
        }

        else if (id == R.id.app_bar_switch_albums) {
            if (switchAlbums.isChecked()) {
                switchAlbums.setChecked(false);
            } else {
                switchAlbums.setChecked(true);
            }
        }

        else if (id == R.id.app_bar_switch_artistes) {
            if (switchArtistes.isChecked()) {
                switchArtistes.setChecked(false);
            } else {
                switchArtistes.setChecked(true);
            }
        }

        else if (id == R.id.app_bar_switch_morceaux) {
            if (switchMorceaux.isChecked()) {
                switchMorceaux.setChecked(false);
            } else {
                switchMorceaux.setChecked(true);
            }
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
       // drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void remplirRecommandation() {

        root.child("recommandations").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                dataSnapshotRecom = ds;
                recupDataSnapshotFromRecommandable();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                debug("erreur");
            }
        });

    }

    public void recupDataSnapshotFromRecommandable () {
        root.child("recommandables").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                dataSnapshotInfosSupp = ds;
                createRecommandation("morceau");
                createRecommandation("album");
                createRecommandation("artiste");
                afficherRecommandation();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                debug("erreur");
            }
        });

    }

    public void createRecommandation(String type) {
        String recomChild = "";
        String infosSuppChild = "";
        String infosSuppID = "";
        switch(type) {
            case "morceau":
                recomChild = "recommandationsMorceau";
                infosSuppChild = "morceaux";
                infosSuppID = "idMorceau";
                break;
            case "album":
                recomChild = "recommandationsAlbum";
                infosSuppChild = "albums";
                infosSuppID = "idAlbum";
                break;
            case "artiste":
                recomChild = "recommandationsArtiste";
                infosSuppChild = "artistes";
                infosSuppID = "idArtiste";
                break;
            default: Log.i("stp_marche", "Type inconnu : " + type);
        }
        Iterator<DataSnapshot> i = dataSnapshotRecom.child(recomChild).getChildren().iterator();
        while (i.hasNext()) {
            DataSnapshot dataRecom = i.next();
            String dest = dataRecom.child("destinataire").getValue(String.class);
            String emet = dataRecom.child("emetteur").getValue(String.class);
            int nbLikes = dataRecom.child("nbLikes").getValue(Integer.class);
            int nbAppuis = dataRecom.child("nbAppuis").getValue(Integer.class);

            String idInfosSupp = dataRecom.child(infosSuppID).getValue(String.class);
            Log.i("stp_marche", "ID infos supp (pour " + type + ") : " + idInfosSupp);

            Iterator<DataSnapshot> j = dataSnapshotInfosSupp.child(infosSuppChild).getChildren().iterator();
            while (j.hasNext()) {
                DataSnapshot dataInfosSupp = j.next();
                Log.i("stp_marche", "data : " + dataInfosSupp);
                Log.i("stp_marche", "trying to create recom");
                if (dataInfosSupp.getKey().equals(idInfosSupp)) {
                    Log.i("stp_marche", "c'est le bon album");
                    Log.i("stp_marche", "type : " + type);
                    switch(type) {
                        case "morceau":
                            String artisteMorceau = dataInfosSupp.child("artiste").getValue(String.class);
                            Integer dureeSecondes = dataInfosSupp.child("duree").getValue(Integer.class);
                            String dureeMinutes = dureeSecondes/60 + "min" + dureeSecondes%60 + "s";
                            String titreMorceau = dataInfosSupp.child("titre").getValue(String.class);
                            String imgAlb = dataInfosSupp.child("pictureURL").getValue(String.class);
                            String nomAlbum = dataInfosSupp.child("album").getValue(String.class);
                            recommandations.add(new MorceauRecom(dest, emet, nbLikes, nbAppuis, imgAlb, artisteMorceau, dureeMinutes, titreMorceau, nomAlbum));
                            Log.i("stp_marche", "Création recommandation morceau");
                            break;

                        case "album":
                            Integer nbTracks = dataInfosSupp.child("nbTrack").getValue(Integer.class);
                            String artisteAlbum = dataInfosSupp.child("artiste").getValue(String.class);
                            String titreAlbum = dataInfosSupp.child("titre").getValue(String.class);
                            String imgAlbum = dataInfosSupp.child("pictureURL").getValue(String.class);
                            recommandations.add(new AlbumRecom(dest, emet, nbLikes, nbAppuis, imgAlbum, artisteAlbum, nbTracks, titreAlbum));
                            Log.i("stp_marche", "Création recommandation album");
                            break;

                        case "artiste":
                            String nom = dataInfosSupp.child("nom").getValue(String.class);
                            Integer nbAlbums = dataInfosSupp.child("nbAlbums").getValue(Integer.class);
                            String picture = dataInfosSupp.child("pictureURL").getValue(String.class);
                            recommandations.add(new ArtisteRecom(dest, emet, nbLikes, nbAppuis, picture, nom, nbAlbums));
                            Log.i("stp_marche", "Création recommandation artiste");
                            break;

                        default: Log.i("stp_marche", "Type inconnu : " + type);
                    }
                    break; //pour ne pas parcourir tous les autres infosSuppChildren
                }
            }
        }

    }

    public void afficherRecommandation() {
        ll.removeAllViews();
        for (Recommandation recommandation : recommandations) {
            if (recommandation instanceof MorceauRecom && switchMorceaux.isChecked()
                    || recommandation instanceof ArtisteRecom && switchArtistes.isChecked()
                    || recommandation instanceof AlbumRecom && switchAlbums.isChecked()
                    ){
                ll.addView(new Space(this),new LinearLayout.LayoutParams(1,size.y/50));
                Log.i("llcenter", "0");
                ll.addView(recommandation.toLinearLayout(this));
            }
        }
        if (ll.getChildCount()==0) {
            TextView aucuneRecommandation = new TextView(this);
            aucuneRecommandation.setText("Aucune recommandation à afficher");
            ll.addView(aucuneRecommandation);

        }
    }

    public void setScrollView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size.x*95/100,size.y);
        sv = new ScrollView(this);
        fenetrePrincipale.addView(sv,new LinearLayout.LayoutParams(size.x,size.y));
        ll = new LinearLayout(this);
        ll.setLayoutParams(params);
        sv.addView(ll);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setX((size.x-size.x*95/100)/2);
    }

    public void debug(String s) {
        Log.e("-----",s);
    }

}

