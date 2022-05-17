module com.example.assemblyfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.eclipse.paho.client.mqttv3;
    requires java.sql;
    requires org.json;


    opens com.example.assemblyfx to javafx.fxml;
    exports com.example.assemblyfx;
    exports com.example.assemblyfx.controllers;
    opens com.example.assemblyfx.controllers to javafx.fxml;
    exports Warehouse;
    opens Warehouse to javafx.fxml;
}