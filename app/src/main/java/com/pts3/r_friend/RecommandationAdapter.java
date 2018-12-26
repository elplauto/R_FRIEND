package com.pts3.r_friend;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecommandationAdapter extends ArrayAdapter<Recommandation> {

    MainActivity context;

    //tweets est la liste des models à afficher
    public RecommandationAdapter(MainActivity context, List<Recommandation> tweets) {
        super(context, 0, tweets);
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.recommandation_model,parent, false);
        }

        RecommandationViewHolder viewHolder = (RecommandationViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new RecommandationViewHolder();
            viewHolder.emetteur_destinataire = (TextView) convertView.findViewById(R.id.emetteur_destinataire);
            viewHolder.titre = (TextView) convertView.findViewById(R.id.titre);
            viewHolder.nom_artiste = (TextView) convertView.findViewById(R.id.nom_artiste);
            viewHolder.album = (TextView) convertView.findViewById(R.id.album);
            viewHolder.nombre_coeur = (TextView) convertView.findViewById(R.id.nombre_coeur);
            viewHolder.nombre_plus_un = (TextView) convertView.findViewById(R.id.nombre_plus_un);

            viewHolder.imageRecommandation = (ImageView) convertView.findViewById(R.id.imageRecommandation);

            viewHolder.image_play = (ImageButton) convertView.findViewById(R.id.image_play);
            viewHolder.image_button_coeur = (ImageButton) convertView.findViewById(R.id.image_button_coeur);
            viewHolder.image_button_plus_un = (ImageButton) convertView.findViewById(R.id.image_button_plus_un);

            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Tweet> tweets
        final Recommandation recommandation = getItem(position);


        //il ne reste plus qu'à remplir notre vue

        if (recommandation instanceof MorceauRecom) {
            viewHolder.emetteur_destinataire.setText(recommandation.getEmetteur() + " recommande un morceau" + (recommandation.getDestinataire().equals("")?"":" à " + recommandation.getDestinataire()));
            viewHolder.titre.setText("Titre : " + ((MorceauRecom) recommandation).getTitre());
            viewHolder.nom_artiste.setText("Artiste : "+ ((MorceauRecom) recommandation).getArtiste());
            viewHolder.album.setText("Album : " + ((MorceauRecom) recommandation).getNomAlbum());
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
            viewHolder.image_play = null;
        }

        viewHolder.nombre_plus_un.setText("0");
        viewHolder.nombre_coeur.setText("0");
        Picasso.with(context).load(recommandation.getPicture()).into(viewHolder.imageRecommandation);

        return convertView;
    }

    private class RecommandationViewHolder {

        public TextView emetteur_destinataire;
        public TextView titre;
        public TextView nom_artiste;
        public TextView album;
        public TextView nombre_coeur;
        public TextView nombre_plus_un;

        public ImageView imageRecommandation;

        public ImageButton image_play;
        public ImageButton image_button_coeur;
        public ImageButton image_button_plus_un;
    }
}