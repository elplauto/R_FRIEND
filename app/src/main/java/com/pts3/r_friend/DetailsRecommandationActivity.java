package com.pts3.r_friend;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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

    ImageView imageRecommandation;
    ImageButton btnSend;
    ImageView coeur;
    ImageView plusUn;
    TextView nbLikes;
    TextView nbAppuis;

    TextView intro;
    TextView dateRecom;
    TextView champ1;
    TextView champ2;
    TextView champ3;

    String nomOeuvre;


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
        imageRecommandation = (ImageView) findViewById(R.id.image_detailsRecom);
        nbLikes = (TextView) findViewById(R.id.nombre_coeur);
        nbAppuis = (TextView) findViewById(R.id.nombre_plus_un);
        coeur = (ImageView) findViewById(R.id.coeur_detailsRecom);
        plusUn = (ImageView) findViewById(R.id.plus_un_detailsRecom);
        intro = (TextView) findViewById(R.id.emetteur_destinataire_details);
        dateRecom = (TextView) findViewById(R.id.date_details);
        champ1 = (TextView) findViewById(R.id.champ1_detailsRecom);
        champ2 = (TextView) findViewById(R.id.champ2_detailsRecom);
        champ3 = (TextView) findViewById(R.id.champ3_detailsRecom);

        btnSend = (ImageButton) findViewById(R.id.send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nouveauCommentaire.getText().toString().equals("")) {
                    DatabaseReference newComment =  root.child("recommandations").child(recomChild).child(idRecommandation).child("commentaires").push();
                    newComment.child("redacteur").setValue(redacteur);
                    newComment.child("message").setValue(nouveauCommentaire.getText().toString());
                    newComment.child("date").setValue(System.currentTimeMillis());
                    DatabaseReference interractions = root.child("interractions").push();
                    interractions.child("user").setValue(redacteur);
                    interractions.child("typeInterraction").setValue("commentaire");
                    interractions.child("typeRecommandation").setValue(infosSuppChild.substring(0,infosSuppChild.length()-1));
                    interractions.child("idRecommandation").setValue(idRecommandation);
                    interractions.child("date").setValue(System.currentTimeMillis());
                    interractions.child("nom").setValue(nomOeuvre);
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

        dateRecom.setText(getDate((Long) recomData.child("dateRecommandation").getValue(Long.class)));
        nbAppuis.setText(supportingUsers.size()+"");
        nbLikes.setText(likingUsers.size()+"");

        Picasso.with(this).load(oeuvreData.child("pictureURL").getValue().toString()).into(imageRecommandation);

        String emetteur = recomData.child("emetteur").getValue().toString();
        String destinataire = recomData.child("destinataire").getValue().toString();


            switch(typeRecommandation) {
                        case "MorceauRecom":
                            String artisteMorceau = oeuvreData.child("artiste").getValue(String.class);
                            Integer dureeSecondes = oeuvreData.child("duree").getValue(Integer.class);
                            String dureeMinutes = dureeSecondes/60 + "min" + dureeSecondes%60 + "s";
                            String titreMorceau = oeuvreData.child("titre").getValue(String.class);
                            nomOeuvre=titreMorceau;
                            String nomAlbum = oeuvreData.child("album").getValue(String.class);
                            intro.setText(emetteur + " recommande un morceau" + (destinataire.equals("")?"":" à " + destinataire));
                            champ1.setText("Titre : " + titreMorceau + " (" + dureeMinutes + ")");
                            champ2.setText("Album : " + nomAlbum);
                            champ3.setText("Artiste : " + artisteMorceau);
                            break;

                        case "AlbumRecom":
                            Integer nbTracks = oeuvreData.child("nbTrack").getValue(Integer.class);
                            String artisteAlbum = oeuvreData.child("artiste").getValue(String.class);
                            String titreAlbum = oeuvreData.child("titre").getValue(String.class);
                            nomOeuvre=titreAlbum;
                            intro.setText(emetteur + " recommande un album" + (destinataire.equals("")?"":" à " + destinataire));
                            champ1.setText("Titre : " + titreAlbum);
                            champ2.setText("Artiste : " +  artisteAlbum);
                            champ3.setText("Nombre de morceaux : " + nbTracks);
                            break;

                        case "ArtisteRecom":
                            String nom = oeuvreData.child("nom").getValue(String.class);
                            nomOeuvre = nom;
                            Integer nbAlbums = oeuvreData.child("nbAlbums").getValue(Integer.class);
                            intro.setText(emetteur + " recommande un artiste" + (destinataire.equals("")?"":" à " + destinataire));
                            champ1.setText("Nom : " + nom);
                            champ2.setText("Nombre d'album : " +  nbAlbums);
                            champ3.setText("");
                            break;

            }


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
                } else if (o1.getDate() > o2.getDate()) {
                    return -1;
                }
                return 0;
            }
        };
        Collections.sort(commentaires,comparator);
    }

    private String getDate(Long dateRecom) {
        Long dateActuelle = System.currentTimeMillis();
        Long differenceTemps = dateActuelle - dateRecom;
        if (differenceTemps < 60 * 1000) {
            return "Il y a " + differenceTemps /1000 + " s";
        } else if (differenceTemps < 60 * 60 * 1000) {
            return "Il y a " + differenceTemps / (60 * 1000) + " min";
        } else if (differenceTemps < 24 * 60 * 60 * 1000) {
            return "Il y a " + differenceTemps / (60 * 60 * 1000) + " h";
        } else if (differenceTemps < 30.5 *24 * 60 * 60 * 1000) {
            return "Il y a " + differenceTemps / (24 * 60 * 60 * 1000) + " j";
        } else if (differenceTemps <  365 *24 * 60 * 60 * 1000) {
            return "Il y a " + differenceTemps / ( 30.5 * 24 * 60 * 60 * 1000) + " mois";
        }
        return "Il y a " + differenceTemps / ( 365 * 24 * 60 * 60 * 1000) + " ans";
    }
}

