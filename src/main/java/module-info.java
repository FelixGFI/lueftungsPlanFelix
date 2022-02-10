module com.example.lueftungsplan {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires opencsv;
    requires com.fasterxml.jackson.databind;

    opens com.example.lueftungsplan to javafx.fxml;
    exports com.example.lueftungsplan;
}