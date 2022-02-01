package com.example.lueftungsplan;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class HelloController {
    @FXML
    private Label label;

    private ArrayList<LocalDateTime> alarmList = new ArrayList<>();
    private LocalDateTime startZeitpunkt;
    private DateTimeFormatter simpleTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public void initialize() {
        label.setText("Lüftungserinnerungsprogramm");

        startZeitpunkt = LocalDateTime.now();
        LocalDateTime ersterAlarm = LocalDateTime.now().plusSeconds(10);
        LocalDateTime zweiterAlarm = LocalDateTime.now().plusSeconds(20);
        LocalDateTime dritterAlarm = LocalDateTime.now().plusSeconds(30);
        alarmList.add(ersterAlarm);
        alarmList.add(zweiterAlarm);
        alarmList.add(dritterAlarm);
    }

    private void countDownToAlarm(ArrayList<LocalDateTime> alarmZeitpunktListe) throws InterruptedException {

        for (LocalDateTime alarmZeitpunkt : alarmZeitpunktListe) {
            System.out.println("Alarm: " + alarmZeitpunkt.format(simpleTimeFormatter));

            long millisekunden = ChronoUnit.MILLIS.between(LocalDateTime.now(), alarmZeitpunkt);
            Instant instant = alarmZeitpunkt.atZone(ZoneId.of("Europe/Berlin")).toInstant();
            Date alarmInstant = Date.from(instant);
            Timer myTimer = new Timer();
            myTimer.schedule(new TimerTask(){

                @Override
                public void run() {
                    System.out.println("ALARM! ALAARM");
                    Platform.runLater(() -> erzeugeAlert(alarmZeitpunkt));
                }
            }, alarmInstant);
        }
    }

    public void erzeugeAlert(LocalDateTime alarmZeitpunkt) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Alarm");
        a.setHeaderText("ALARM! ALARM!");
        a.setContentText("Es ist Zeit zu lüften\n" + alarmZeitpunkt.format(simpleTimeFormatter));


        DialogPane root = a.getDialogPane();

        Stage dialogStage = new Stage(StageStyle.UTILITY);

        for (ButtonType buttonType : root.getButtonTypes()) {
            ButtonBase button = (ButtonBase) root.lookupButton(buttonType);
            button.setOnAction(evt -> {
                root.setUserData(buttonType);
                dialogStage.close();
            });
        }

// replace old scene root with placeholder to allow using root in other Scene
        root.getScene().setRoot(new Group());

        root.setPadding(new Insets(10, 0, 10, 0));
        Scene scene = new Scene(root);

        dialogStage.setScene(scene);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setAlwaysOnTop(true);
        dialogStage.setResizable(false);
        dialogStage.showAndWait();
        Optional<ButtonType> result = Optional.ofNullable((ButtonType) root.getUserData());
        System.out.println("result: "+result.orElse(null));
    }

    @FXML
    protected void onStartButtonClick() throws InterruptedException {
        System.out.println("Startzeitpunkt: " + startZeitpunkt.format(simpleTimeFormatter));
        countDownToAlarm(alarmList);
    }

    @FXML
    protected void onSchliessenButtonClick() {
        Stage stage = (Stage) this.label.getScene().getWindow();
        stage.close();
    }
}