package com.pts3.r_friend;

public class Interraction implements Affichable{

    private String phrase;
    private String type;
    private String idRecommandation;
    private Long date;

    public Interraction(String phrase, String type, String idRecommandation, Long date) {
        this.phrase = phrase;
        this.type = type;
        this.idRecommandation = idRecommandation;
        this.date=date;
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

    public Long getDate() {
        return date;
    }
}
