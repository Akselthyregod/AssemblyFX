package com.example.assemblyfx.controllers;

import MQTT.AssemblyMQTT;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;

public class AssemblyController {
    @FXML
    private Button start;
    @FXML
    private Button statusBtn;
    @FXML
    private TextField input;
    @FXML
    private Label lastOP;
    @FXML
    private Label currentOP;
    @FXML
    private Label state;
    @FXML
    private Label time;
    @FXML
    private Label health;
    AssemblyMQTT mqtt;

    public AssemblyController() throws MqttException {
    }

    public void initialize() throws MqttException, IOException, InterruptedException {
        mqtt = AssemblyMQTT.getInstance();
        mqtt.connect();
        mqtt.publishMessage(500);
    }

    public void btnClick(ActionEvent actionEvent) {
        lastOP.setText(mqtt.getLastOperation());
        currentOP.setText(mqtt.getCurrentOperation());
        state.setText(mqtt.getAssemblyStationState());
        time.setText(mqtt.getAssemblyTimeStamp());
        health.setText(mqtt.getHealth());
    }
}
