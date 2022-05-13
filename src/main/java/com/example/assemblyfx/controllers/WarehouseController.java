package com.example.assemblyfx.controllers;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class WarehouseController {

    public AnchorPane warehouse;

    private HelloController helloController;
    public Label WarehouseLabel;

    public void initialize(){

    }

    public void injectMainController(HelloController helloController) {
        this.helloController = helloController;

    }
}
