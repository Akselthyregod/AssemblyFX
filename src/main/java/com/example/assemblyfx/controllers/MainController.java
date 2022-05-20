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

    @FXML
    private AGVController agvController;

    private Inventory warehouseInventory;
    private Map<String, String> assemblyInfo;

    private Map<String, String> agvInfo;


    public void initialize() throws MqttException, IOException, InterruptedException {

        updateInfo();
        updateText(new ActionEvent());

    }


    public void getWarehouseInventory(){
        warehouseInventory = warehouseController.getInventory();

    }
    public void getAssemblyMap() {
        assemblyInfo = assemblyController.getInfo();

    }

    public void getAGVInfo() throws IOException, InterruptedException {
        agvInfo = agvController.getStatus();

    }

    private void updateInfo() throws IOException, InterruptedException {
        getWarehouseInventory();
        getAssemblyMap();
        getAGVInfo();
    }

    public void updateText(ActionEvent actionEvent) throws IOException, InterruptedException {
        textAssembly.setText(assemblyInfo.get("state"));
        textAGV.setText(agvInfo.get("state") + "\n" + agvInfo.get("program name"));
        textWarehouse.setText("State: " + warehouseInventory.getState());
        setWarehouseText();
    }

    public void startProduction(ActionEvent actionEvent) throws IOException, InterruptedException, MqttException {

        /*
            Tell AGV to go to warehouse
            when at warehouse pick item
            move to assembly
            wait for assembly
            move to warehouse
            put in warehouse

            **Check for battery**

            {"MoveToChargerOperation", "MoveToAssemblyOperation",
            "MoveToStorageOperation","PutAssemblyOperation",
            "PickAssemblyOperation","PickWarehouseOperation",
            "PutWarehouseOperation"};
        */

        System.out.println("Start");

        agvInfo = agvController.setProgram("MoveToStorageOperation");
        timeout(1000);

        warehouseInventory = warehouseController.pickItem("3");
        agvInfo = agvController.setProgram("PickWarehouseOperation");
        timeout(1000);

        System.out.println("picked");
        agvInfo = agvController.setProgram("MoveToAssemblyOperation");
        timeout(1000);

        agvInfo = agvController.setProgram("PutAssemblyOperation");
        assemblyInfo = assemblyController.insertItem();
        timeout(1000);

        System.out.println("Assembly");

        agvInfo = agvController.setProgram("PickAssemblyOperation");
        timeout(5000);

        agvInfo = agvController.setProgram("MoveToStorageOperation");
        timeout(5000);

        agvInfo = agvController.setProgram("PutWarehouseOperation");
        warehouseInventory = warehouseController.putItem("3");
        timeout(5000);

        agvInfo = agvController.setProgram("MoveToChargerOperation");
        timeout(5000);
    }

    private void timeout(int time) throws InterruptedException, IOException {
        Thread.sleep(time);
        updateText(new ActionEvent());
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