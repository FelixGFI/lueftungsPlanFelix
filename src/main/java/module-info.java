module com.example.lueftungsplan {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.lueftungsplan to javafx.fxml;
    exports com.example.lueftungsplan;
}