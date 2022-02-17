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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class LueftungsErinnerungController {

    @FXML private Label ueberschriftLabel = new Label();

    @FXML TableView tb = new TableView();
    @FXML TableColumn<Alarm, String > c1= new TableColumn();

    private ArrayList<Alarm> alarmListe = new ArrayList<>();

    public Timer myTimer = new Timer();
    private DateTimeFormatter simpleTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private Media media = new Media(new File("src/Media/Air-raid-siren.mp3").toURI().toString());
    private MediaPlayer mediaPlayer = new MediaPlayer(media);
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

        tb.setEditable(true);

        c1.setCellValueFactory(new PropertyValueFactory<>("alarmZeitpunktString"));

        ueberschriftLabel.setText("Lüftungserinnerungsprogramm");
    }


    private void scheduleAllTasks(ArrayList<Alarm> alarmZeitpunktListe) {
        myTimer.cancel();
        myTimer.purge();
        myTimer = new Timer();

        for (Alarm alarm : alarmZeitpunktListe) {
            LocalDateTime alarmZeitpunkt = alarm.getAlarmZeitpunkt();
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

    @FXML
    protected void onStartButtonClick() {
        auslesenTableView();
        scheduleAllTasks(alarmListe);
    }

    private void auslesenTableView() {
        alarmListe = new ArrayList<>();
        myTimer.cancel();
        myTimer.purge();
        myTimer = new Timer();

         System.out.println(tb.getItems());
         for(Object alarmObject : tb.getItems()) {
             Alarm alarm = (Alarm) alarmObject;
             alarmListe.add(alarm);
         }
    }
    @FXML
    protected void onLadenButtonClick() throws IOException {

        File file = chooseFile();
        FileReader reader = new FileReader(file);
        CSVReader csvReader = new CSVReader(reader);

        List<String[]> sList = csvReader.readAll();

        for(String[] s : sList) {

            String zeitpunktString = s[0];

            if (getLocalDateTime(zeitpunktString) != null) {
                Alarm a = new Alarm(getLocalDateTime(zeitpunktString));
                tb.getItems().add(a);
            }
        }
    }
    @FXML
    protected void onSpeichernButtonClick() throws URISyntaxException, IOException {
        auslesenTableView();

        File file = chooseFile();

        try {
            FileWriter outputfile = new FileWriter(file);
            CSVWriter csvWriter = new CSVWriter(outputfile);
            String[] header = { "Stunde", "Minute" };
            csvWriter.writeNext(header);

            for (Alarm alarm : alarmListe) {

                String[] data = {alarm.getAlarmZeitpunktString()};
                csvWriter.writeNext(data);
            }

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

    public LocalDateTime getLocalDateTime(String zeitpunkt) {
        try {

            String[] stundeUndMinuteArr = zeitpunkt.split(":");
            String stundeString = stundeUndMinuteArr[0];
            String minuteString = stundeUndMinuteArr[1];

            if (minuteString.equals("")) {
                minuteString = "00";
            }

            if (stundeString.length() < 2) {
                stundeString = "0" + stundeString;
            }

            if (minuteString.length() < 2) {
                minuteString = "0" + minuteString;
            }

            int jahr = LocalDateTime.now().getYear();

            long monat = LocalDateTime.now().getMonth().getValue();

            int tagDesMonats = LocalDateTime.now().getDayOfMonth();
            String dateString;

            if (tagDesMonats < 10 && monat < 10) {
                dateString = "0" + tagDesMonats + " 0" + monat + " " + jahr + " ";
            } else if (tagDesMonats < 10) {
                dateString = "0" + tagDesMonats + " " + monat + " " + jahr + " ";
            } else if (monat < 10) {
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
}