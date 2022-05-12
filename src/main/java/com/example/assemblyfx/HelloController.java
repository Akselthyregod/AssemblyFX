package com.example.assemblyfx;

import MQTT.AssemblyMQTT;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.converter.LocalDateTimeStringConverter;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;

public class HelloController {
    @FXML
    private Label welcomeText;

    private Warehouse warehouse;

    @FXML
    protected void onHelloButtonClick() throws IOException, InterruptedException {
        welcomeText.setText(warehouse.getTime());


    }

    public void initialize() throws MqttException, IOException, InterruptedException {
        //getMQTT();

        welcomeText.setText("Welcome");
        warehouse = new Warehouse();
    }

    public void getMQTT() throws MqttException {
        AssemblyMQTT mqtt = AssemblyMQTT.getInstance();

        mqtt.connect();

        mqtt.publishMessage(100);


    }





}