package com.example.lueftungsplan;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class LueftungsErinnerungController {

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

    @FXML TableView tb = new TableView();
    @FXML TableColumn<Alarm, String > c1= new TableColumn();

    private ArrayList<Alarm> alarmClassList = new ArrayList<>();

    public Timer myTimer = new Timer();
    private ArrayList<LocalDateTime> alarmList = new ArrayList<>();
    private DateTimeFormatter simpleTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private Media media = new Media(new File("src/Media/Air-raid-siren.mp3").toURI().toString());
    private MediaPlayer mediaPlayer = new MediaPlayer(media);
    private ArrayList<TextField> tfListe;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy HH:mm:ss", Locale.ROOT);

    public static void  showDialog(LueftungsErinnerungController controller) throws IOException {
        Stage stage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(LueftungsErinnerungApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Alarm");
        stage.setScene(scene);

        stage.setOnCloseRequest(we -> {
            controller.closeWindow(stage);
        });

       stage.show();
    }

    public void initialize() {

        c1.setCellValueFactory(new PropertyValueFactory<>("alarmZeitpunkt"));


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
                if(minuteText.equals("")) {
                    minuteText = "00";
                    minuteFeld.setText(minuteText);
                }

                if(stundeText.length() < 2) {
                    stundeText = "0" + stundeText;
                    stundeFeld.setText(stundeText);
                }

                if(minuteText.length() < 2) {
                    minuteText = "0" + minuteText;
                    minuteFeld.setText(minuteText);
                }

            } else if((!stundeFeld.isFocused()) && stundeText.equals("")) {
                minuteText = "";
                minuteFeld.setText(minuteText);
            }
            stundeMinuteIsValid(stundeFeld, minuteFeld, stundeText,minuteText);
        }
    }

    private void fuelleTextFields(ArrayList<String> textForFieldsList ,ArrayList<TextField> textFelderList) {

        for (int i = 0; i < textFelderList.size(); i += 2) {
            try{
                textFelderList.get(i).setText(textForFieldsList.get(i));
                textFelderList.get(i + 1).setText(textForFieldsList.get(i + 1));
            } catch (Exception e) {
                break;
            }
        }
    }

    @FXML
    protected void onLadenButtonClick() throws IOException {

        File file = chooseFile();
        FileReader reader = new FileReader(file);
        CSVReader csvReader = new CSVReader(reader);

        List<String[]> sList = csvReader.readAll();
        ArrayList<String> textForFieldsList = new ArrayList<>();

        for(String[] s : sList) {

            String stundeString = s[0].trim();
            String minuteString = s[1].trim();

            if(minuteString.equals("")) {
                minuteString = "00";
            }

            if(stundeString.length() < 2) {
                stundeString = "0" + stundeString;
            }

            if(minuteString.length() < 2) {
                minuteString = "0" + minuteString;
            }

            System.out.println(stundeString + " " + minuteString);
            if(getLocalDateTime(stundeString, minuteString) != null) {

                Alarm alarm = new Alarm(getLocalDateTime(stundeString, minuteString));
                alarmClassList.add(alarm);

                tb.getItems().add(alarm);

                textForFieldsList.add(stundeString);
                textForFieldsList.add(minuteString);
            }
        }


        fuelleTextFields(textForFieldsList ,this.tfListe);

        updateAllTextFieldValues(tfListe);
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
        if(varifyInput(textFelderListe)) {
            alarmList = new ArrayList<>();
            myTimer.cancel();
            myTimer.purge();
            myTimer = new Timer();

            for (int i = 0; i < textFelderListe.size(); i += 2) {

                String stundeString = textFelderListe.get(i).getText().trim();

                if(!stundeString.equals("")) {
                    String minuteString = textFelderListe.get(i + 1).getText().trim();

                    //TODO Potenziell entferne Code Dopplung hier. (code schon vorhanden in getLocalDateTime()
                    /*if(minuteString.equals("")) {
                        minuteString = "00";
                    }

                    if(stundeString.length() < 2) {
                        stundeString = "0" + stundeString;
                    }

                    if(minuteString.length() < 2) {
                        minuteString = "0" + minuteString;
                    }*/

                    LocalDateTime parsedDate = getLocalDateTime(stundeString, minuteString);

                    alarmList.add(parsedDate);
                }
            }
        }


    }

    private LocalDateTime getLocalDateTime(String stundeString, String minuteString) {
        try{

            if(minuteString.equals("")) {
                minuteString = "00";
            }

            if(stundeString.length() < 2) {
                stundeString = "0" + stundeString;
            }

            if(minuteString.length() < 2) {
                minuteString = "0" + minuteString;
            }

            int jahr = LocalDateTime.now().getYear();

            long monat = LocalDateTime.now().getMonth().getValue();

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
            LocalDateTime parsedDate = LocalDateTime.parse(dateString + stundeString + ":" + minuteString + ":00", formatter);
            return parsedDate;
        } catch (Exception e) {
            return null;
        }

    }

    protected boolean varifyInput(ArrayList<TextField> textFelderListe) {
        boolean isValid = true;


        updateAllTextFieldValues(textFelderListe);

        for (int i = 0; i < textFelderListe.size(); i += 2) {
            TextField stundeFeld = textFelderListe.get(i);
            TextField minuteFeld = textFelderListe.get(i + 1);
            String stundeText = stundeFeld.getText().trim();
            String minuteText = minuteFeld.getText().trim();
            isValid = stundeMinuteIsValid(stundeFeld, minuteFeld, stundeText, minuteText);

        }
        return isValid;
    }

    private boolean stundeMinuteIsValid( TextField stundeFeld, TextField minuteFeld, String stundeText, String minuteText) {
        Paint farbeGelb = Paint.valueOf("yellow");
        Paint farbeWeiss = Paint.valueOf("white");
        boolean isValid = true;

        stundeText = stundeText.trim();
        minuteText = minuteText.trim();

        if(stundeText.equals("")) {
            stundeFeld.setStyle("-fx-control-inner-background: #" + farbeWeiss.toString().substring(2));
            minuteFeld.setStyle("-fx-control-inner-background: #" + farbeWeiss.toString().substring(2));
        } else if (minuteText.equals("") && getLocalDateTime(stundeText, "00") != null) {
            stundeFeld.setStyle("-fx-control-inner-background: #" + farbeWeiss.toString().substring(2));
            minuteFeld.setStyle("-fx-control-inner-background: #" + farbeWeiss.toString().substring(2));
        } else if(getLocalDateTime(stundeText, minuteText) == null) {
            stundeFeld.setStyle("-fx-control-inner-background: #" + farbeGelb.toString().substring(2));
            minuteFeld.setStyle("-fx-control-inner-background: #" + farbeGelb.toString().substring(2));
            isValid = false;

            /*try {
                getLocalDateTime(stundeText, minuteText);
                stundeFeld.setStyle("-fx-control-inner-background: #"+ farbeWeiss.toString().substring(2));
                minuteFeld.setStyle("-fx-control-inner-background: #"+ farbeWeiss.toString().substring(2));

            } catch (Exception e) {
                isValid = false;
                stundeFeld.setStyle("-fx-control-inner-background: #" + farbeGelb.toString().substring(2));
                minuteFeld.setStyle("-fx-control-inner-background: #" + farbeGelb.toString().substring(2));
            }*/
        } else {
            stundeFeld.setStyle("-fx-control-inner-background: #"+ farbeWeiss.toString().substring(2));
            minuteFeld.setStyle("-fx-control-inner-background: #"+ farbeWeiss.toString().substring(2));
        }
        return isValid;
    }

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
        int indexMomentan = this.tfListe.indexOf(tfMomentan);
        TextField tfNext;

        // 32 ... 0 - 31
        int indexNext = (indexMomentan + 1) % this.tfListe.size();
        tfNext = this.tfListe.get(indexNext);

        tfNext.requestFocus();
    }

    @FXML
    protected void onSpeichernButtonClick() throws URISyntaxException, IOException {
        auslesenTextFields(tfListe);

        //File file = new File("C:\\Users\\van_loechtern_felix\\eclipse-workspace\\lueftungsPlan\\src\\main\\resources\\newTest.csv");

        File file = chooseFile();

        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);

            // create CSVWriter object filewriter object as parameter
            CSVWriter csvWriter = new CSVWriter(outputfile);

            // adding header to csv
            String[] header = { "Stunde", "Minute" };
            csvWriter.writeNext(header);

            for (LocalDateTime ldt : alarmList) {
                String stundeString = ldt.getHour() + "";
                String minuteString = ldt.getMinute() + "";

                if(minuteString.equals("")) {
                    minuteString = "00";
                }

                if(stundeString.length() < 2) {
                    stundeString = "0" + stundeString;
                }

                if(minuteString.length() < 2) {
                    minuteString = "0" + minuteString;
                }


                String[] data = {stundeString, minuteString};
                csvWriter.writeNext(data);
            }

            // closing csvWriter connection
            csvWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File chooseFile() {

        FileChooser fileChooserDat = new FileChooser();
        File defaultPath = new File("src/Media");
        fileChooserDat.setInitialDirectory(defaultPath);
        fileChooserDat.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );
        Stage stage = (Stage) ueberschriftLabel.getScene().getWindow();

        File file = fileChooserDat.showOpenDialog(stage);

        fileChooserDat.setInitialDirectory(new File(file.getParent()));

        return file;

    }
}