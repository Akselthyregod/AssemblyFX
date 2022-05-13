package com.example.assemblyfx.controllers;

import Warehouse.Inventory;
import Warehouse.Warehouse;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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
    public Button updateButton;

    private HelloController helloController;
    public Label WarehouseLabel;

    private Warehouse warehouseIns;

    public void initialize() throws IOException, InterruptedException {
        initTableView();
        warehouseIns = new Warehouse();

        tableViewInventory.getItems().add(warehouseIns.getStringArray());

    }

    public void injectMainController(HelloController helloController) {
        this.helloController = helloController;

    }

    private void initTableView(){

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

    }

    public void UpdateTable(MouseEvent actionEvent) throws IOException, InterruptedException {

        //not sure if needed
        Platform.runLater(()->{
            try {
                tableViewInventory.getItems().add(warehouseIns.getStringArray());
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

    }
}
