package com.example.assemblyfx.controllers;

import MQTT.AssemblyMQTT;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class HelloController {

    public AnchorPane warehouse;
    public AnchorPane agv;
    public AnchorPane assembly;
    @FXML
    private Label welcomeText;
    AssemblyMQTT mqtt;

    @FXML
    private WarehouseController warehouseController;



    @FXML
    protected void onHelloButtonClick() {

        mqtt.printContent();

    }

    public void initialize() throws MqttException, IOException, InterruptedException {


        //warehouseController.injectMainController(this);
    }

    public void getMQTT() throws MqttException, InterruptedException {


    }

}