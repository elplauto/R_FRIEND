package com.pts3.r_friend;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    List<Recommandation> recommandations;
    ConstraintLayout fenetrePrincipale;
    ScrollView sv;
    LinearLayout ll;
    Point size;
    SwitchCompat switchRecommandationsPersos;
    SwitchCompat switchAlbums;
    SwitchCompat switchMusiques;
    SwitchCompat switchArtistes;
    SearchView userSearchView;
    SearchView mainSearchView;
    DeezerManager deezerManager;
    FirebaseManager firebaseManager;
    DatabaseReference root;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recommandations= new ArrayList<>();
        Display display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        fenetrePrincipale = (ConstraintLayout) findViewById(R.id.fenetrePrincipale);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        remplirRecommandation();
        setScrollView();
       // afficherRecommandation();

        deezerManager = new DeezerManager(this,getResources().getString(R.string.app_id));

        switchRecommandationsPersos = findViewById(R.id.app_bar_switch_persos);
        switchMusiques = findViewById(R.id.app_bar_switch_musiques);
        switchAlbums = findViewById(R.id.app_bar_switch_albums);
        switchArtistes = findViewById(R.id.app_bar_switch_artistes);
        userSearchView = findViewById(R.id.app_bar_search);

        firebaseManager = new FirebaseManager(this);



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

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        switchRecommandationsPersos = findViewById(R.id.app_bar_switch_persos);
        switchMusiques = findViewById(R.id.app_bar_switch_musiques);
        switchAlbums = findViewById(R.id.app_bar_switch_albums);
        switchArtistes = findViewById(R.id.app_bar_switch_artistes);
        userSearchView = findViewById(R.id.app_bar_search);

        ImageView paramsIcon = findViewById(R.id.imageView);
        paramsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(getApplicationContext(),ConnexionActivity.class);
                 startActivity(intent);
            }
        });

        switchAlbums.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                afficherRecommandation();
            }
        });
        switchMusiques.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        userSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                deezerManager.rechercheMusique(query);
                deezerManager.rechercheArtiste(query);
                deezerManager.rechercheAlbum(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

              //  Intent intent = new Intent(MainActivity.this,MainActivity2.class);
                return false;
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

        else if (id == R.id.app_bar_switch_musiques) {
            if (switchMusiques.isChecked()) {
                switchMusiques.setChecked(false);
            } else {
                switchMusiques.setChecked(true);
            }
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
       // drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void remplirRecommandation() {

        //recommandation de musiques
        recommandations.add(new MusiqueRecom("Jean", "Paul", 3, 5, "Sum41", "3:00", "Blood in my Eyes"));
        recommandations.add(new MusiqueRecom("", "Hervé", 46, 6,"Sum41", "3:50", "Screaming Bloody Murders"));
        recommandations.add(new MusiqueRecom("Huguette", "Josianne", 14, 23, "Cannibal Corpse", "17:23", "I Cum Blood"));

        //recommandation d'artistes
        recommandations.add(new GroupeRecom("Gilbert", "Claudette", 12, 256, "Pascal Obispo"));
        recommandations.add(new GroupeRecom("Martin", "Baptiste", 0, 0, "Orelsan"));
        recommandations.add(new GroupeRecom("", "Sylvie", 61, 36, "Jean Michel Jarre"));
        recommandations.add(new GroupeRecom("Françoise", "Patrick", 18, 41, "Francky Vincent"));

        //recommandation d'albums
        recommandations.add(new AlbumRecom("Foxxx", "Sasha", 1274, 26,"Amy Winehouse", 13, "Back to Black", "/"));
        recommandations.add(new AlbumRecom("Alberto", "Jacques", 3, 14,"Megadeth", 9, "Rust in Peace", "/"));


        //il faudrait récup les recommandations depuis la bdd
        root.child("recommandations").child("recommandationsAlbum").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> i = dataSnapshot.getChildren().iterator();
                while (i.hasNext()) {
                    DataSnapshot data = i.next();

                    String dest = data.child("destinataire").getValue().toString();
                    String emet = data.child("emetteur").getValue().toString();
                    int nbLikes = Integer.parseInt(data.child("nbLikes").getValue().toString());
                    int nbAppuis = Integer.parseInt(data.child("nbappuis").getValue().toString());

                    //groupe, nbTracks, imgAlb et titre depuis recherche sur l'album
                    String idAlb = data.child("idAlbum").getValue().toString();
                    int nbTracks = searchNbTracksFromAlbum(idAlb);


                }

                //il va falloir recup isLiked et isSupported depuis une autre branche de la bdd
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                debug("erreur");
            }
        });

    }

    private String idAlb;
    public int searchNbTracksFromAlbum(String idAlb) {
        root.child("albums").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> i = dataSnapshot.getChildren().iterator();
                while (i.hasNext()) {
                    DataSnapshot data = i.next();
                    if (data.getValue().toString().equals(idAlb)) {

                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                debug("erreur");
            }
        });
    }

    public void afficherRecommandation() {
        ll.removeAllViews();
        for (Recommandation recommandation : recommandations) {
            if (recommandation instanceof MusiqueRecom && switchMusiques.isChecked()
                    || recommandation instanceof GroupeRecom && switchArtistes.isChecked()
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

    public void rechercheMusiqueReponse(List<Musique> musiques) {
        for (Musique musique : musiques) {
            firebaseManager.ajouterMusique(musique);
        }
    }

    public void rechercheAlbumReponse(List<com.pts3.r_friend.Album> albums) {
        for (com.pts3.r_friend.Album album : albums) {
            firebaseManager.ajouterAlbum(album);
        }
    }

    public void rechercheArtisteReponse(List<Artiste> artistes) {
        for (Artiste artiste : artistes) {
            firebaseManager.ajouterArtiste(artiste);
        }
    }
}
