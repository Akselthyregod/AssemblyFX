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
    public TabPane tabPane;
    public Button moveChargeBtn;
    public Button moveAssemblyBtn;
    public Button moveWarehouseBtn;
    public Button putAssemblyBtn;
    public Button pickAssemblyBtn;
    public Button putWarehouseBtn;
    public Button pickWarehouseBtn;
    @FXML
    private Label welcomeText;
    AssemblyMQTT mqtt;

    @FXML
    private WarehouseController warehouseController;

    @FXML
    private AssemblyController assemblyController;

    @FXML
    private AGVController agvController;

    //Inventory getters:
    // getTime, getState, getTray(1-10)
    public Inventory warehouseInventory;

    /*
    Assembly keys:
    "lastOP"
    "currentOP"
    "state"
    "time"
    "health"
    */
    private Map<String, String> assemblyInfo;

    /*
    AGV Keys:
    state
    battery
    program name
    timestamp
     */
    private Map<String, String> agvInfo;
    private String agvPosition;
    public String agvHoldingItem;
    public String assemblyHoldingItem;

    private final ObservableList<Status> statusObservableList = FXCollections.observableArrayList();


    public void initialize() throws MqttException, IOException, InterruptedException {

        updateInfo();
        updateText(new ActionEvent());
        initTableView();
        checkButtons();

        assemblyController.startMessage();
        assemblyInfo = assemblyController.getInfo();

        //inject this controller in warehouse to allow it to access tabPane
        warehouseController.injectMainController(this);
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

    //pulls info from services. Takes a bit of time to execute
    private void updateInfo() throws IOException, InterruptedException {
        getWarehouseInventory();
        getAssemblyMap();
        getAGVInfo();
    }

    //button update
    public void updateText(ActionEvent actionEvent) throws IOException, InterruptedException {
        updateInfo();
        refreshText();
    }

    //refresh text areas from maps
    public void refreshText() throws IOException, InterruptedException {
        updateInfo();

        textAssembly.setText(
                "State: " + assemblyInfo.get("state") +
                "\nItem: " + assemblyHoldingItem );

        textAGV.setText(
                "Position: " + agvPosition +
                "\nItem: " + agvHoldingItem +
                "\nState: " + agvInfo.get("state") +
                "\n" + agvInfo.get("program name") +
                "\nBattery: " + agvInfo.get("battery"));

        textWarehouse.setText(
                "State: " + warehouseInventory.getState());

        setWarehouseText();
        addToTable();
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

    public void moveChargeButton(ActionEvent actionEvent) throws IOException, InterruptedException {
        agvInfo = agvController.setProgram("MoveToChargerOperation");
        agvPosition = "Charging";
        refreshText();
        checkButtons();
    }

    public void moveAssemblyButton(ActionEvent actionEvent) throws IOException, InterruptedException {
        agvInfo = agvController.setProgram("MoveToAssemblyOperation");
        agvPosition = "Assembly";
        refreshText();
        checkButtons();
    }

    public void moveWarehouseButton(ActionEvent actionEvent) throws IOException, InterruptedException {
        agvInfo = agvController.setProgram("MoveToStorageOperation");
        agvPosition = "Warehouse";
        refreshText();
        checkButtons();
    }

    public void putAssemblyButton(ActionEvent actionEvent) throws IOException, InterruptedException, MqttException {
        agvInfo = agvController.setProgram("PutAssemblyOperation");
        assemblyInfo = assemblyController.insertItem();
        assemblyHoldingItem = agvHoldingItem;
        agvHoldingItem = null;
        refreshText();
        checkButtons();
    }

    public void pickAssemblyButton(ActionEvent actionEvent) throws IOException, InterruptedException {
        agvInfo = agvController.setProgram("PickAssemblyOperation");
        agvHoldingItem = assemblyHoldingItem;
        assemblyHoldingItem = null;
        refreshText();
        checkButtons();
    }

    public void putWarehouseButton(ActionEvent actionEvent) throws IOException, InterruptedException {
        agvInfo = agvController.setProgram("PutWarehouseOperation");
        checkButtons();
        tabPane.getSelectionModel().selectNext();
    }

    public void pickWarehouseButton(ActionEvent actionEvent) throws IOException, InterruptedException {
        agvInfo = agvController.setProgram("PickWarehouseOperation");
        checkButtons();
        tabPane.getSelectionModel().selectNext();
    }

    private void checkButtons() throws InterruptedException, IOException {

        //disable all buttons, then depending on current position enable specific ones
        moveChargeBtn.setDisable(true);

        moveAssemblyBtn.setDisable(true);
        moveWarehouseBtn.setDisable(true);

        putWarehouseBtn.setDisable(true);
        pickWarehouseBtn.setDisable(true);

        putAssemblyBtn.setDisable(true);
        pickAssemblyBtn.setDisable(true);

        int battery = Integer.parseInt(agvInfo.get("battery"));

        if(battery<25){
            moveChargeBtn.setDisable(false);
        }else if(agvPosition == null){
            //if null forced move
            moveChargeBtn.setDisable(false);
            moveAssemblyBtn.setDisable(false);
            moveWarehouseBtn.setDisable(false);
        }else if(agvPosition.equals("Charging")){
            if(agvInfo.get("battery").equals("100")){
                moveChargeBtn.setDisable(true);
                moveAssemblyBtn.setDisable(false);
                moveWarehouseBtn.setDisable(false);
            }
            //recursive call to recheck battery level
            else{
                getAGVInfo();
                checkButtons();
            }
        }else if(agvPosition.equals("Warehouse")){
            moveChargeBtn.setDisable(false);
            moveAssemblyBtn.setDisable(false);

            putWarehouseBtn.setDisable(false);
            pickWarehouseBtn.setDisable(false);
        }else if(agvPosition.equals("Assembly")){
            moveChargeBtn.setDisable(false);
            moveWarehouseBtn.setDisable(false);

            putAssemblyBtn.setDisable(false);
            pickAssemblyBtn.setDisable(false);
        }
    }

    //record used to collect states from each service in an object
    public record Status(String stateAGV, String stateAssembly, String stateWarehouse, String time){}

}