module com.example.lueftungsplan {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires opencsv;


    opens com.example.lueftungsplan to javafx.fxml;
    exports com.example.lueftungsplan;
}