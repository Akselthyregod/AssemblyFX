package com.example.assemblyfx.controllers;

import MQTT.AssemblyMQTT;
import Warehouse.Inventory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;
import java.util.Map;

public class MainController {

    public AnchorPane warehouse;
    public AnchorPane agv;
    public AnchorPane assembly;
    public TextArea textAreaMap;
    public TextArea textAssembly;
    public TextArea textAGV;
    public TextArea textWarehouse;
    @FXML
    private Label welcomeText;
    AssemblyMQTT mqtt;

    @FXML
    private WarehouseController warehouseController;

    @FXML
    private AssemblyController assemblyController;

    private Inventory warehouseInventory;
    private Map<String, String> assemblyInfo;




    public void initialize() throws MqttException, IOException, InterruptedException {

      updateText(new ActionEvent());
    }


    public void getWarehouseInventory(ActionEvent actionEvent){
        warehouseInventory = warehouseController.getInventory();

    }
    public void getAssemblyMap(ActionEvent actionEvent) {
        assemblyInfo = assemblyController.getInfo();

    }

    public void updateText(ActionEvent actionEvent) {
        getWarehouseInventory(actionEvent);
        getAssemblyMap(actionEvent);

        textAssembly.setText(assemblyInfo.get("state"));
        textAGV.setText("PLACEHOLDER");
        textWarehouse.setText("State: " + warehouseInventory.getState());
        setWarehouseText();
    }

    public void startProduction(ActionEvent actionEvent) {

        /*

            Tell AGV to go to warehouse
            when at warehouse pick item
            move to assembly
            wait for assembly
            move to warehouse
            put in warehouse

            **Check for battery**
        */
    }

    private void setWarehouseText(){
        textWarehouse.appendText("\n1: " + warehouseInventory.getTray1());
        textWarehouse.appendText("\n2: " + warehouseInventory.getTray2());
        textWarehouse.appendText("\n3: " + warehouseInventory.getTray3());
        textWarehouse.appendText("\n4: " + warehouseInventory.getTray4());
        textWarehouse.appendText("\n5: " + warehouseInventory.getTray5());
        textWarehouse.appendText("\n6: " + warehouseInventory.getTray6());
        textWarehouse.appendText("\n7: " + warehouseInventory.getTray7());
        textWarehouse.appendText("\n8: " + warehouseInventory.getTray8());
        textWarehouse.appendText("\n9: " + warehouseInventory.getTray9());
        textWarehouse.appendText("\n10: " + warehouseInventory.getTray10());

    }
}