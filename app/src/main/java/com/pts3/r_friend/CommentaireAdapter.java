package com.pts3.r_friend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CommentaireAdapter extends ArrayAdapter<Commentaire> {

    Context context;
    FirebaseDatabase database;
    DatabaseReference root;

    public CommentaireAdapter(Context context, List<Commentaire> commentaires) {
        super(context, 0, commentaires);
        this.context=context;
        database = FirebaseDatabase.getInstance();
        root = database.getReference();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Commentaire commentaire = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.commentaire_model,parent, false);
        }

        CommentaireAdapter.CommentaireViewHolder viewHolder = (CommentaireAdapter.CommentaireViewHolder) convertView.getTag();

        if(viewHolder == null) {
            viewHolder = new CommentaireAdapter.CommentaireViewHolder();
            viewHolder.redacteur_commentaire = (TextView) convertView.findViewById(R.id.redacteur_commentaire);
            viewHolder.message_commentaire = (TextView) convertView.findViewById(R.id.message_commentaire);
            viewHolder.date_commentaire = (TextView) convertView.findViewById(R.id.date_commentaire);

            convertView.setTag(viewHolder);
        }

        viewHolder.redacteur_commentaire.setText(commentaire.redacteur);
        viewHolder.message_commentaire.setText(commentaire.message);
        viewHolder.date_commentaire.setText(getDate(commentaire.date));


        return convertView;
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

    private class CommentaireViewHolder {

        public TextView redacteur_commentaire;
        public TextView message_commentaire;
        public TextView date_commentaire;

    }
}
