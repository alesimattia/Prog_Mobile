package com.example.progetto_mobile;

public class Ride {

    private String tratta, verso, posti, ora, data;

    public Ride(String tratta, String verso, String posti, String ora, String data) {
        this.tratta=tratta;
        this.verso=verso;
        this.posti=posti;
        this.ora=ora;
        this.data=data;
    }

    public String getTratta() {
        return tratta;
    }
    public String getVerso() {
        return verso;
    }
    public String getPosti() {
        return posti;
    }
    public String getOra() {
        return ora;
    }
    public String getData() {
        return data;
    }


}
