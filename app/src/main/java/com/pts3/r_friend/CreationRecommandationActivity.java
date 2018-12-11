package com.pts3.r_friend;

import android.content.DialogInterface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CreationRecommandationActivity extends AppCompatActivity {
    EditText textNomDestinataire;
    String nomRecherche;
    String nomDestinataire;
    ListView listDestinataires;
    Button buttonAjouter;
    List<String> listDest;
    ArrayAdapter<String> adapter;
    ConstraintLayout ecran;
    MainActivity context;
    private SearchView.SearchAutoComplete mSearchAutoComplete;
    private android.widget.SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_recommandation);
        ecran = findViewById(R.id.Ecran);


      /*  ArrayList<String> propositions = new ArrayList<>();
        propositions.add("blablabla");
        propositions.add("blablablaaaaa");
        propositions.add("lalalaal");
        propositions.add("zddzzdzqdzqd");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, propositions);

        SearchView.SearchAutoComplete searchAutoComplete = findViewById(R.id.searchView);

        searchAutoComplete.setAdapter(adapter);*/

        listDestinataires = findViewById(R.id.listDestinataires);
        listDestinataires.setY(ecran.getY()-5);
        buttonAjouter = findViewById(R.id.buttonAjouter);
        listDest = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,listDest);
        listDestinataires.setAdapter(adapter);
        /*buttonAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nomDestinataire = textNomDestinataire.getText().toString();
                textNomDestinataire.setText("");
                listDest.add(nomDestinataire);
                adapter.notifyDataSetChanged();
            }
        });
        listDestinataires.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                listDest.remove(position);
                adapter.notifyDataSetChanged();
            }

        });*/

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



}
