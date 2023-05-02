package com.example.ohjelmistotuotanto1;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;




public class TilavarausController {

    // Luodaan asiakaslista

    // Testi dataa että näkee miten toimii
    ObservableList<AsiakasEsim> asiakaslista = FXCollections.
            observableArrayList();


   @FXML // asiakaslista
    private ListView<AsiakasEsim> asiakasListView;
    @FXML
    private Connection m_conn;
   @FXML
   private TextField asiakkaanNimi;

   @FXML
   private TextField asiakkaanEmail;

   @FXML
   private TextField asiakkaanPuh;

   @FXML
   private TextField asiakkaanKaupunki;

    @FXML
    private TextField asiakkaanPostinumero;

    @FXML
    private TextField asiakkaanOsoite;

    @FXML
    private Button lisaaUusi;
    // avataan tietokanta

    public TilavarausController() throws SQLException, Exception {
        yhdista();
    }

    /*
    Avataan tietokantayhteys
    */
    public void yhdista() throws SQLException, Exception {
        m_conn = null;
        String url = "jdbc:mariadb://localhost:3308/vt";
        try {
            m_conn = DriverManager.getConnection(url, "root", "12345");
            System.out.println("Database connection successful");

            // Create a table if it doesn't exist
            String createTableQuery = "CREATE TABLE IF NOT EXISTS asiakkaat ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "nimi VARCHAR(255),"
                    + "email VARCHAR(255),"
                    + "puhelin VARCHAR(255),"
                    + "kaupunki VARCHAR(255),"
                    + "osoite VARCHAR(255),"
                    + "postinumero VARCHAR(255)"
                    + ")";
            Statement statement = m_conn.createStatement();
            statement.executeUpdate(createTableQuery);
            statement.close();
        } catch (SQLException e) {
            m_conn = null;
            System.out.println("Failed to connect to the database");
            throw e;
        }

        Statement statement = m_conn.createStatement();


        ResultSet resultSet = statement.executeQuery("SELECT * FROM asiakkaat");


        while (resultSet.next()) {
            String nimi = resultSet.getString("nimi");
            String email = resultSet.getString("email");
            String puhelin = resultSet.getString("puhelin");
            String kaupunki = resultSet.getString("kaupunki");
            String osoite = resultSet.getString("osoite");
            String postinumero = resultSet.getString("postinumero");

            // Create a new AsiakasEsim object with the retrieved data
            AsiakasEsim asiakas = new AsiakasEsim(nimi, email, puhelin, kaupunki, osoite, postinumero);

            // Add the object to the asiakaslista
            asiakaslista.add(asiakas);
        }

        // Close the result set and statement
        resultSet.close();
        statement.close();
    }






    // Lisää uuden asiakkaan
    @FXML
    private void lisaaUusiPressed() {
        AsiakasEsim uusiAsiakas = new AsiakasEsim(
                asiakkaanNimi.getText(),
                asiakkaanEmail.getText(),
                asiakkaanPuh.getText(),
                asiakkaanKaupunki.getText(),
                asiakkaanOsoite.getText(),
                asiakkaanPostinumero.getText()
        );

        asiakaslista.add(uusiAsiakas);

        // Insert the new customer into the database
        try {
            String query = "INSERT INTO asiakkaat (nimi, email, puhelin, kaupunki, osoite, postinumero) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = m_conn.prepareStatement(query);
            statement.setString(1, uusiAsiakas.getNimi());
            statement.setString(2, uusiAsiakas.getEmail());
            statement.setString(3, uusiAsiakas.getPuhelinNumero());
            statement.setString(4, uusiAsiakas.getKaupunki());
            statement.setString(5, uusiAsiakas.getLahiOsoite());
            statement.setString(6, uusiAsiakas.getPostiNumero());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Uusi asiakas lisätty tietokantaan");
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Virhe lisätessä asiakasta tietokantaan");
        }

        asiakkaanNimi.clear();
        asiakkaanEmail.clear();
        asiakkaanPuh.clear();
        asiakkaanPostinumero.clear();
        asiakkaanKaupunki.clear();
        asiakkaanOsoite.clear();
    }

    // Poista asiakaspainike, joka poistaa valittuna olevan asiakkaan mikäli saa luvan Vahvista poisto varoitukselta
    @FXML
    private void poistaAsiakas() {
        AsiakasEsim poistettava = asiakasListView.getSelectionModel().getSelectedItem();
        if (poistettava != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Vahvista poisto");
            alert.setHeaderText("Vahvista poisto");
            alert.setContentText("Oletko varma että haluat poistaa?!");
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                try {

                    PreparedStatement statement = m_conn.prepareStatement("DELETE FROM asiakkaat WHERE nimi = ?");
                    statement.setString(1, poistettava.getNimi());


                    int affectedRows = statement.executeUpdate();
                    if (affectedRows > 0) {
                        // Remove the item from the asiakaslista
                        asiakaslista.remove(poistettava);
                        System.out.println("Asiakas poistettiin");
                    }


                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    // Tallenna asiakaspainike, joka päivittää valittuna olevan asiakkaan tiedot.
    @FXML
    private void tallennaAsiakas() {
        AsiakasEsim muokattavaAsiakas = asiakasListView.getSelectionModel().getSelectedItem();
        muokattavaAsiakas.setNimi(asiakkaanNimi.getText());
        muokattavaAsiakas.setEmail(asiakkaanEmail.getText());
        muokattavaAsiakas.setPuhelinNumero(asiakkaanPuh.getText());
        muokattavaAsiakas.setKaupunki(asiakkaanKaupunki.getText());
        muokattavaAsiakas.setLahiOsoite(asiakkaanOsoite.getText());
        muokattavaAsiakas.setPostiNumero(asiakkaanPostinumero.getText());

        System.out.println("Asiakkaan tiedot tallennettiin");
    }

    @FXML
    private void initialize(){

        asiakasListView.setItems(asiakaslista);
        asiakasListView.getSelectionModel().selectedItemProperty().addListener((observableValue, asiakas, t1) -> {
            AsiakasEsim valittuAsiakas = asiakasListView.getSelectionModel().getSelectedItem();
            asiakkaanNimi.setText(valittuAsiakas.getNimi());
            asiakkaanEmail.setText(valittuAsiakas.getEmail());
            asiakkaanPuh.setText(valittuAsiakas.getPuhelinNumero());
            asiakkaanKaupunki.setText(valittuAsiakas.getKaupunki());
            asiakkaanPostinumero.setText(valittuAsiakas.getPostiNumero());
            asiakkaanOsoite.setText(valittuAsiakas.getLahiOsoite());
        });

    }
}