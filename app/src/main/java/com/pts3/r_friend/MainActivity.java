package com.pts3.r_friend;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Display;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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
    SwitchCompat switchMusiques;
    SwitchCompat switchArtistes;
    SearchView userSearchView;
    SearchView mainSearchView;


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

        remplirRecommandation();
        setScrollView();
       // afficherRecommandation();

        switchRecommandationsPersos = findViewById(R.id.app_bar_switch_persos);
        switchMusiques = findViewById(R.id.app_bar_switch_musiques);
        switchAlbums = findViewById(R.id.app_bar_switch_albums);
        switchArtistes = findViewById(R.id.app_bar_switch_artistes);
        userSearchView = findViewById(R.id.app_bar_search);
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

        return super.onCreateOptionsMenu(menu);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the search menu action bar.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        // Get the search menu.
        MenuItem searchMenu = menu.findItem(R.id.action_search);

        // Get SearchView object.
        SearchView searchView = (SearchView) searchMenu.getActionView();

        // Get SearchView autocomplete object.
        final  SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setBackgroundColor(Color.BLUE);
        searchAutoComplete.setTextColor(Color.WHITE);
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.holo_blue_light);

        // Create a new ArrayAdapter and add data to search auto complete object.
        String dataArr[] = {"Apple" , "Amazon" , "Amd", "Microsoft", "Microwave", "MicroNews", "Intel", "Intelligence"};
        ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, dataArr);
        searchAutoComplete.setAdapter(newsAdapter);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }*/

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
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
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
        recommandations.add(new Recommandation("Jean","Paul", "Titanic","Musique","7 janvier 1999"));
        recommandations.add(new Recommandation("Jean","Paul", "Titanic","Musique","7 janvier 1999"));
        recommandations.add(new Recommandation("Jean","Paul", "Titanic","Musique","7 janvier 1999"));
        recommandations.add(new Recommandation("Jean","Paul", "Titanic","Musique","7 janvier 1999"));
        recommandations.add(new Recommandation("Jean","Paul", "Titanic","Artiste","7 janvier 1999"));
        recommandations.add(new Recommandation("Jean","Paul", "Titanic","Artiste","7 janvier 1999"));
        recommandations.add(new Recommandation("Jean","Paul", "Titanic","Album","7 janvier 1999"));
        recommandations.add(new Recommandation("Jean","Paul", "Titanic","Album","7 janvier 1999"));
        recommandations.add(new Recommandation("Jean","Paul", "Titanic","Film","7 janvier 1999"));
        recommandations.add(new Recommandation("Jean","Paul", "Titanic","Film","7 janvier 1999"));
        recommandations.add(new Recommandation("Jean","Paul", "Titanic","Film","7 janvier 1999"));
        recommandations.add(new Recommandation("Jean","Paul", "Titanic","Film","7 janvier 1999"));
        recommandations.add(new Recommandation("Jean","Paul", "Titanic","Film","7 janvier 1999"));
    }

    public void afficherRecommandation() {
        ll.removeAllViews();
        for (Recommandation recommandation : recommandations) {
            if (recommandation.getType().equals("Musique") && switchMusiques.isChecked()
                    || recommandation.getType().equals("Artiste") && switchArtistes.isChecked()
                    || recommandation.getType().equals("Album") && switchAlbums.isChecked()
                    ){
                ll.addView(new Space(this),new LinearLayout.LayoutParams(1,size.y/50));
                ll.addView(recommandationToLinearLayout(recommandation));
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

    public LinearLayout recommandationToLinearLayout(Recommandation recommandation) {
        LinearLayout ll = new LinearLayout(this);
        LinearLayout ll2 = new LinearLayout(this);
        LinearLayout ll3 = new LinearLayout(this);
        LinearLayout ll4 = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll2.setOrientation(LinearLayout.HORIZONTAL);
        ll3.setOrientation(LinearLayout.VERTICAL);
        ll4.setOrientation(LinearLayout.HORIZONTAL);
        ll.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        TextView tv = new TextView(this);
        tv.setText(recommandation.getEmetteur()+" recommande à "+recommandation.getDestinataire());
        tv.setTypeface(null, Typeface.BOLD_ITALIC);
        ll.addView(tv);
        ll.addView(ll2);
        TextView tv2 = new TextView(this);
        tv2.setText(recommandation.getTitre());
        TextView tv3 = new TextView(this);
        tv3.setText(recommandation.getType());
        TextView tv4 = new TextView(this);
        tv4.setText(recommandation.getSortie());
        ll3.addView(tv2);
        ll3.addView(tv3);
        ll3.addView(tv4);
        ImageView imageView = new ImageView(this);
        Picasso.with(this).load("http://i.imgur.com/DvpvklR.png").into(imageView);
        ll2.addView(imageView,new LinearLayout.LayoutParams(size.x/5,size.y/5));
        ll2.addView(ll3);
        final ImageButton imageButton = new ImageButton(this);
        imageButton.setBackgroundResource(R.drawable.coeur_vide);
        ll4.addView(new Space(this),new LinearLayout.LayoutParams((size.x*95/100-size.x/5)/3,1));
        ll4.addView(imageButton,new LinearLayout.LayoutParams(size.x/10,size.x/10));
        ll4.addView(new Space(this),new LinearLayout.LayoutParams((size.x*95/100-size.x/5)/3,1));
        final ImageButton imageButton2 = new ImageButton(this);
        imageButton2.setBackgroundResource(R.drawable.one_white);
        ll4.addView(imageButton2,new LinearLayout.LayoutParams(size.x/10,size.x/10));
        ll.addView(ll4);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageButton.getBackground().equals(R.drawable.coeur_rouge)) {
                    imageButton.setBackgroundResource(R.drawable.coeur_vide);
                }
                else {
                    imageButton.setBackgroundResource(R.drawable.coeur_rouge);
                }
            }
        });

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton temp = new ImageButton(getApplicationContext());
                temp.setBackgroundResource(R.drawable.one_green);
                temp.setBackground(temp.getBackground());
                if (imageButton2.getBackground().equals(temp.getBackground())){
                    imageButton2.setBackgroundResource(R.drawable.one_white);
                }
                else {
                    imageButton2.setBackgroundResource(R.drawable.one_green);
                }
            }
        });
        return ll;
    }

    public void debug(String s) {
        Log.e("-----",s);
    }
    }
