package com.example.assemblyfx.controllers;

import AGV.AGV;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AGVController implements Initializable {
    @FXML
    public ChoiceBox chooseProgram;
    @FXML
    public TextField currentStatus;
    @FXML
    public AnchorPane agv;
    @FXML
    public Button getStatusButton;
    @FXML
    public Button runButton;
    @FXML
    public Text stateText;

    AGV agv1 = new AGV();


    //Need a map of all values/states of the component

    private String[] allPrograms =
            {"MoveToChargerOperation", "MoveToAssemblyOperation",
            "MoveToStorageOperation","PutAssemblyOperation",
            "PickAssemblyOperation","PickWarehouseOperation",
            "PutWarehouseOperation"};

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        chooseProgram.getItems().addAll(allPrograms);
    }

    public void onRunProgram(ActionEvent actionEvent) throws IOException, InterruptedException {
        agv1.callSetProgram((String) chooseProgram.getValue());
    }

    public void onGetStatus() throws IOException, InterruptedException {
        String aa = String.valueOf(agv1.callGetStatus());
        currentStatus.setText(aa);
    }
}