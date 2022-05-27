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
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
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

    /*
        Keys:
        state
        battery
        program name
        timestamp
     */
    private Map<String, String> status;

    private String[] allPrograms =
            {"MoveToChargerOperation", "MoveToAssemblyOperation",
            "MoveToStorageOperation","PutAssemblyOperation",
            "PickAssemblyOperation","PickWarehouseOperation",
            "PutWarehouseOperation"};

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        chooseProgram.getItems().addAll(allPrograms);
        this.status = new HashMap<>();
    }

    public Map<String, String> setProgram(String program) throws IOException, InterruptedException {
        agv1.callSetProgram(program);
        return getStatus();
    }

    public void onRunProgram(ActionEvent actionEvent) throws IOException, InterruptedException {
        agv1.callSetProgram((String) chooseProgram.getValue());
    }

    public void onGetStatus() throws IOException, InterruptedException {
        String aa = String.valueOf(agv1.callGetStatus());
        currentStatus.setText(aa);
    }

    public Map<String, String> getStatus() throws IOException, InterruptedException {
        JSONObject obj = agv1.callGetStatus();

        if(obj == null){
            return status;
        }

        status.put("state", String.valueOf(obj.getInt("state")));
        status.put("battery", String.valueOf(obj.getInt("battery")));
        status.put("program name", obj.getString("program name"));
        status.put("timestamp", obj.getString("timestamp"));

        return status;
    }
}