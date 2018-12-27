package com.pts3.r_friend;

public class Commentaire {

    String redacteur;
    String message;
    Long date;

    public Commentaire(String redacteur, String message, Long date) {
        this.redacteur=redacteur;
        this.message=message;
        this.date=date;
    }

    public String getRedacteur() {
        return redacteur;
    }

    public String getMessage() {
        return message;
    }

    public Long getDate() {
        return date;
    }
}
