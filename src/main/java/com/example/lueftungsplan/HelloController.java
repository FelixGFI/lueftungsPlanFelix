package com.example.lueftungsplan;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HelloController {

    @FXML private Label ueberschriftLabel = new Label();

    @FXML private TextField tf1Stunde = new TextField();
    @FXML private TextField tf1Minute = new TextField();
    @FXML private TextField tf2Stunde = new TextField();
    @FXML private TextField tf2Minute = new TextField();
    @FXML private TextField tf3Stunde = new TextField();
    @FXML private TextField tf3Minute = new TextField();
    @FXML private TextField tf4Stunde = new TextField();
    @FXML private TextField tf4Minute = new TextField();
    @FXML private TextField tf5Stunde = new TextField();
    @FXML private TextField tf5Minute = new TextField();
    @FXML private TextField tf6Stunde = new TextField();
    @FXML private TextField tf6Minute = new TextField();
    @FXML private TextField tf7Stunde = new TextField();
    @FXML private TextField tf7Minute = new TextField();
    @FXML private TextField tf8Stunde = new TextField();
    @FXML private TextField tf8Minute = new TextField();
    @FXML private TextField tf9Stunde = new TextField();
    @FXML private TextField tf9Minute = new TextField();
    @FXML private TextField tf10Stunde = new TextField();
    @FXML private TextField tf10Minute = new TextField();
    @FXML private TextField tf11Stunde = new TextField();
    @FXML private TextField tf11Minute = new TextField();
    @FXML private TextField tf12Stunde = new TextField();
    @FXML private TextField tf12Minute = new TextField();
    @FXML private TextField tf13Stunde = new TextField();
    @FXML private TextField tf13Minute = new TextField();
    @FXML private TextField tf14Stunde = new TextField();
    @FXML private TextField tf14Minute = new TextField();
    @FXML private TextField tf15Stunde = new TextField();
    @FXML private TextField tf15Minute = new TextField();
    @FXML private TextField tf16Stunde = new TextField();
    @FXML private TextField tf16Minute = new TextField();

    //TODO AutoFill

    public Timer myTimer = new Timer();
    private ArrayList<LocalDateTime> alarmList = new ArrayList<>();
    private DateTimeFormatter simpleTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private Media media = new Media(new File("src/Media/Air-raid-siren.mp3").toURI().toString());
    private MediaPlayer mediaPlayer = new MediaPlayer(media);
    private ArrayList<TextField> tfListe;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy HH:mm:ss", Locale.ROOT);

    public static void  showDialog(HelloController controller) throws IOException {
        Stage stage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Alarm");
        stage.setScene(scene);

        stage.setOnCloseRequest(we -> {
            System.out.println("handle closing of program");

            controller.closeWindow(stage);
        });

       stage.show();
    }

    public void initialize() {
        ueberschriftLabel.setText("Lüftungserinnerungsprogramm");


        tfListe = new ArrayList<>(Arrays.asList(tf1Stunde, tf1Minute, tf2Stunde, tf2Minute, tf3Stunde, tf3Minute, tf4Stunde, tf4Minute, tf5Stunde, tf5Minute,
                tf6Stunde, tf6Minute, tf7Stunde, tf7Minute, tf8Stunde, tf8Minute, tf9Stunde, tf9Minute, tf10Stunde, tf10Minute, tf11Stunde, tf11Minute, tf12Stunde, tf12Minute,
                tf13Stunde, tf13Minute, tf14Stunde, tf14Minute, tf15Stunde, tf15Minute, tf16Stunde, tf16Minute));

        for (TextField tf : tfListe) {
            setOnFocusEvent(tf);
        }

    }

    //sets Chane Listener on a Textfield and Triggers the updateAllTextFields() Method when the respective Textfield is NO LONGER focused after being previously focused
    private void setOnFocusEvent(TextField tf) {
        tf.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (!newPropertyValue)
            {
                updateAllTextFieldValues(tfListe);
            }
        });
    }

    /* WICHTIG! stundeText und minuteText müssen bei jeder änderung am inhalt der Textfelder geändert werden damit die Späteren überprüfungen
    anhand von stundeText bzw. minuteText ordnungsgemäß funktionieren */

    private void updateAllTextFieldValues(ArrayList<TextField> textFelderListe) {
        for (int i = 0; i < textFelderListe.size(); i += 2) {

            TextField stundeFeld = textFelderListe.get(i);
            TextField minuteFeld = textFelderListe.get(i + 1);
            String stundeText = stundeFeld.getText().trim();
            String minuteText = minuteFeld.getText().trim();

            if(!stundeText.equals("") && !minuteFeld.isFocused()) {
                System.out.println("updateAllTextFieldValues() Stunde Exists");

                System.out.println("|" + minuteText + "|");
                if(minuteText.equals("")) {
                    minuteText = "00";
                    minuteFeld.setText(minuteText);
                    System.out.println("minuteText.equals()");
                }

                System.out.println(minuteText.length());

                if(stundeText.length() < 2) {
                    stundeText = "0" + stundeText;
                    stundeFeld.setText(stundeText);
                    System.out.println("stundeText.length() < 2");
                }

                if(minuteText.length() < 2) {
                    minuteText = "0" + minuteText;
                    minuteFeld.setText(minuteText);
                    System.out.println("minuteText.length() < 2");
                }
            } else if((!stundeFeld.isFocused()) && stundeText.equals("")) {
                minuteText = "";
                minuteFeld.setText(minuteText);
            }

        }
    }

    private void fuelleTextFields(ArrayList<TextField> textFelderListe) {

        int uhrzeit = 8;
        for (int i = 0; i < textFelderListe.size(); i += 2) {
            System.out.println("fuelleTextFields()");
            textFelderListe.get(i).setText(uhrzeit + "");
            textFelderListe.get(i + 1).setText("30");
            uhrzeit++;
        }
    }

    @FXML
    protected void onLadenButtonClick() {
        System.out.println("onLadenButtonClick()");
        fuelleTextFields(this.tfListe);
    }

    private void scheduleAllTasks(ArrayList<LocalDateTime> alarmZeitpunktListe) {
        myTimer.cancel();
        myTimer.purge();
        myTimer = new Timer();

        for (LocalDateTime alarmZeitpunkt : alarmZeitpunktListe) {
            if(alarmZeitpunkt.isAfter(LocalDateTime.now())) {
                System.out.println("Alarm: " + alarmZeitpunkt.format(simpleTimeFormatter));

                Instant instant = alarmZeitpunkt.atZone(ZoneId.of("Europe/Berlin")).toInstant();
                Date alarmInstant = Date.from(instant);

                myTimer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        System.out.println("ALARM! ALAARM");
                        Platform.runLater(() -> playAlarmSound());
                        Platform.runLater(() -> erzeugeAlert(alarmZeitpunkt));
                    }
                }, alarmInstant);
            }
        }
    }

    public void playAlarmSound() {
        mediaPlayer.play();
    }

    public void erzeugeAlert(LocalDateTime alarmZeitpunkt) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Alarm");
        a.setHeaderText("ALARM! ALARM!");
        a.setContentText("Es ist Zeit zu lüften\n" + alarmZeitpunkt.format(simpleTimeFormatter));

        // replace old scene root with placeholder to allow using root in other Scene
        DialogPane root = a.getDialogPane();
        root.getScene().setRoot(new Group());
        root.setPadding(new Insets(10, 0, 10, 0));
        Scene scene = new Scene(root);
        Stage dialogStage = new Stage(StageStyle.UTILITY);
        dialogStage.setScene(scene);

        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setAlwaysOnTop(true);
        dialogStage.setResizable(false);


        ButtonBase button = (ButtonBase) root.lookupButton(ButtonType.OK);
        button.setOnAction(evt -> {
            dialogStage.close();
            mediaPlayer.stop();
        });

        dialogStage.showAndWait();
    }

    private void auslesenTextFields(ArrayList<TextField> textFelderListe) {
        alarmList = new ArrayList<>();
        myTimer.cancel();
        myTimer.purge();
        myTimer = new Timer();

        int jahr = LocalDateTime.now().getYear();

        long monat = LocalDateTime.now().getMonth().getValue();
        System.out.println(monat);

        int tagDesMonats = LocalDateTime.now().getDayOfMonth();
            String dateString;

        if(tagDesMonats < 10 && monat < 10) {
            dateString = "0" + tagDesMonats + " 0" + monat + " " + jahr + " ";
        } else if(tagDesMonats < 10) {
            dateString = "0" + tagDesMonats + " " + monat + " " + jahr + " ";
        } else if(monat < 10) {
            dateString = tagDesMonats + " 0" + monat + " " + jahr + " ";
        } else {
            dateString = tagDesMonats + " " + monat + " " + jahr + " ";
        }

        System.out.println(dateString);

        for (int i = 0; i < textFelderListe.size(); i += 2) {

            String stundeString = textFelderListe.get(i).getText().trim();

            if(!stundeString.equals("")) {
                String minuteString = textFelderListe.get(i + 1).getText().trim();

                if(minuteString.equals("")) {
                    minuteString = "00";
                }

                if(stundeString.length() < 2) {
                    stundeString = "0" + stundeString;
                }

                if(minuteString.length() < 2) {
                    minuteString = "0" + minuteString;
                }

                System.out.println(dateString + stundeString + ":" + minuteString + ":00");
                LocalDateTime parsedDate = LocalDateTime.parse(dateString + stundeString + ":" + minuteString + ":00", formatter);

                alarmList.add(parsedDate);
            }
        }
    }

    //ToDo überprüfen/einfügen bei focus Wechsel



    @FXML
    protected void onStartButtonClick() {
        auslesenTextFields(this.tfListe);
        scheduleAllTasks(alarmList);
    }

    @FXML
    protected void onSchliessenButtonClick() {
        Stage stage = (Stage) ueberschriftLabel.getScene().getWindow();
        closeWindow(stage);
    }

    protected void closeWindow(Stage stage) {
        myTimer.cancel();
        myTimer = null;
        stage.close();
        System.exit(1);
    }

    @FXML
    protected void onTextFieldEnterKeyPress() {
        Stage stage = (Stage) ueberschriftLabel.getScene().getWindow();
        TextField tfMomentan = (TextField) stage.getScene().getFocusOwner();
        System.out.println("onTextFieldEnterKeyPress()");
        int indexMomentan = this.tfListe.indexOf(tfMomentan);
        TextField tfNext;

        // 32 ... 0 - 31
        int indexNext = (indexMomentan + 1) % this.tfListe.size();
        tfNext = this.tfListe.get(indexNext);

        tfNext.requestFocus();
    }

}