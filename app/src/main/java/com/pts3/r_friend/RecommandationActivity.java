package com.pts3.r_friend;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.TextView;

public class RecommandationActivity extends AppCompatActivity {

    public TextView comments ;
    public String lesCommentaires ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommandation);

        comments = findViewById(R.id.comments) ;
        lesCommentaires = "\nFlorian : Oui j'adore aussi ! "
                + "\nRomain : Bof je suis pas fan!"
                + "\nRomain : Bof je suis pas fan!"
                + "\nRomain : Bof je suis pas fan!"
                + "\nRomain : Bof je suis pas fan!"
                + "\nRomain : Bof je suis pas fan!"
                + "\nRomain : Bof je suis pas fan!"
                + "\nRomain : Bof je suis pas fan!"
                + "\nRomain : Bof je suis pas fan!"
                + "\nRomain : Bof je suis pas fan!"
                + "\nRomain : Bof je suis pas fan!"
                + "\nRomain : Bof je suis pas fan!"
                + "\nRomain : Bof je suis pas fan!"
                + "\nRomain : Bof je suis pas fan!"
                + "\nRomain : Bof je suis pas fan!"
                + "\nRomain : Bof je suis pas fan!"
                + "\nRomain : Bof je suis pas fan!"
                + "\nRomain : Bof je suis pas fan!"
                + "\nRomain : Bof je suis pas fan!"
                + "\nRomain : Bof je suis pas fan!"
                + "\nRomain : Bof je suis pas fan!"
                + "\nRomain : Bof je suis pas fan!"
                + "\nRomain : Bof je suis pas fan!"
                + "\nRomain : Bof je suis pas fan!"
                + "\nRomain : Bof je suis pas fan!"
                + "\nRomain : Bof je suis pas fan!"
                + "\nRomain : Bof je suis pas fan!"
                + "\nRomain : Bof je suis pas fan!"
                + "\nRomain : Bof je suis pas fan!"
                + "\nRomain : Bof je suis pas fan!"
                + "\nRomain : Bof je suis pas fan!"
                + "\nRomain : Bof je suis pas fan!"
                + "\nRomain : Bof je suis pas fan!"
                + "\nRomain : Bof je suis pas fan!"
                + "\nRomain : Bof je suis pas fan!"
                + "\nRémi : Merci pour la recommandation :) "  ;
        comments.setText(lesCommentaires);
    }
}

