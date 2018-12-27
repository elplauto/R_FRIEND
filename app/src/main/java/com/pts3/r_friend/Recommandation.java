package com.pts3.r_friend;

import java.util.ArrayList;
import java.util.List;

public abstract class Recommandation {

    private String destinataire;
    private String emetteur;
    private String picture;
    private List<String> likingUsers;
    private List<String> supportingUsers;


    public Recommandation(String destinataire, String emetteur, String picture, List<String> likingUsers, List<String> supportingUsers) {
        this.destinataire = destinataire;
        this.emetteur = emetteur;
        this.picture = picture;
        this.likingUsers=likingUsers;
        this.supportingUsers=supportingUsers;
    }

        /*final ImageButton imageButton = new ImageButton(c);
        if (!isLiked())
            imageButton.setBackgroundResource(R.drawable.coeur_vide);
        else
            imageButton.setBackgroundResource(R.drawable.coeur_rouge);


        final ImageButton imageButton2 = new ImageButton(c);
        if (!isSupported())
            imageButton2.setBackgroundResource(R.drawable.one_white);
        else
            imageButton2.setBackgroundResource(R.drawable.one_green);





        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLiked()) {
                    setLiked(false);
                    imageButton.setBackgroundResource(R.drawable.coeur_vide);
                    setNbLikes(getNbLikes()-1);
                    displayNbLikes();
                }
                else {
                    setLiked(true);
                    imageButton.setBackgroundResource(R.drawable.coeur_rouge);
                    setNbLikes(getNbLikes()+1);
                    displayNbLikes();
                }
            }
        });

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSupported()){ //si la recommandation était appuyée est que l'utilisateur rappuie sur +1 alors elle n'est plus appuyée
                    setSupported(false);
                    imageButton2.setBackgroundResource(R.drawable.one_white);
                    setNbAppuis(getNbAppuis()-1);
                    displayNbAppuis();
                }
                else { //Si elle n'était pas appuyée elle le devient
                    setSupported(true);
                    imageButton2.setBackgroundResource(R.drawable.one_green);
                    setNbAppuis(getNbAppuis()+1);
                    displayNbAppuis();
                }
            }
        });*/

    public String getDestinataire() {
        return destinataire;
    }

    public String getEmetteur() {
        return emetteur;
    }

    public String getPicture() {
        return picture;
    }


    public List<String> getLikingUsers() {
        if (likingUsers == null)
            return new ArrayList<>();
        return likingUsers;
    }

    public List<String> getSupportingUsers() {
        if (supportingUsers == null)
            return new ArrayList<>();
        return supportingUsers;
    }

    public boolean addNewLikingUser(String pseudo) {
        if (likingUsers.contains(pseudo))
            return false;
        return likingUsers.add(pseudo);
    }

    public boolean addNewSupportingUser(String pseudo) {
        if (supportingUsers.contains(pseudo))
            return false;
        return supportingUsers.add(pseudo);
    }



}
