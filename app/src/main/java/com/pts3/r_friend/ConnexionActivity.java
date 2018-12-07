package com.pts3.r_friend;

import android.content.Intent;
import android.support.annotation.NonNull;
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

    Button buttonConnexion;
    Button buttonCreerCompte;
    EditText editTextPseudo;
    String textPseudo;
    String textMDP;
    String pseudo;
    String mdp;
    EditText editTextMDP;
    TextView mdpOublier;
    boolean compatibilité = false;
    MainActivity context;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRefPseudo;
    DatabaseReference myRefMdp;
    DatabaseReference refNBId = database.getReference("nbId");
    int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_connexion);
        refNBId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                id = dataSnapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        buttonConnexion = findViewById(R.id.buttonConnexion);
        buttonCreerCompte = findViewById(R.id.btnValider);
        editTextPseudo = findViewById(R.id.textPseudo);
        editTextMDP = findViewById(R.id.textMDP);
        mdpOublier = findViewById(R.id.textMDPOublier);
        buttonConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textPseudo = editTextPseudo.getText().toString();
                textMDP = editTextMDP.getText().toString();
                //Log.e("Text Pseudo",""+textPseudo);
                for(int i = 0;i<id;i++){
                    myRefPseudo = database.getReference().child("Utilisateurs").child("user"+i).child("Pseudo");
                    myRefMdp = database.getReference().child("Utilisateurs").child("user"+i).child("Motdepasse");
                    myRefPseudo.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            pseudo = dataSnapshot.getValue(String.class);
                            Log.e("----",""+pseudo);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    myRefMdp.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            mdp = dataSnapshot.getValue(String.class);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    Log.e("----",""+pseudo);

                    if (textPseudo == pseudo){
                        //Log.e("----","---- Compatibilité = "+compatibilité);
                        compatibilité = true;
                    }

                }
                //Log.e("----",""+compatibilité);


                if (compatibilité == true) {
                    Toast.makeText(getApplicationContext(), "Correct\nPseudo: "+textPseudo+" mdp :"+textMDP, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Incorrect", Toast.LENGTH_SHORT).show();
                }
                compatibilité=false;
            }

        });
        buttonCreerCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnexionActivity.this,CreationCompteActivity.class);
                startActivity(intent);
            }
        });
        mdpOublier.setClickable(true);

        mdpOublier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnexionActivity.this,MdpOublieActivity.class);
                startActivity(intent);

            }
        });

    }

    public void debug(String s) {
        Log.e("-----",s);
    }









}

