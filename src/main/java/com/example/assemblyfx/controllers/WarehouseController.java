package com.example.assemblyfx.controllers;

import Warehouse.Inventory;
import Warehouse.Warehouse;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class WarehouseController {

    public AnchorPane warehouse;
    public TableView tableViewInventory;
    public TableColumn<Inventory, String> colTime;
    public TableColumn<Inventory, String> col1;
    public TableColumn<Inventory, String> col2;
    public TableColumn<Inventory, String> col3;
    public TableColumn<Inventory, String> col4;
    public TableColumn<Inventory, String> col5;
    public TableColumn<Inventory, String> col6;
    public TableColumn<Inventory, String> col7;
    public TableColumn<Inventory, String> col8;
    public TableColumn<Inventory, String> col9;
    public TableColumn<Inventory, String> col10;
    public TableColumn<Inventory, String> col11;
    public Button updateButton;
    public TextField putField;
    public ChoiceBox pickCB;
    public Button pickBtn;
    public ChoiceBox putCB;
    public Button putBtn;

    private MainController helloController;
    public Label WarehouseLabel;

    private Warehouse warehouseIns;

    private Inventory inventory;

    private String itemPicked;

    public void initialize() throws IOException, InterruptedException {
        warehouseIns = new Warehouse();
        inventory = warehouseIns.getStringArray();
        initTableView();
        initChoiceBoxes();
    }

    private void initChoiceBoxes() {
        int size = 10;
        String[] str = new String[size];
        for (int i = 1; i <= size; i++) {
            str[i-1] = String.valueOf(i);
        }
        pickCB.setItems(FXCollections.observableArrayList(str));
        putCB.setItems(FXCollections.observableArrayList(str));
    }

    public void injectMainController(MainController helloController) {
        this.helloController = helloController;
    }

    private void initTableView() throws IOException, InterruptedException {

        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        col1.setCellValueFactory(new PropertyValueFactory<>("tray1"));
        col2.setCellValueFactory(new PropertyValueFactory<>("tray2"));
        col3.setCellValueFactory(new PropertyValueFactory<>("tray3"));
        col4.setCellValueFactory(new PropertyValueFactory<>("tray4"));
        col5.setCellValueFactory(new PropertyValueFactory<>("tray5"));
        col6.setCellValueFactory(new PropertyValueFactory<>("tray6"));
        col7.setCellValueFactory(new PropertyValueFactory<>("tray7"));
        col8.setCellValueFactory(new PropertyValueFactory<>("tray8"));
        col9.setCellValueFactory(new PropertyValueFactory<>("tray9"));
        col10.setCellValueFactory(new PropertyValueFactory<>("tray10"));
        col11.setCellValueFactory(new PropertyValueFactory<>("state"));

        tableViewInventory.getItems().add(warehouseIns.getStringArray());
    }

    public void updateTable(ActionEvent actionEvent) throws IOException, InterruptedException {
        //not sure if needed
        Platform.runLater(()->{
            try {
                inventory = warehouseIns.getStringArray();
                tableViewInventory.getItems().add(inventory);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Inventory getInventory(){
        return this.inventory;
    }
    public void putAction(ActionEvent actionEvent) throws IOException, InterruptedException {

        String itemName = putField.getText();
        helloController.agvHoldingItem = null;

        int selected = putCB.getSelectionModel().getSelectedIndex() + 1;

        Inventory result = warehouseIns.insertItem(itemName, String.valueOf(selected));
        inventory = result;

        tableViewInventory.getItems().add(result);
    }

    public void pickAction(ActionEvent actionEvent) throws IOException, InterruptedException {

        int selected = pickCB.getSelectionModel().getSelectedIndex() + 1;

        itemPicked = inventory.getX(selected);
        putField.setText("Picked: " + itemPicked);
        helloController.agvHoldingItem = itemPicked;

        Inventory result = warehouseIns.pickItem(String.valueOf(selected));
        inventory = result;

        tableViewInventory.getItems().add(result);
    }

    public Inventory pickItem(String trayID) throws IOException, InterruptedException {

        inventory = warehouseIns.pickItem(trayID);

        itemPicked = inventory.getX(Integer.parseInt(trayID));
        putField.setText("Picked: " + itemPicked);

        helloController.agvHoldingItem = itemPicked;

        return inventory;
    }

    public Inventory putItem(String trayID) throws IOException, InterruptedException {
        return warehouseIns.insertItem(trayID, helloController.agvHoldingItem);
    }

    public void returnToControls(ActionEvent actionEvent) throws IOException, InterruptedException {
        helloController.warehouseInventory = inventory;
        helloController.refreshText();
        helloController.tabPane.getSelectionModel().select(0);
    }
}
