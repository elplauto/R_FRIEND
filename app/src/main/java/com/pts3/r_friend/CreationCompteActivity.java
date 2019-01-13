package com.pts3.r_friend;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreationCompteActivity extends AppCompatActivity {

    Button btnValider;
    EditText editTextEmail;
    EditText editTextPseudo;
    EditText editTextMdp;
    EditText editTextConfirmerMdp;
    String email;
    String pseudo;
    String mdp;
    String confirmerMdp;
    FirebaseDatabase database;
    DatabaseReference root;
    boolean pseudoDispo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_compte);

        btnValider = findViewById(R.id.btnCreerCompte);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPseudo = findViewById(R.id.editTextPseudo);
        editTextMdp = findViewById(R.id.editTextNouveauMdp1);
        editTextConfirmerMdp = findViewById(R.id.editTextNouveauMdp2);

        database = FirebaseDatabase.getInstance();
        root = database.getReference();

        btnValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editTextEmail.getText().toString();
                pseudo = editTextPseudo.getText().toString();
                mdp = editTextMdp.getText().toString();
                confirmerMdp = editTextConfirmerMdp.getText().toString();
                verifierDispoPseudo();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#26223C")));
    }

    private void verifierDispoPseudo() {
        root.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pseudoDispo=!dataSnapshot.hasChild(pseudo);
                verification();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                debug("erreur");
            }
        });
    }

    private void verification() {
        if (email.equals("")){
            Toast.makeText(getApplicationContext(), "Veuillez saisir une adresse mail",Toast.LENGTH_SHORT).show();
        }
        else if (pseudo.equals("") || pseudo.replace(" ","").equals("")) {
            Toast.makeText(getApplicationContext(), "Veuillez saisir un pseudo",Toast.LENGTH_SHORT).show();
        }
        else if (pseudo.length() < 3) {
            Toast.makeText(getApplicationContext(), "Le pseudo doit contenir au moins 3 caractères",Toast.LENGTH_SHORT).show();
        }
        else if (pseudo.length() > 20) {
            Toast.makeText(getApplicationContext(), "Le pseudo doit contenir au maximum 20 caractères",Toast.LENGTH_SHORT).show();
        }
        else if(!pseudoDispo || pseudo.equals("Non connecté")) {
            Toast.makeText(getApplicationContext(), "Pseudo indisponible",Toast.LENGTH_SHORT).show();
        }
        else if (mdp.equals("")) {
            Toast.makeText(getApplicationContext(), "Veuillez saisir un mot de passe",Toast.LENGTH_SHORT).show();
        }
        else if (mdp.length() < 6) {
            Toast.makeText(getApplicationContext(), "Le mot de passe doit contenir au moins 6 caractères",Toast.LENGTH_SHORT).show();
        }
        else if (mdp.length() > 20) {
            Toast.makeText(getApplicationContext(), "Le mot de passe doit contenir au maximum 20 caractères",Toast.LENGTH_SHORT).show();
        }
        else if (confirmerMdp.equals("")) {
            Toast.makeText(getApplicationContext(), "Veuillez confirmer le mot de passe",Toast.LENGTH_SHORT).show();
        }
        else if (!mdp.equals(confirmerMdp)) {
            Toast.makeText(getApplicationContext(), "Les mots de passe ne correspondent pas",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"Votre compte vient d'être créé", Toast.LENGTH_SHORT).show();
            DatabaseReference user = root.child("users").child(pseudo);
            user.child("mdp").setValue(mdp);
            user.child("adresseMail").setValue(email);
            editTextEmail.setText("");
            editTextPseudo.setText("");
            editTextMdp.setText("");
            editTextConfirmerMdp.setText("");
            finish();
        }
    }

    public void debug(String s) {
        Log.e("-----",s);
    }




}

