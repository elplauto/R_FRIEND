package com.pts3.r_friend;

public class Interraction implements Affichable{

    private String phrase;
    private String type;
    private String idRecommandation;

    public Interraction(String phrase, String type, String idRecommandation) {
        this.phrase = phrase;
        this.type = type;
        this.idRecommandation = idRecommandation;
    }

    public String getPhrase() {
        return phrase;
    }

    public String getType() {
        return type;
    }


    public String getIdRecommandation() {
        return idRecommandation;
    }

}
