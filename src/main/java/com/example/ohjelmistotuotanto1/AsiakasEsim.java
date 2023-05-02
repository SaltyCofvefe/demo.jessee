package com.example.ohjelmistotuotanto1;

public class AsiakasEsim {

    // Asiakas luokka esimerkki

    private String nimi;
    private String email;
    private String puhelinNumero;
    private String kaupunki;
    private String lahiOsoite;
    private String postiNumero;

    public AsiakasEsim(String nimi, String email, String puh, String kaupunki, String lahiOsoite, String postiNumero) {
        this.nimi = nimi;
        this.email = email;
        this.puhelinNumero = puh;
        this.kaupunki = kaupunki;
        this.lahiOsoite = lahiOsoite;
        this.postiNumero = postiNumero;
    }

    public String getKaupunki() {
        return kaupunki;
    }

    public void setKaupunki(String kaupunki) {
        this.kaupunki = kaupunki;
    }

    public String getLahiOsoite() {
        return lahiOsoite;
    }

    public void setLahiOsoite(String lahiOsoite) {
        this.lahiOsoite = lahiOsoite;
    }

    public String getPostiNumero() {
        return postiNumero;
    }

    public void setPostiNumero(String postiNumero) {
        this.postiNumero = postiNumero;
    }


    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPuhelinNumero() {
        return puhelinNumero;
    }

    public void setPuhelinNumero(String puhelinNumero) {
        this.puhelinNumero = puhelinNumero;
    }

    public String toString() {
        return nimi;
    }
}
