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


        warehouseController.injectMainController(this);
    }

    public void getMQTT() throws MqttException, InterruptedException {

        

    }

    private String getInventory() throws IOException, InterruptedException {

        /**
         *      getInventory    :   arg[0] = "getInventory"
         *
         *         insertItem      :   arg[0] = "insertItem"
         *                             arg[1] = name (String)
         *                             arg[2] = trayID (int)
         *
         *         pickItem        :   arg[0] = "pickItem"
         *                             arg[1] = trayID (int)
         */


        File file = new File("../AssemblyFX/src/main/resources/WarehouseSpring-0.0.1-SNAPSHOT.jar");
        String filePath = file.getPath();

        String[] args = new String[]{"getInventory"};

        Process process = Runtime.getRuntime().exec("java -jar "+ filePath +" " + args[0]);
        InputStream in = process.getInputStream();
        InputStream err = process.getErrorStream();

        return new String(err.readAllBytes(), StandardCharsets.UTF_8);

    }
}