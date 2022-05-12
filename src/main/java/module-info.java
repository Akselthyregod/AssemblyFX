module com.example.assemblyfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.eclipse.paho.client.mqttv3;
    requires java.sql;


    opens com.example.assemblyfx to javafx.fxml;
    exports com.example.assemblyfx;
}