package com.example.lueftungsplan;

import java.time.LocalDateTime;

public class Alarm {
    LocalDateTime alarmZeitpunkt;

    public Alarm() {}

    public Alarm(LocalDateTime alarmZeitpunkt) {
        this.alarmZeitpunkt = alarmZeitpunkt;
    }

    public LocalDateTime getAlarmZeitpunkt() {
        return alarmZeitpunkt;
    }

    public void setAlarmZeitpunkt(LocalDateTime alarmZeitpunkt) {
        this.alarmZeitpunkt = alarmZeitpunkt;
    }
}
