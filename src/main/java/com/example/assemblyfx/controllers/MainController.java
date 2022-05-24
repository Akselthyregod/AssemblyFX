package com.example.assemblyfx.controllers;

import MQTT.AssemblyMQTT;
import Warehouse.Inventory;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class MainController {

    public AnchorPane warehouse;
    public AnchorPane agv;
    public AnchorPane assembly;
    public TextArea textAreaMap;
    public TextArea textAssembly;
    public TextArea textAGV;
    public TextArea textWarehouse;
    public Button startButton;
    public Label AssemblyState;
    public Label AGVState;
    public TableView statusTable;
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

    private final ObservableList<Status> statusObservableList = FXCollections.observableArrayList();


    public void initialize() throws MqttException, IOException, InterruptedException {

        updateInfo();
        updateText(new ActionEvent());
        initTableView();

    }


    private void initTableView() throws IOException, InterruptedException {

        TableColumn<Status, String> col1 = new TableColumn<>("Time");
        TableColumn<Status, String> col2 = new TableColumn<>("AGV");
        TableColumn<Status, String> col3 = new TableColumn<>("Assembly");
        TableColumn<Status, String> col4 = new TableColumn<>("Warehouse");
        statusTable.getColumns().addAll(col1, col2, col3, col4);


        col1.setCellValueFactory(status -> new SimpleStringProperty(status.getValue().time()));
        col2.setCellValueFactory(status -> new SimpleStringProperty(status.getValue().stateAGV()));
        col3.setCellValueFactory(status -> new SimpleStringProperty(status.getValue().stateAssembly()));
        col4.setCellValueFactory(status -> new SimpleStringProperty(status.getValue().stateWarehouse()));

        statusTable.setItems(statusObservableList);
    }

    private void addToTable(){

        statusObservableList.add(new Status(agvInfo.get("state"), assemblyInfo.get("state"), warehouseInventory.getState(), new Date().toString()));

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

        addToTable();
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
            flow1();

            timeout(500);

            flow2();
            timeout(500);

            System.out.println("picked");
            flow3();
            timeout(500);

            agvInfo = agvController.setProgram("PutAssemblyOperation");
            assemblyInfo = assemblyController.insertItem();
            timeout(500);

            System.out.println("Assembly");

            agvInfo = agvController.setProgram("PickAssemblyOperation");
            timeout(500);

            agvInfo = agvController.setProgram("MoveToStorageOperation");
            timeout(500);

            agvInfo = agvController.setProgram("PutWarehouseOperation");
            warehouseInventory = warehouseController.putItem("3");
            timeout(500);

            agvInfo = agvController.setProgram("MoveToChargerOperation");
            timeout(500);

    }

    private void flow1() throws IOException, InterruptedException {
        agvInfo = agvController.setProgram("MoveToStorageOperation");
        addToTable();

    }
    private void flow2() throws IOException, InterruptedException {
        warehouseInventory = warehouseController.pickItem("3");
        agvInfo = agvController.setProgram("PickWarehouseOperation");
        addToTable();
    }
    private void flow3() throws IOException, InterruptedException {
        agvInfo = agvController.setProgram("MoveToAssemblyOperation");
        addToTable();
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


    public record Status(String stateAGV, String stateAssembly, String stateWarehouse, String time){}

}