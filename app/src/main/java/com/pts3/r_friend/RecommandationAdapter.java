package com.pts3.r_friend;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.GregorianCalendar;
import java.util.List;

public class RecommandationAdapter extends ArrayAdapter<Recommandation> {

    MainActivity context;
    FirebaseDatabase database;
    DatabaseReference root;

    public RecommandationAdapter(MainActivity context, List<Recommandation> recommandations) {
        super(context, 0, recommandations);
        this.context=context;
        database = FirebaseDatabase.getInstance();
        root = database.getReference();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //getItem(position) va récupérer l'item [position] de la List<Recommandation> recomandations
        final Recommandation recommandation = getItem(position);

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.recommandation_model,parent, false);

        RecommandationViewHolder viewHolder = (RecommandationViewHolder) convertView.getTag();

            viewHolder = new RecommandationViewHolder();
            viewHolder.emetteur_destinataire = (TextView) convertView.findViewById(R.id.emetteur_destinataire);
            viewHolder.titre = (TextView) convertView.findViewById(R.id.titre);
            viewHolder.nom_artiste = (TextView) convertView.findViewById(R.id.nom_artiste);
            viewHolder.album = (TextView) convertView.findViewById(R.id.album);
            viewHolder.nombre_coeur = (TextView) convertView.findViewById(R.id.nombre_coeur);
            viewHolder.nombre_plus_un = (TextView) convertView.findViewById(R.id.nombre_plus_un);
            viewHolder.nombre_commentaire = (TextView) convertView.findViewById(R.id.nombre_commentaire);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);

            viewHolder.imageRecommandation = (ImageView) convertView.findViewById(R.id.imageRecommandation);

            viewHolder.image_play = (ImageButton) convertView.findViewById(R.id.image_play);
            viewHolder.image_button_coeur = (ImageButton) convertView.findViewById(R.id.image_button_coeur);
            viewHolder.image_button_plus_un = (ImageButton) convertView.findViewById(R.id.image_button_plus_un);
            viewHolder.image_button_commentaire = (ImageButton) convertView.findViewById(R.id.image_button_commentaire);

            convertView.setTag(viewHolder);
            
        //il ne reste plus qu'à remplir notre vue

        if (!context.userMail.getText().toString().equals("") && recommandation.getLikingUsers().contains(context.username.getText().toString())) {
            viewHolder.image_button_coeur.setBackgroundResource(R.drawable.coeur_rouge);
        }
        else {
            viewHolder.image_button_coeur.setBackgroundResource(R.drawable.coeur_vide);
        }
        if (!context.userMail.getText().toString().equals("") && recommandation.getSupportingUsers().contains(context.username.getText().toString())) {
            viewHolder.image_button_plus_un.setBackgroundResource(R.drawable.one_green);
        } else {
            viewHolder.image_button_plus_un.setBackgroundResource(R.drawable.one_white);
        }


        if (recommandation instanceof MorceauRecom) {
            viewHolder.emetteur_destinataire.setText(recommandation.getEmetteur() + " recommande un morceau" + (recommandation.getDestinataire().equals("")?"":" à " + recommandation.getDestinataire()));
            viewHolder.titre.setText("Titre : " + ((MorceauRecom) recommandation).getTitre());
            viewHolder.nom_artiste.setText("Artiste : "+ ((MorceauRecom) recommandation).getArtiste());
            viewHolder.album.setText("Album : " + ((MorceauRecom) recommandation).getNomAlbum());
            if (context.deezerMusicPlayer.idMorceauEnCours == Integer.parseInt(((MorceauRecom)recommandation).getIdMorceau())) {
                viewHolder.image_play.setBackgroundResource(R.drawable.pause);
            }
            final RecommandationViewHolder finalViewHolder = viewHolder;

            viewHolder.image_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String status = context.deezerMusicPlayer.getTrackPlayerState();
                    if (status.equals("STARTED") || status.equals("PAUSED") || status.equals("PLAYBACK_COMPLETED") || status.equals("STOPPED")) {
                        context.deezerMusicPlayer.jouerMorceau(Integer.parseInt(((MorceauRecom)recommandation).getIdMorceau()), finalViewHolder.image_play);
                        finalViewHolder.image_play.setBackgroundResource(R.drawable.pause);
                    } else if (status.equals("PLAYING") && ((MorceauRecom) recommandation).getIdMorceau().equals(context.deezerMusicPlayer.getIdMorceauEnCours()+"")) {
                        context.deezerMusicPlayer.stopMorceau();
                        finalViewHolder.image_play.setBackgroundResource(R.drawable.play);
                    } else if (status.equals("PLAYING") && !((MorceauRecom) recommandation).getIdMorceau().equals(context.deezerMusicPlayer.getIdMorceauEnCours())) {
                        context.deezerMusicPlayer.jouerMorceau(Integer.parseInt(((MorceauRecom)recommandation).getIdMorceau()),finalViewHolder.image_play);
                        finalViewHolder.image_play.setBackgroundResource(R.drawable.pause);
                    }
                }
            });

        } else if (recommandation instanceof  AlbumRecom){
            viewHolder.emetteur_destinataire.setText(recommandation.getEmetteur() + " recommande un album" + (recommandation.getDestinataire().equals("")?"":" à " + recommandation.getDestinataire()));
            viewHolder.titre.setText("Titre : " + ((AlbumRecom) recommandation).getTitre());
            viewHolder.nom_artiste.setText("Artiste : "+ ((AlbumRecom) recommandation).getArtiste());
            viewHolder.album.setText("Nombre de morceaux : " + ((AlbumRecom) recommandation).getNbTracks());
            if (context.deezerMusicPlayer.idAlbumEnCours == Integer.parseInt(((AlbumRecom)recommandation).getIdAlbum())) {
                viewHolder.image_play.setBackgroundResource(R.drawable.pause);
            }
            final RecommandationViewHolder finalViewHolder = viewHolder;
            viewHolder.image_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String status = context.deezerMusicPlayer.getTrackPlayerState();
                    if (status.equals("STARTED") || status.equals("PAUSED") || status.equals("PLAYBACK_COMPLETED") || status.equals("STOPPED")) {
                        context.deezerMusicPlayer.jouerAlbum(Integer.parseInt(((AlbumRecom)recommandation).getIdAlbum()), finalViewHolder.image_play);
                        finalViewHolder.image_play.setBackgroundResource(R.drawable.pause);
                    } else if (status.equals("PLAYING") && ((AlbumRecom) recommandation).getIdAlbum().equals(context.deezerMusicPlayer.getIdMorceauEnCours()+"")) {
                        context.deezerMusicPlayer.stopMorceau();
                        finalViewHolder.image_play.setBackgroundResource(R.drawable.play);
                    } else if (status.equals("PLAYING") && !((AlbumRecom) recommandation).getIdAlbum().equals(context.deezerMusicPlayer.getIdMorceauEnCours())) {
                        context.deezerMusicPlayer.jouerMorceau(Integer.parseInt(((AlbumRecom)recommandation).getIdAlbum()),finalViewHolder.image_play);
                        finalViewHolder.image_play.setBackgroundResource(R.drawable.pause);
                    }
                }
            });

        } else if (recommandation instanceof  ArtisteRecom){
            viewHolder.emetteur_destinataire.setText(recommandation.getEmetteur() + " recommande un artiste" + (recommandation.getDestinataire().equals("")?"":" à " + recommandation.getDestinataire()));
            viewHolder.titre.setText("Nom : " + ((ArtisteRecom) recommandation).getNom());
            viewHolder.nom_artiste.setText("Nombre d'album : "+ ((ArtisteRecom) recommandation).getNbAlbums());
            viewHolder.album.setText("");
            viewHolder.image_play.setVisibility(View.INVISIBLE);
        }

        final RecommandationViewHolder finalViewHolder = viewHolder;

        viewHolder.image_button_coeur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String pseudo = context.username.getText().toString();
                if (pseudo.equals("Non connecté")) {
                    Toast.makeText(context, "Vous devez être connecté pour interragir avec les recommandations", Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(context,ConnexionActivity.class);
                    context.startActivity(intent);
                } else if(recommandation.getLikingUsers().contains(pseudo)) {
                    Toast.makeText(context, "Vous avez déja aimé cette recommandation", Toast.LENGTH_SHORT).show();
                } else if (recommandation.getSupportingUsers().contains(pseudo)) {
                    Toast.makeText(context, "Vous ne pouvez pas aimer une recommandation que vous avez appuyé", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Confirmation");
                    alertDialogBuilder.setMessage("Aimer signifie que vous avez découvert l'objet de la recommandation et que vous l'appréciez.\n\nÊtes-vous certain de vouloir aimer cette recommandation ?");
                    alertDialogBuilder.setPositiveButton("Aimer", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            recommandation.addNewLikingUser(context.username.getText().toString());
                            finalViewHolder.image_button_coeur.setBackgroundResource(R.drawable.coeur_rouge);
                            finalViewHolder.nombre_coeur.setText(recommandation.getLikingUsers().size()+"");
                            addNewLikingUserToDatabase(recommandation,pseudo);
                        }
                    });

                    alertDialogBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alertDialogBuilder.create().show();
                }
            }
        });

        viewHolder.image_button_plus_un.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String pseudo = context.username.getText().toString();
                if (pseudo.equals("Non connecté")) {
                    Toast.makeText(context, "Vous devez être connecté pour interragir avec les recommandations", Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(context,ConnexionActivity.class);
                    context.startActivity(intent);
                } else if(recommandation.getSupportingUsers().contains(pseudo)) {
                    Toast.makeText(context, "Vous avez déja appuyé cette recommandation", Toast.LENGTH_SHORT).show();
                } else if (recommandation.getLikingUsers().contains(pseudo)) {
                    Toast.makeText(context, "Vous ne pouvez pas appuyer une recommandation que vous avez aimé", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Confirmation");
                    alertDialogBuilder.setMessage("Appuyer signifie que vous connaissez déja l'objet de la recommandation et que vous l'approuvez.\n\nÊtes-vous certain de vouloir appuyer cette recommandation ?");
                    alertDialogBuilder.setPositiveButton("Appuyer", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            recommandation.addNewSupportingUser(context.username.getText().toString());
                            finalViewHolder.image_button_plus_un.setBackgroundResource(R.drawable.one_green);
                            finalViewHolder.nombre_plus_un.setText(recommandation.getSupportingUsers().size()+"");
                            addNewSupportingUserToDatabase(recommandation,pseudo);
                        }
                    });

                    alertDialogBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alertDialogBuilder.create().show();
                }
            }
        });

        viewHolder.image_button_coeur.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Liste des utilisateurs qui aiment cette recommandation");
                String message ="";
                for (String each : recommandation.getLikingUsers()) {
                   message += each + "\n";
                }
                alertDialogBuilder.setMessage(message);
                alertDialogBuilder.setNegativeButton("Fermer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialogBuilder.create().show();

                return true;
            }
        });

        viewHolder.image_button_plus_un.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Liste des utilisateurs qui appuient cette recommandation");
                String message ="";
                for (String each : recommandation.getSupportingUsers()) {
                    message += each + "\n";
                }
                alertDialogBuilder.setMessage(message);
                alertDialogBuilder.setNegativeButton("Fermer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialogBuilder.create().show();

                return true;
            }
        });

        viewHolder.nombre_plus_un.setText("("+recommandation.getSupportingUsers().size()+")");
        viewHolder.nombre_coeur.setText("("+recommandation.getLikingUsers().size()+")");
        viewHolder.nombre_commentaire.setText("("+recommandation.getCommentaires().size()+")");
        viewHolder.date.setText(getDate(recommandation.getDateRecommandation()));

        Picasso.with(context).load(recommandation.getPicture()).into(viewHolder.imageRecommandation);

        viewHolder.image_button_commentaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String pseudo = context.username.getText().toString();
                if (pseudo.equals("Non connecté")) {
                    Toast.makeText(context, "Vous devez être connecté pour voir ou poster un commentaire", Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(context,ConnexionActivity.class);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, DetailsRecommandationActivity.class);
                    intent.putExtra("idRecommandation", recommandation.getIdRecommandation());
                    intent.putExtra("type", recommandation.getClass().getSimpleName());
                    intent.putExtra("redacteur", context.username.getText().toString());
                    context.startActivity(intent);
                }
            }
        });

        return convertView;
    }

    public void addNewSupportingUserToDatabase(Recommandation recommandation, String pseudo) {
        String type = "";
        if (recommandation instanceof MorceauRecom) {
            type = "recommandationsMorceau";
        } else if (recommandation instanceof AlbumRecom) {
            type = "recommandationsAlbum";
        } else if (recommandation instanceof ArtisteRecom) {
            type = "recommandationsArtiste";
        }
        root.child("recommandations").child(type).child(recommandation.getIdRecommandation()).child("supportingUsers").child(pseudo).setValue(pseudo);
    }

    public void addNewLikingUserToDatabase(Recommandation recommandation, String pseudo) {
        String type = "";
        if (recommandation instanceof MorceauRecom) {
            type = "recommandationsMorceau";
        } else if (recommandation instanceof AlbumRecom) {
            type = "recommandationsAlbum";
        } else if (recommandation instanceof ArtisteRecom) {
            type = "recommandationsArtiste";
        }
        root.child("recommandations").child(type).child(recommandation.getIdRecommandation()).child("likingUsers").child(pseudo).setValue(pseudo);
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

    private class RecommandationViewHolder {

        public TextView emetteur_destinataire;
        public TextView titre;
        public TextView nom_artiste;
        public TextView album;
        public TextView nombre_coeur;
        public TextView nombre_plus_un;
        public TextView nombre_commentaire;
        public TextView date;

        public ImageView imageRecommandation;

        public ImageButton image_play;
        public ImageButton image_button_coeur;
        public ImageButton image_button_plus_un;
        public ImageButton image_button_commentaire;
    }
}