package com.pts3.r_friend;

import java.util.ArrayList;
import java.util.List;

public abstract class Recommandation implements Affichable{

    private String idRecommandation;
    private Long dateRecommandation;
    private String destinataire;
    private String emetteur;
    private String picture;
    private List<String> likingUsers;
    private List<String> supportingUsers;
    private List<Commentaire> commentaires;



    public Recommandation(String idRecommandation, Long dateRecommandation, String destinataire, String emetteur, String picture, List<String> likingUsers, List<String> supportingUsers, List<Commentaire> commentaires) {
        this.idRecommandation=idRecommandation;
        this.dateRecommandation = dateRecommandation;
        this.destinataire = destinataire;
        this.emetteur = emetteur;
        this.picture = picture;
        this.likingUsers=likingUsers;
        this.supportingUsers=supportingUsers;
        this.commentaires=commentaires;
    }

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

    public String getIdRecommandation() {
        return idRecommandation;
    }

    public Long getDateRecommandation() {
        return dateRecommandation;
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

    public List<Commentaire> getCommentaires() {
        return commentaires;
    }

    public abstract boolean contains(String text);

    public abstract String getNomObjet();
}
