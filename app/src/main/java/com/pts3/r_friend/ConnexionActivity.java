package com.pts3.r_friend;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ConnexionActivity extends AppCompatActivity {

    Button btnConnexion;
    Button btnCreerCompte;
    EditText editTextPseudo;
    EditText editTextMDP;
    String pseudo;
    String mdp;
    TextView mdpOublie;
    FirebaseDatabase database;
    DatabaseReference root;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_connexion);
        database = FirebaseDatabase.getInstance();
        root = database.getReference();

        btnConnexion = findViewById(R.id.btnConnexion);
        btnCreerCompte = findViewById(R.id.btnCreerCompte);
        editTextPseudo = findViewById(R.id.textPseudo);
        editTextMDP = findViewById(R.id.textMDP);
        mdpOublie = findViewById(R.id.textMdpOublie);

        btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pseudo = editTextPseudo.getText().toString();
                mdp = editTextMDP.getText().toString();
                if (pseudo.equals("")) {
                    tentativeDeConnexion(false, "Veuillez saisir un pseudo");
                }
                else if (mdp.equals("")){
                    tentativeDeConnexion(false, "Veuillez saisir un mot de passe");
                }
                else {
                    verification();
                }
            }
        });

        btnCreerCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnexionActivity.this,CreationCompteActivity.class);
                startActivity(intent);
            }
        });

        mdpOublie.setClickable(true);

        mdpOublie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnexionActivity.this,MdpOublieActivity.class);
                startActivity(intent);
            }
        });
    }

    private void verification() {
        root.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean pseudoExistant=dataSnapshot.hasChild(pseudo);
                if (pseudoExistant) {
                    if (mdp.equals(dataSnapshot.child(pseudo).child("mdp").getValue().toString())) {
                        tentativeDeConnexion(true, "Connexion réussie");
                    } else {
                        tentativeDeConnexion(false, "Le pseudo et le mot de passe ne correspondent pas");
                    }
                } else {
                    tentativeDeConnexion(false, "Pseudo inexistant");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                debug("erreur");
            }
        });
    }

    private void tentativeDeConnexion(boolean connexionReussie, String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        if (connexionReussie) {
           connexion();
        }
    }

    private void connexion () {
        root.child("users").child(pseudo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("pseudo",pseudo);
                editor.putString("email",dataSnapshot.child("adresseMail").getValue().toString());
                editor.commit();
                finish();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                debug("erreur");
            }
        });
    }



    public void debug(String s) {
        Log.e("-----",s);
    }
}

