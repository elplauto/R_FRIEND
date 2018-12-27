package com.pts3.r_friend;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

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

public class DetailsRecommandationActivity extends AppCompatActivity {

    RelativeLayout fenetrePrincipale;
    ListView listView;

    FirebaseDatabase database;
    DatabaseReference root;

    String typeRecommandation;
    String idRecommandation;
    String redacteur;

    DataSnapshot recomData;
    DataSnapshot oeuvreData;

    String recomChild;
    String infosSuppChild;
    String infosSuppID;

    List<Commentaire> commentaires;

    ImageButton btnSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_recommandation);

        fenetrePrincipale = (RelativeLayout) findViewById(R.id.fenetreDetailsRecom);
        listView = (ListView) findViewById(R.id.commentaires);

        database = FirebaseDatabase.getInstance();
        root = database.getReference();

        Bundle bundle = getIntent().getExtras();
        typeRecommandation = bundle.getString("type");
        idRecommandation = bundle.getString("idRecommandation");
        redacteur = bundle.getString("redacteur");

        commentaires = new ArrayList<>();

        final TextInputEditText nouveauCommentaire = (TextInputEditText) findViewById(R.id.nouveauCommentaire);

        btnSend = (ImageButton) findViewById(R.id.send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nouveauCommentaire.getText().toString().equals("")) {

                } else {
                    DatabaseReference newComment =  root.child("recommandations").child(recomChild).child(idRecommandation).child("commentaires").push();
                    newComment.child("redacteur").setValue(redacteur);
                    newComment.child("message").setValue(nouveauCommentaire.getText().toString());
                    newComment.child("date").setValue(System.currentTimeMillis());
                    nouveauCommentaire.setText("");
                    btnSend.setVisibility(View.INVISIBLE);
                    chargerRecommandation();
                }
            }
        });

        chargerRecommandation();
    }

    public void chargerRecommandation() {
        commentaires.clear();
        switch(typeRecommandation) {
            case "MorceauRecom":
                recomChild = "recommandationsMorceau";
                infosSuppChild = "morceaux";
                infosSuppID = "idMorceau";
                break;
            case "AlbumRecom":
                recomChild = "recommandationsAlbum";
                infosSuppChild = "albums";
                infosSuppID = "idAlbum";
                break;
            case "ArtisteRecom":
                recomChild = "recommandationsArtiste";
                infosSuppChild = "artistes";
                infosSuppID = "idArtiste";
                break;
        }

        root.child("recommandations").child(recomChild).child(idRecommandation).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                recomData = ds;
                chargerOeuvre(recomData.child(infosSuppID).getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

    }

    public void chargerOeuvre(String id) {
        root.child("recommandables").child(infosSuppChild).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                oeuvreData = ds;
                afficherRecommandation();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

    }

    public void afficherRecommandation () {
        List<String> likingUsers = new ArrayList<>();
        List<String> supportingUsers = new ArrayList<>();

        Iterator<DataSnapshot> it = recomData.child("likingUsers").getChildren().iterator();
        while (it.hasNext()) {
            likingUsers.add(it.next().getValue().toString());
        }
        it = recomData.child("supportingUsers").getChildren().iterator();
        while (it.hasNext()) {
            supportingUsers.add(it.next().getValue().toString());
        }

        it = recomData.child("commentaires").getChildren().iterator();
        while (it.hasNext()) {
            DataSnapshot temp = it.next();
            commentaires.add(new Commentaire(temp.child("redacteur").getValue().toString(),
                    temp.child("message").getValue().toString(),
                    temp.child("date").getValue(Long.class)));
        }

            /*switch(typeRecommandation) {
                        case "MorceauRecom":
                            String artisteMorceau = dataInfosSupp.child("artiste").getValue(String.class);
                            Integer dureeSecondes = dataInfosSupp.child("duree").getValue(Integer.class);
                            String dureeMinutes = dureeSecondes/60 + "min" + dureeSecondes%60 + "s";
                            String titreMorceau = dataInfosSupp.child("titre").getValue(String.class);
                            String imgAlb = dataInfosSupp.child("pictureURL").getValue(String.class);
                            String nomAlbum = dataInfosSupp.child("album").getValue(String.class);
                            String idMorceau = dataInfosSupp.getKey().toString();
                            break;

                        case "AlbumRecom":
                            Integer nbTracks = dataInfosSupp.child("nbTrack").getValue(Integer.class);
                            String artisteAlbum = dataInfosSupp.child("artiste").getValue(String.class);
                            String titreAlbum = dataInfosSupp.child("titre").getValue(String.class);
                            String imgAlbum = dataInfosSupp.child("pictureURL").getValue(String.class);
                            String idAlbum = dataInfosSupp.getKey().toString();
                            recommandations.add(new AlbumRecom(idRecommandation, date, dest, emet, imgAlbum, likingUsers, supportingUsers, artisteAlbum, nbTracks, titreAlbum, idAlbum));
                            break;

                        case "ArtisteRecom":
                            String nom = dataInfosSupp.child("nom").getValue(String.class);
                            Integer nbAlbums = dataInfosSupp.child("nbAlbums").getValue(Integer.class);
                            String picture = dataInfosSupp.child("pictureURL").getValue(String.class);
                            recommandations.add(new ArtisteRecom(idRecommandation, date, dest, emet, picture, likingUsers, supportingUsers, nom, nbAlbums));
                            break;
                    }
                }*/
        ordonnerCommentaire();
        CommentaireAdapter adapter = new CommentaireAdapter(this, commentaires);
        listView.setAdapter(adapter);
        btnSend.setVisibility(View.VISIBLE);
    }

    public void ordonnerCommentaire() {
        Comparator<Commentaire> comparator = new Comparator<Commentaire>() {
            @Override
            public int compare(Commentaire o1, Commentaire o2) {
                if (o1.getDate() < o2.getDate()) {
                    return 1;
                }
                return -1;
            }
        };
        Collections.sort(commentaires,comparator);
    }
}

