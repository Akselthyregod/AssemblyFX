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
import java.util.HashMap;
import java.util.Map;

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

    /*
        Keys:
        state
        lastOP
        currentOP
        time
        health
     */
    private Map<String, String> info;

    public AssemblyController() throws MqttException {
    }

    public void initialize() throws MqttException, IOException, InterruptedException {
        mqtt = AssemblyMQTT.getInstance();
        mqtt.connect();

        info = new HashMap<>();
        updateMap();
    }

    public void startMessage() throws MqttException {
        mqtt.publishMessage(1000);
    }
    
    public void btnClick(ActionEvent actionEvent) {

        updateMap();

        lastOP.setText(info.get("lastOP"));
        currentOP.setText(info.get("currentOP"));
        state.setText(info.get("state"));
        time.setText(info.get("time"));
        health.setText(info.get("health"));
    }

    private void updateMap(){
        info.put("lastOP", mqtt.getLastOperation());
        info.put("currentOP", mqtt.getCurrentOperation());
        info.put("state", mqtt.getAssemblyStationState());
        info.put("time", mqtt.getAssemblyTimeStamp());
        info.put("health", mqtt.getHealth());

    }

    public Map<String, String> insertItem() throws MqttException {
        mqtt.publishMessage(6000);
        return mqtt.getContent();
    }

    public Map<String, String> getInfo(){
        return info;
    }
}
