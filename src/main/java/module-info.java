module com.example.sprintjava {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires java.desktop;


    opens com.example.sprintjava to javafx.fxml;
    exports com.example.sprintjava;
}