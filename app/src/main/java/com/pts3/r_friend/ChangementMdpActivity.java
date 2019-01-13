package com.pts3.r_friend;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangementMdpActivity extends AppCompatActivity {

        Button btnChgtMdp;

        EditText editTextPseudo;
        EditText editTextMdpActuel;
        EditText editTextNouveauMdp;
        EditText editTextConfirmerNouveauMdp;

        String pseudo;
        String mdpActuel;
        String nouveauMdp;
        String confirmerNouveauMdp;

        Boolean pseudoExistant;
        String mdp;

        FirebaseDatabase database;
        DatabaseReference root;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_chgt_mdp);

            database = FirebaseDatabase.getInstance();
            root = database.getReference();

            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle("");
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#26223C")));

            editTextPseudo =  (EditText) findViewById(R.id.chgtMdpPseudo);
            editTextMdpActuel =  (EditText) findViewById(R.id.editTextMdpActuel);
            editTextNouveauMdp =  (EditText) findViewById(R.id.editTextNouveauMdp1);
            editTextConfirmerNouveauMdp =  (EditText) findViewById(R.id.editTextNouveauMdp2);

            btnChgtMdp = (Button) findViewById(R.id.btnValiderChgtMdp);
            btnChgtMdp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pseudo = editTextPseudo.getText().toString();
                    mdpActuel = editTextMdpActuel.getText().toString();
                    nouveauMdp = editTextNouveauMdp.getText().toString();
                    confirmerNouveauMdp = editTextConfirmerNouveauMdp.getText().toString();
                    verifierExistance();
                }
            });
        }

    private void verifierExistance() {
        root.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pseudoExistant=dataSnapshot.hasChild(pseudo);
                mdp = dataSnapshot.child(pseudo).child("mdp").getValue(String.class);
                verification();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    private void verification() {

        if (pseudo.equals("") || pseudo.replace(" ","").equals("")) {
            Toast.makeText(getApplicationContext(), "Veuillez saisir un pseudo",Toast.LENGTH_SHORT).show();
        }
        else if(!pseudoExistant) {
            Toast.makeText(getApplicationContext(), "Pseudo inexistant",Toast.LENGTH_SHORT).show();
        }
        else if (mdpActuel.equals("")) {
            Toast.makeText(getApplicationContext(), "Veuillez votre mot de passe actuel",Toast.LENGTH_SHORT).show();
        }
        else if (!mdpActuel.equals(mdp)) {
            Toast.makeText(getApplicationContext(), "Mot de passe incorrect",Toast.LENGTH_SHORT).show();
        }
        else if (nouveauMdp.equals("")) {
            Toast.makeText(getApplicationContext(), "Veuillez saisir un nouveau mot de passe",Toast.LENGTH_SHORT).show();
        }
        else if (nouveauMdp.length() < 6) {
            Toast.makeText(getApplicationContext(), "Le nouveau mot de passe doit contenir au moins 6 caractères",Toast.LENGTH_SHORT).show();
        }
        else if (nouveauMdp.length() > 20) {
            Toast.makeText(getApplicationContext(), "Le nouveau mot de passe doit contenir au maximum 20 caractères",Toast.LENGTH_SHORT).show();
        }
        else if (confirmerNouveauMdp.equals("")) {
            Toast.makeText(getApplicationContext(), "Veuillez confirmer votre nouveau mot de passe",Toast.LENGTH_SHORT).show();
        }
        else if (!nouveauMdp.equals(confirmerNouveauMdp)) {
            Toast.makeText(getApplicationContext(), "Les nouveaux mots de passe ne correspondent pas",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"Votre mot de passe a bien été mis à jour", Toast.LENGTH_SHORT).show();
            DatabaseReference user = root.child("users").child(pseudo);
            user.child("mdp").setValue(nouveauMdp);
            finish();
        }
    }
}

