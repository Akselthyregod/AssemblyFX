package com.example.assemblyfx.controllers;

import MQTT.AssemblyMQTT;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;

public class AssemblyController {
    @FXML
    private TextArea status;
    @FXML
    private Button statusBtn;

    AssemblyMQTT mqtt;

    public AssemblyController() throws MqttException {
    }

    public void initialize() throws MqttException, IOException, InterruptedException {
        mqtt = AssemblyMQTT.getInstance();
        mqtt.connect();
        mqtt.publishMessage(654);
    }


    public void btnClick(ActionEvent actionEvent) {
        status.appendText(mqtt.getAssemblyStationState());
    }
}
