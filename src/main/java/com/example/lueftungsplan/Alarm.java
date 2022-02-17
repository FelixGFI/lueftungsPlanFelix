package com.example.lueftungsplan;

import java.time.LocalDateTime;

public class Alarm {
    LocalDateTime alarmZeitpunkt;
    String alarmZeitpunktString;

    public Alarm() {}

    public Alarm(LocalDateTime alarmZeitpunkt) {
        this.alarmZeitpunkt = alarmZeitpunkt;
        this.alarmZeitpunktString = getZeitpunktStringFromLocalDateTime(alarmZeitpunkt);
    }

    public String getZeitpunktStringFromLocalDateTime (LocalDateTime ldt) {
        String stundeString = ldt.getHour() + "";
        String minuteString = ldt.getMinute() + "";

        if (ldt.getHour() < 10) {
            stundeString = "0" + stundeString;
        }

        if(ldt.getMinute() < 10) {
            minuteString = "0" + minuteString;
        }

        return stundeString + ":" + minuteString;
    }

    public LocalDateTime getAlarmZeitpunkt() {
        return alarmZeitpunkt;
    }

    public void setAlarmZeitpunkt(LocalDateTime alarmZeitpunkt) {
        this.alarmZeitpunkt = alarmZeitpunkt;
    }

    public String getAlarmZeitpunktString() {
        return alarmZeitpunktString;
    }

    public void setAlarmZeitpunktString() {
        this.alarmZeitpunktString = getZeitpunktStringFromLocalDateTime(alarmZeitpunkt);
    }
}
