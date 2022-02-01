package com.example.lueftungsplan;

import javafx.application.Platform;
import javafx.fxml.FXML;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HelloController {
    @FXML
    private Label label;

    private ArrayList<LocalDateTime> alarmList = new ArrayList<>();
    private LocalDateTime startZeitpunkt;
    private DateTimeFormatter simpleTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public void initialize() {
        label.setText("Lüftungserinnerungsprogramm");

        startZeitpunkt = LocalDateTime.now();
        LocalDateTime ersterAlarm = LocalDateTime.now().plusSeconds(5);
        LocalDateTime zweiterAlarm = LocalDateTime.now().plusSeconds(10);
        LocalDateTime dritterAlarm = LocalDateTime.now().plusSeconds(15);
        alarmList.add(ersterAlarm);
        alarmList.add(zweiterAlarm);
        alarmList.add(dritterAlarm);
    }

    private void startAllTimers(ArrayList<LocalDateTime> alarmZeitpunktListe) throws InterruptedException {

        for (LocalDateTime alarmZeitpunkt : alarmZeitpunktListe) {
            if(alarmZeitpunkt.isAfter(LocalDateTime.now())) {
                System.out.println("Alarm: " + alarmZeitpunkt.format(simpleTimeFormatter));

                Instant instant = alarmZeitpunkt.atZone(ZoneId.of("Europe/Berlin")).toInstant();
                Date alarmInstant = Date.from(instant);

                Timer myTimer = new Timer();
                myTimer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        System.out.println("ALARM! ALAARM");
                        //Platform.runLater(() -> playAlarmSound());
                        Platform.runLater(() -> erzeugeAlert(alarmZeitpunkt));
                    }
                }, alarmInstant);
            }
        }
    }

    public void playAlarmSound() {

        Media media = new Media("C:\\Users\\van_loechtern_felix\\eclipse-workspace\\lueftungsPlan\\src\\Media\\Air-raid-siren.mp3");
        MediaPlayer mediaPlayer = new MediaPlayer(media);
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
        });

        dialogStage.showAndWait();
    }

    @FXML
    protected void onStartButtonClick() throws InterruptedException {
        System.out.println("Startzeitpunkt: " + startZeitpunkt.format(simpleTimeFormatter));
        startAllTimers(alarmList);
    }

    @FXML
    protected void onSchliessenButtonClick() {
        Stage stage = (Stage) this.label.getScene().getWindow();
        stage.close();
    }
}