package com.pts3.r_friend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    List<Recommandation> recommandations;
    ConstraintLayout fenetrePrincipale;
    Point size;
    ListView listView;
    SwitchCompat switchRecommandationsRecues;
    SwitchCompat switchAlbums;
    SwitchCompat switchMorceaux;
    SwitchCompat switchArtistes;
    SwitchCompat switchRecommandationsEffectuees;
    SearchView mainSearchView;
    TextView username;
    TextView userMail;
    NavigationView navigationView;
    DeezerMusicPlayer deezerMusicPlayer;

    DatabaseReference root;
    DataSnapshot dataSnapshotRecom;
    DataSnapshot dataSnapshotInfosSupp;

    SwipeRefreshLayout swipeRefreshLayout;

    String filtreSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recommandations= new ArrayList<>();
        Display display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        fenetrePrincipale = (ConstraintLayout) findViewById(R.id.fenetrePrincipale);
        fenetrePrincipale.setFocusable(true);

        root = FirebaseDatabase.getInstance().getReference();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackground(new ColorDrawable(Color.parseColor("#26223C")));
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.listView);

        swipeRefreshLayout = new SwipeRefreshLayout(this);
        fenetrePrincipale.removeAllViews();
        fenetrePrincipale.addView(swipeRefreshLayout);
        swipeRefreshLayout.addView(listView);
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!userMail.getText().toString().equals("")) {
                    Intent intent =new Intent(getApplicationContext(),CreationRecommandationActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Vous devez être connecté pour créer une recommandation", Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(getApplicationContext(),ConnexionActivity.class);
                    startActivity(intent);
                }
            }
        });

        deezerMusicPlayer = new DeezerMusicPlayer(this,getResources().getString(R.string.app_id));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        filtreSearchView="";

    }

    @Override
    protected void onStart() {
        super.onStart();

        refresh();

        if (username != null && userMail != null) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            username.setText(sharedPref.getString("pseudo","Non connecté"));
            userMail.setText(sharedPref.getString("email",""));
        }

        if (navigationView.getMenu() != null && userMail != null) {
            Boolean connected = !username.getText().toString().equals("Non connecté");
            navigationView.getMenu().findItem(R.id.buttonCompte).setVisible(connected);
            navigationView.getMenu().findItem(R.id.buttonDeconnexion).setVisible(connected);
            navigationView.getMenu().findItem(R.id.buttonConnexion).setVisible(!connected);
            navigationView.getMenu().findItem(R.id.app_bar_switch_recomEffectuees).setVisible(connected);
            navigationView.getMenu().findItem(R.id.app_bar_switch_recomRecues).setVisible(connected);
            if (!connected) {
                switchRecommandationsRecues.setChecked(false);
                switchRecommandationsEffectuees.setChecked(false);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (listView.getChildAt(0).getTop() != 0) {
            listView.smoothScrollToPosition(0);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        mainSearchView =(SearchView) searchItem.getActionView();
        mainSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filtreSearchView = query;
                afficherRecommandation();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equals("")) {
                    filtreSearchView = "";
                    afficherRecommandation();
                }
                return false;
            }
        });

        switchRecommandationsRecues = findViewById(R.id.app_bar_switch_recomRecues);
        switchRecommandationsEffectuees = findViewById(R.id.app_bar_switch_recomEffectuees);
        switchMorceaux = findViewById(R.id.app_bar_switch_morceaux);
        switchAlbums = findViewById(R.id.app_bar_switch_albums);
        switchArtistes = findViewById(R.id.app_bar_switch_artistes);

        switchArtistes.setChecked(true);
        switchAlbums.setChecked(true);
        switchMorceaux.setChecked(true);
        switchRecommandationsRecues.setChecked(false);
        switchRecommandationsEffectuees.setChecked(false);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        username = findViewById(R.id.username);
        userMail = findViewById(R.id.user_email);
        username.setText(sharedPref.getString("pseudo","Non connecté"));
        userMail.setText(sharedPref.getString("email",""));

        Boolean connected = !username.getText().toString().equals("Non connecté");
        navigationView.getMenu().findItem(R.id.buttonCompte).setVisible(connected);
        navigationView.getMenu().findItem(R.id.buttonDeconnexion).setVisible(connected);
        navigationView.getMenu().findItem(R.id.buttonConnexion).setVisible(!connected);
        navigationView.getMenu().findItem(R.id.app_bar_switch_recomEffectuees).setVisible(connected);
        navigationView.getMenu().findItem(R.id.app_bar_switch_recomRecues).setVisible(connected);


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

        switchRecommandationsRecues.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchRecommandationsRecues.isChecked()) {
                    switchRecommandationsEffectuees.setChecked(false);
                }
                afficherRecommandation();
            }
        });

        switchRecommandationsEffectuees.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchRecommandationsEffectuees.isChecked()) {
                    switchRecommandationsRecues.setChecked(false);
                }
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

        int id = item.getItemId();

        if (id == R.id.app_bar_switch_recomRecues) {
            switchRecommandationsRecues.setChecked(!switchRecommandationsRecues.isChecked());
        }

        else if (id == R.id.app_bar_switch_recomEffectuees) {
            switchRecommandationsEffectuees.setChecked(!switchRecommandationsEffectuees.isChecked());
        }

        else if (id == R.id.app_bar_switch_albums) {
            switchAlbums.setChecked(!switchAlbums.isChecked());
        }

        else if (id == R.id.app_bar_switch_artistes) {
            switchArtistes.setChecked(!switchArtistes.isChecked());
        }

        else if (id == R.id.app_bar_switch_morceaux) {
            switchMorceaux.setChecked(!switchMorceaux.isChecked());
        }

        else if (id == R.id.buttonConnexion) {
            Intent intent = new Intent(getApplicationContext(),ConnexionActivity.class);
            startActivity(intent);
        }

        else if (id == R.id.buttonDeconnexion) {
            deconnexion();
        }

        else if (id == R.id.buttonCompte) {

        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        debug(item.getItemId()+"");

        if (item.getItemId()==R.id.menu_refresh) {
            refresh();
        } else if (item.getItemId()==R.id.action_search) {

        }

        return super.onOptionsItemSelected(item);

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
                recommandations.clear();
                createRecommandation("morceau");
                createRecommandation("album");
                createRecommandation("artiste");
                ordonnerRecommandation();
                afficherRecommandation();
                swipeRefreshLayout.setRefreshing(false);
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
            String idRecommandation = dataRecom.getKey();
            Long date = dataRecom.child("dateRecommandation").getValue(Long.class);
            String dest = dataRecom.child("destinataire").getValue(String.class);
            String emet = dataRecom.child("emetteur").getValue(String.class);
            List<String> likingUsers = new ArrayList<>();
            List<String> supportingUsers = new ArrayList<>();
            List<Commentaire> commentaires = new ArrayList<>();
            Iterator<DataSnapshot> it = dataRecom.child("likingUsers").getChildren().iterator();
            while (it.hasNext()) {
                likingUsers.add(it.next().getValue().toString());
            }
            it = dataRecom.child("supportingUsers").getChildren().iterator();
            while (it.hasNext()) {
                supportingUsers.add(it.next().getValue().toString());
            }
            it = dataRecom.child("commentaires").getChildren().iterator();
            while (it.hasNext()) {
                DataSnapshot temp = it.next();
                commentaires.add(new Commentaire(temp.child("redacteur").getValue().toString(),
                        temp.child("message").getValue().toString(),
                        temp.child("date").getValue(Long.class)));
            }

            String idInfosSupp = dataRecom.child(infosSuppID).getValue(String.class);

            Iterator<DataSnapshot> j = dataSnapshotInfosSupp.child(infosSuppChild).getChildren().iterator();
            while (j.hasNext()) {
                DataSnapshot dataInfosSupp = j.next();
                if (dataInfosSupp.getKey().equals(idInfosSupp)) {
                    switch(type) {
                        case "morceau":
                            String artisteMorceau = dataInfosSupp.child("artiste").getValue(String.class);
                            Integer dureeSecondes = dataInfosSupp.child("duree").getValue(Integer.class);
                            String dureeMinutes = dureeSecondes/60 + "min" + dureeSecondes%60 + "s";
                            String titreMorceau = dataInfosSupp.child("titre").getValue(String.class);
                            String imgAlb = dataInfosSupp.child("pictureURL").getValue(String.class);
                            String nomAlbum = dataInfosSupp.child("album").getValue(String.class);
                            String idMorceau = dataInfosSupp.getKey().toString();
                            recommandations.add(new MorceauRecom(idRecommandation, date, dest, emet, imgAlb, likingUsers, supportingUsers,commentaires,artisteMorceau, dureeMinutes, titreMorceau, nomAlbum,idMorceau));
                            break;

                        case "album":
                            Integer nbTracks = dataInfosSupp.child("nbTrack").getValue(Integer.class);
                            String artisteAlbum = dataInfosSupp.child("artiste").getValue(String.class);
                            String titreAlbum = dataInfosSupp.child("titre").getValue(String.class);
                            String imgAlbum = dataInfosSupp.child("pictureURL").getValue(String.class);
                            String idAlbum = dataInfosSupp.getKey().toString();
                            recommandations.add(new AlbumRecom(idRecommandation, date, dest, emet, imgAlbum, likingUsers, supportingUsers,commentaires, artisteAlbum, nbTracks, titreAlbum, idAlbum));
                            break;

                        case "artiste":
                            String nom = dataInfosSupp.child("nom").getValue(String.class);
                            Integer nbAlbums = dataInfosSupp.child("nbAlbums").getValue(Integer.class);
                            String picture = dataInfosSupp.child("pictureURL").getValue(String.class);
                            recommandations.add(new ArtisteRecom(idRecommandation, date, dest, emet, picture, likingUsers, supportingUsers,commentaires, nom, nbAlbums));
                            break;
                    }
                    break; //pour ne pas parcourir tous les autres infosSuppChildren
                }
            }
        }

    }

    public void afficherRecommandation() {
        List<Recommandation> temp = new ArrayList<>();
        for (Recommandation recommandation : recommandations) {
            if (filtreSearchView.equals("") || recommandation.contains(filtreSearchView)) {
                if (recommandation instanceof MorceauRecom && switchMorceaux.isChecked()
                        || recommandation instanceof ArtisteRecom && switchArtistes.isChecked()
                        || recommandation instanceof AlbumRecom && switchAlbums.isChecked()
                        ){
                    if (switchRecommandationsEffectuees.isChecked()) {
                        if (recommandation.getEmetteur().equals(username.getText().toString())) temp.add(recommandation);
                    }
                    else if (switchRecommandationsRecues.isChecked()) {
                        if (recommandation.getDestinataire().equals(username.getText().toString())) temp.add(recommandation);
                    }
                    else {
                        temp.add(recommandation);
                    }

                }
            }
        }

        RecommandationAdapter adapter = new RecommandationAdapter(MainActivity.this, temp);
        listView.setAdapter(adapter);
    }

    public void deconnexion() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("pseudo");
        editor.remove("email");
        editor.commit();
        username.setText("Non connecté");
        userMail.setText("");
        Toast.makeText(getApplicationContext(), "Déconnexion réussie", Toast.LENGTH_SHORT).show();

        navigationView.getMenu().findItem(R.id.buttonCompte).setVisible(false);
        navigationView.getMenu().findItem(R.id.buttonDeconnexion).setVisible(false);
        navigationView.getMenu().findItem(R.id.buttonConnexion).setVisible(true);
        navigationView.getMenu().findItem(R.id.app_bar_switch_recomRecues).setVisible(false);
        navigationView.getMenu().findItem(R.id.app_bar_switch_recomEffectuees).setVisible(false);
        switchRecommandationsRecues.setChecked(false);
        switchRecommandationsEffectuees.setChecked(false);

        afficherRecommandation();
    }

    public int getNavBarHeight() {
        int resourceId = this.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return this.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public int getActionBarHeight() {
        return getSupportActionBar().getHeight();
    }

    public Point getSize() {
        return size;
    }

    public void debug(String s) {
        Log.e("-----",s);
    }

    public void refresh() {
        recommandations.clear();
        afficherRecommandation();
        remplirRecommandation();
        deezerMusicPlayer.stopMorceau();
        deezerMusicPlayer.stopAlbum();
    }

    public void ordonnerRecommandation() {
        Comparator<Recommandation> comparator = new Comparator<Recommandation>() {
            @Override
            public int compare(Recommandation o1, Recommandation o2) {
                if (o1.getDateRecommandation() < o2.getDateRecommandation()) {
                    return 1;
                }
                return -1;
            }
        };
        Collections.sort(recommandations,comparator);
    }

    @Override
    protected void onPause() {
        super.onPause();
        deezerMusicPlayer.stopAlbum();
        deezerMusicPlayer.stopMorceau();
       // mainSearchView.
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

