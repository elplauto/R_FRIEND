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
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, InternetConnectivityListener {

    List<Recommandation> recommandations;
    List<Interraction> interractions;
    List<Affichable> fluxCharge;
    List<Affichable> fluxAffiche;
    ConstraintLayout fenetrePrincipale;
    Point size;
    ListView listView;
    SwitchCompat switchRecommandationsRecues;
    SwitchCompat switchAlbums;
    SwitchCompat switchMorceaux;
    SwitchCompat switchArtistes;
    SwitchCompat switchRecommandationsEffectuees;
    SwitchCompat switchInterractions;
    SearchView mainSearchView;
    TextView username;
    TextView userMail;
    NavigationView navigationView;
    DeezerMusicPlayer deezerMusicPlayer;

    DatabaseReference root;
    DataSnapshot dataSnapshotRecom;
    DataSnapshot dataSnapshotInfosSupp;
    DataSnapshot dataSnapshotInterraction;

    SwipeRefreshLayout swipeRefreshLayout;

    String filtreSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recommandations= new ArrayList<>();
        interractions= new ArrayList<>();
        fluxCharge = new ArrayList<>();
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

        InternetAvailabilityChecker.init(this);
        InternetAvailabilityChecker mInternetAvailabilityChecker;
        mInternetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
        mInternetAvailabilityChecker.addInternetConnectivityListener(this);

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
       /* SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        listView.smoothScrollToPosition(sharedPref.getInt("firstItemVisible",0));
        debug(sharedPref.getInt("firstItemVisible",0)+"------------------");*/

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
                afficherFlux();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equals("")) {
                    filtreSearchView = "";
                    afficherFlux();
                }
                return false;
            }
        });

        switchRecommandationsRecues = findViewById(R.id.app_bar_switch_recomRecues);
        switchRecommandationsEffectuees = findViewById(R.id.app_bar_switch_recomEffectuees);
        switchMorceaux = findViewById(R.id.app_bar_switch_morceaux);
        switchAlbums = findViewById(R.id.app_bar_switch_albums);
        switchArtistes = findViewById(R.id.app_bar_switch_artistes);
        switchInterractions = findViewById(R.id.app_bar_switch_interractions);

        switchArtistes.setChecked(true);
        switchAlbums.setChecked(true);
        switchMorceaux.setChecked(true);
        switchInterractions.setChecked(true);
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
                afficherFlux();
            }
        });
        switchMorceaux.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                afficherFlux();
            }
        });
        switchArtistes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                afficherFlux();
            }
        });
        switchInterractions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                afficherFlux();
            }
        });

        switchRecommandationsRecues.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                afficherFlux();
            }
        });

        switchRecommandationsEffectuees.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                afficherFlux();
            }
        });

        remplirInterraction();

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

        else if (id == R.id.app_bar_switch_interractions) {
            switchInterractions.setChecked(!switchInterractions.isChecked());
        }

        else if (id == R.id.buttonConnexion) {
            Intent intent = new Intent(getApplicationContext(),ConnexionActivity.class);
            startActivity(intent);
        }

        else if (id == R.id.buttonDeconnexion) {
            deconnexion();
        }

        else if (id == R.id.buttonCompte) {
            Intent intent = new Intent(getApplicationContext(),ChangementMdpActivity.class);
            startActivity(intent);
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

    public void remplirInterraction() {
        root.child("interractions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                dataSnapshotInterraction = ds;
                interractions.clear();
                createInterraction();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                debug("erreur");
            }
        });
    }

    public void createInterraction() {
        Iterator<DataSnapshot> i = dataSnapshotInterraction.getChildren().iterator();
        while (i.hasNext()) {
            DataSnapshot dataInterraction = i.next();
            String idRecommandation = dataInterraction.child("idRecommandation").getValue(String.class);
            Long date = dataInterraction.child("date").getValue(Long.class);
            String typeInterraction = dataInterraction.child("typeInterraction").getValue(String.class);
            String typeRecommandation = dataInterraction.child("typeRecommandation").getValue(String.class);
            String user = dataInterraction.child("user").getValue(String.class);
            String nomObjet = dataInterraction.child("nom").getValue(String.class);

            String phrase="";
            if (typeInterraction.equals("commentaire")) {
                phrase += user + " a commenté";
            } else if (typeInterraction.equals("like")) {
                phrase += user + " a aimé";
            } else if (typeInterraction.equals("plusun")) {
                phrase += user + " a appuyé";
            }

            if (typeRecommandation.equals("morceau")) {
                phrase += " une recommandation concernant le morceau " + "\"" + nomObjet +"\"";
            } else if (typeRecommandation.equals("album")) {
                phrase += " une recommandation concernant l'album " + "\"" + nomObjet +"\"";
            } else if (typeRecommandation.equals("artiste")) {
                phrase += " une recommandation concernant l'artiste " + "\"" + nomObjet +"\"";
            }

            interractions.add(new Interraction(phrase,typeRecommandation,idRecommandation,date));

        }
        remplirRecommandation();
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
                afficherFlux();
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
                            String idTitre = dataInfosSupp.child("idTitre").getValue(Long.class)+"";
                            recommandations.add(new ArtisteRecom(idRecommandation, date, dest, emet, picture, likingUsers, supportingUsers,commentaires, nom, nbAlbums,idTitre));
                            break;
                    }
                    break; //pour ne pas parcourir tous les autres infosSuppChildren
                }
            }
        }

    }

    public void afficherFlux() {
        fluxCharge = new ArrayList<>();
        for (Recommandation recommandation : recommandations) {
            if (filtreSearchView.equals("") || recommandation.contains(filtreSearchView)) {
                if (recommandation instanceof MorceauRecom && switchMorceaux.isChecked()
                        || recommandation instanceof ArtisteRecom && switchArtistes.isChecked()
                        || recommandation instanceof AlbumRecom && switchAlbums.isChecked()
                        ){
                    if (switchRecommandationsEffectuees.isChecked()) {
                        if (recommandation.getEmetteur().equals(username.getText().toString())) {
                            fluxCharge.add(recommandation);
                            continue;
                        }
                    }
                    if (switchRecommandationsRecues.isChecked()) {
                        if (recommandation.getDestinataire().equals(username.getText().toString())) {
                            fluxCharge.add(recommandation);
                            continue;
                        }
                    }
                    if (!switchRecommandationsRecues.isChecked() && !switchRecommandationsEffectuees.isChecked()) {
                        fluxCharge.add(recommandation);
                    }

                }
            }
        }
        List<Interraction> temp = new ArrayList<>();
        for (Interraction interraction : interractions) {
            for (Affichable each : fluxCharge) {
                Recommandation recommandation = (Recommandation) each;
                if (switchInterractions.isChecked() && recommandation.getIdRecommandation().equals(interraction.getIdRecommandation())
                        && ((recommandation instanceof MorceauRecom && interraction.getType().equals("morceau"))
                        || ((recommandation instanceof AlbumRecom && interraction.getType().equals("album"))
                        || (recommandation instanceof ArtisteRecom && interraction.getType().equals("artiste"))))) {
                    temp.add(interraction);
                    break;
                }
            }
        }
        fluxCharge.addAll(temp);

        AffichableAdapter adapter = new AffichableAdapter(MainActivity.this, ordonnerFlux(fluxCharge));
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

        afficherFlux();
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
        interractions.clear();
        recommandations.clear();
        fluxCharge.clear();
        afficherFlux();
        remplirInterraction();
        deezerMusicPlayer.stopMorceau();
        deezerMusicPlayer.stopAlbum();
    }

    public List<Affichable> ordonnerFlux(List<Affichable> liste) {
        Comparator<Affichable> comparator = new Comparator<Affichable>() {
            @Override
            public int compare(Affichable o1, Affichable o2) {
                Long t1=0l,t2=0l;
                if (o1 instanceof Recommandation) {
                    t1= ((Recommandation) o1).getDateRecommandation();
                } else if (o1 instanceof Interraction) {
                    t1 = ((Interraction) o1).getDate();
                }
                if (o2 instanceof Recommandation) {
                    t2= ((Recommandation) o2).getDateRecommandation();
                } else if (o2 instanceof Interraction) {
                    t2 = ((Interraction) o2).getDate();
                }

                if (t1 < t2) {
                    return 1;
                } else if (t1 > t2) {
                    return -1;
                }
                return 0;
            }
        };
        Collections.sort(liste,comparator);
        return liste;
    }

    @Override
    protected void onPause() {
        super.onPause();
        deezerMusicPlayer.stopAlbum();
        deezerMusicPlayer.stopMorceau();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("firstItemVisible",listView.getFirstVisiblePosition());
        editor.commit();
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        String msg;
        if (isConnected) {
            msg="Vous êtes connecté à internet";
        } else {
            msg="Vous n'êtes pas connecté à internet, de nombreuses fonctionnalités ne sont plus disponibles";
        }
        Toast.makeText(getApplication().getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }

    public void scrollToItem(String idRecommandation, String typeRecommandation) {

        for (int i = 0; i < fluxCharge.size(); i++) {
            Recommandation recommandation=null;
            if (fluxCharge.get(i) instanceof Recommandation) {
                recommandation = (Recommandation) fluxCharge.get(i);
            } else {
                continue;
            }

            if (recommandation instanceof  MorceauRecom && typeRecommandation.equals("morceau") && recommandation.getIdRecommandation().equals(idRecommandation)) {
                listView.smoothScrollToPositionFromTop(i,0);
                break;
            } else if (recommandation instanceof  AlbumRecom && typeRecommandation.equals("album") && recommandation.getIdRecommandation().equals(idRecommandation)) {
                listView.smoothScrollToPositionFromTop(i,0);
                break;
            } else if (recommandation instanceof  ArtisteRecom && typeRecommandation.equals("artiste") && recommandation.getIdRecommandation().equals(idRecommandation)) {
                listView.smoothScrollToPositionFromTop(i,0);
                break;
            }
        }
    }
}

