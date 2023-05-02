package com.example.ohjelmistotuotanto1;

import java.sql.*;
import java.lang.*;
public class Lasku {

    private int m_lasku_id;
    private int m_varaus_id;
    private int m_asiakas_id;
    private String m_etunimi;
    private String m_sukunimi;
    private String m_lahiosoite;
    private String m_postitoimipaikka;
    private String m_postinro;
    private double m_summa;

    public Lasku(){

    }

    public int getLaskuId() {
        return m_lasku_id;
    }

    public void setLaskuId(int m_lasku_id) {
        this.m_lasku_id = m_lasku_id;
    }

    public int getVarausId() {
        return m_varaus_id;
    }

    public void setVarausId(int m_varaus_id) {
        this.m_varaus_id = m_varaus_id;
    }

    public int getAsiakasId() {
        return m_asiakas_id;
    }

    public void setAsiakasId(int m_asiakas_id) {
        this.m_asiakas_id = m_asiakas_id;
    }

    public String getEtunimi() {
        return m_etunimi;
    }

    public void setEtunimi(String m_etunimi) {
        this.m_etunimi = m_etunimi;
    }

    public String getSukunimi() {
        return m_sukunimi;
    }

    public void setSukunimi(String m_sukunimi) {
        this.m_sukunimi = m_sukunimi;
    }

    public String getLahiosoite() {
        return m_lahiosoite;
    }

    public void setLahiosoite(String m_lahiosoite) {
        this.m_lahiosoite = m_lahiosoite;
    }

    public String getPostitoimipaikka() {
        return m_postitoimipaikka;
    }

    public void setPostitoimipaikka(String m_postitoimipaikka) {
        this.m_postitoimipaikka = m_postitoimipaikka;
    }

    public String getPostinro() {
        return m_postinro;
    }

    public void setPostinro(String m_postinro) {
        this.m_postinro = m_postinro;
    }

    public double getSumma() {
        return m_summa;
    }

    public void setSumma(double m_summa) {
        this.m_summa = m_summa;
    }

    public static Lasku haeLasku (Connection yhteys, int id) throws SQLException, Exception {
        ResultSet tulosjoukko = null;
        try {
            PreparedStatement kasky = yhteys.prepareStatement("SELECT lasku_id, varaus_id, asiakas_id, etunimi, sukunimi, lahiosoite, postitoimipaikka, postinro, summa "
                    + " FROM Lasku WHERE lasku_id = ?");
            kasky.setInt( 1, id);
            tulosjoukko = kasky.executeQuery();
            if (tulosjoukko == null) {
                throw new Exception("Laskua ei loydy");
            }
        } catch (SQLException se) {
            throw se;
        } catch (Exception e) {
            throw e;
        }

        Lasku lasku = new Lasku ();

        try {
            while (tulosjoukko.next()){
                lasku.setLaskuId(tulosjoukko.getInt("lasku_id"));
                lasku.setVarausId(tulosjoukko.getInt("varaus_id"));
                lasku.setAsiakasId(tulosjoukko.getInt("asiakas_id"));
                lasku.setEtunimi(tulosjoukko.getString("etunimi"));
                lasku.setSukunimi(tulosjoukko.getString("sukunimi"));
                lasku.setLahiosoite(tulosjoukko.getString("lahiosoite"));
                lasku.setPostitoimipaikka(tulosjoukko.getString("postitoimipaikka"));
                lasku.setPostinro(tulosjoukko.getString("postinro"));
                lasku.setSumma(tulosjoukko.getDouble("summa"));
            }

        }catch (SQLException se) {
            throw se;
        }

        return lasku;
    }



    public int lisaaLasku (Connection yhteys) throws SQLException, Exception {

        PreparedStatement kasky;
        ResultSet tulosjoukko = null;
        try {
            kasky = yhteys.prepareStatement("SELECT lasku_id"
                    + " FROM Lasku WHERE lasku_id = ?");
            kasky.setInt( 1, getLaskuId());
            tulosjoukko = kasky.executeQuery();
            if (tulosjoukko.next()) {
                throw new Exception("Lasku on jo olemassa");
            }
        } catch (SQLException se) {
            throw se;
        } catch (Exception e) {
            throw e;
        }

        try {
            kasky = yhteys.prepareStatement("INSERT INTO Lasku "
                    + "(lasku_id, varaus_id, asiakas_id, etunimi, sukunimi, lahiosoite, postitoimipaikka, postinro, summa) "
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            kasky.setInt(1, getLaskuId());
            kasky.setInt(2, getVarausId());
            kasky.setInt( 3, getAsiakasId());
            kasky.setString(4, getEtunimi());
            kasky.setString(5, getSukunimi());
            kasky.setString(6, getLahiosoite());
            kasky.setString(7, getPostitoimipaikka());
            kasky.setString(8, getPostinro());
            kasky.setDouble(9, getSumma());
            int lkm = kasky.executeUpdate();
            if (lkm == 0) {
                throw new Exception("Laskun lisaaminen ei onnistu");
            }
        } catch (SQLException se) {
            throw se;
        } catch (Exception e) {
            throw e;
        }
        return 0;
    }



    public int muutaLasku (Connection yhteys) throws SQLException, Exception {
        PreparedStatement kasky;
        ResultSet tulosjoukko = null;
        try {
            kasky = yhteys.prepareStatement("SELECT lasku_id"
                    + " FROM Lasku WHERE lasku_id = ?");
            kasky.setInt( 1, getLaskuId());
            tulosjoukko = kasky.executeQuery();
            if (!tulosjoukko.next()) {
                throw new Exception("Laskua ei loydy tietokannasta");
            }
        } catch (SQLException se) {
            throw se;
        } catch (Exception e) {
            throw e;
        }

        try {
            kasky = yhteys.prepareStatement("UPDATE Lasku "
                    + "SET varaus_id = ?, asiakas_id = ? etunimi = ?, sukunimi = ?, lahiosoite = ?, postitoimipaikka = ?, postinro = ?, summa = ? "
                    + " WHERE lasku_id = ?");

            kasky.setInt(1, getVarausId());
            kasky.setInt( 2, getAsiakasId());
            kasky.setString(3, getEtunimi());
            kasky.setString(4, getSukunimi());
            kasky.setString(5, getLahiosoite());
            kasky.setString(6, getPostitoimipaikka ());
            kasky.setString(7, getPostinro());
            kasky.setDouble(8, getSumma());
            kasky.setInt(9, getLaskuId());
            int lkm = kasky.executeUpdate();
            if (lkm == 0) {
                throw new Exception("Laskun muuttaminen ei onnistunut");
            }
        } catch (SQLException se) {
            throw se;
        } catch (Exception e) {
            throw e;
        }
        return 0;
    }



    public int poistaLasku (Connection connection) throws SQLException, Exception {

        try {
            PreparedStatement kasky = connection.prepareStatement("DELETE FROM Lasku WHERE lasku_id = ?");
            kasky.setInt( 1, getAsiakasId());
            int lkm = kasky.executeUpdate();
            if (lkm == 0) {
                throw new Exception("Laskun poistaminen ei onnistunut");
            }
        } catch (SQLException se) {
            throw se;
        } catch (Exception e) {
            throw e;
        }
        return 0;
    }
}