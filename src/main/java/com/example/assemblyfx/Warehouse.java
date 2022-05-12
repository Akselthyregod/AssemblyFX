package com.example.assemblyfx;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Warehouse {

    public Warehouse() {
    }

    private JSONObject callGetInventory() throws IOException, InterruptedException {

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

        String raw = new String (err.readAllBytes(), StandardCharsets.UTF_8);

        JSONObject obj = new JSONObject(raw);


        return obj;


    }

    private String callPickItem(String trayID) throws IOException, InterruptedException {

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

        String[] args = new String[]{"pickItem", trayID};

        Process process = Runtime.getRuntime().exec("java -jar "+ filePath +" " + args[0] + " " + args[1]);
        InputStream in = process.getInputStream();
        InputStream err = process.getErrorStream();

        String rawErr = new String (err.readAllBytes(), StandardCharsets.UTF_8);
        String rawIn = new String (in.readAllBytes(), StandardCharsets.UTF_8);

        System.out.println(rawErr);
        System.out.println("-----");
        System.out.println(rawIn);


        return "Pick";


    }

    private String callInsertItem(String name, String trayID) throws IOException, InterruptedException {

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

        String[] args = new String[]{"insertItem", name, trayID};

        Process process = Runtime.getRuntime().exec("java -jar "+ filePath +" " + args[0] + " " + args[1] + " " + args[2]);
        InputStream in = process.getInputStream();
        InputStream err = process.getErrorStream();

        String rawErr = new String (err.readAllBytes(), StandardCharsets.UTF_8);
        String rawIn = new String (in.readAllBytes(), StandardCharsets.UTF_8);

        System.out.println(rawErr);
        System.out.println("-----");
        System.out.println(rawIn);


        return "Insert";


    }

    public String insertItem(String name, String trayID) throws IOException, InterruptedException {
        return callInsertItem(name, trayID);
    }

    public String pickItem(String trayID) throws IOException, InterruptedException {
        return callPickItem(trayID);
    }

    private void parseJsonObj(JSONObject obj){

        int state = obj.getInt("State");
        System.out.println("State: " + state);

        JSONArray jsonInventory = obj.getJSONArray("Inventory");
        // {"State":0,
        // "Inventory":[{"Content":"Item 1","Id":1},{"Content":"Item 2","Id":2},{"Content":"Item 3","Id":3},{"Content":"Item 4","Id":4},{"Content":"Item 5","Id":5},{"Content":"Item 6","Id":6},{"Content":"Item 7","Id":7},{"Content":"Item 8","Id":8},{"Content":"Item 9","Id":9},{"Content":"Item 10","Id":10}],
        // "DateTime":"2022-05-12T10:15:40.9059514+00:00"}
        for (int i = 0; i < jsonInventory.length(); i++) {
            JSONObject element = jsonInventory.getJSONObject(i);
            System.out.print("Content: " + element.get("Content"));
            System.out.println("| Id: " + element.get("Id"));
        }

        String time = obj.getString("DateTime");
        System.out.println("Time: " + time);
    }

    public String getState() throws IOException, InterruptedException {
        JSONObject obj = callGetInventory();

        int state = obj.getInt("State");

        return String.valueOf(state);
    }

    public String getInventory() throws IOException, InterruptedException {
        JSONObject obj = callGetInventory();

        JSONArray jsonInventory = obj.getJSONArray("Inventory");

        return jsonInventory.toString();
    }

    public String getInventory(int i) throws IOException, InterruptedException {
        JSONObject obj = callGetInventory();

        JSONArray jsonInventory = obj.getJSONArray("Inventory");
        JSONObject element = jsonInventory.getJSONObject(i);

        System.out.print("Content: " + element.get("Content"));
        System.out.println("| Id: " + element.get("Id"));
        String elementString = "Content: " + element.get("Content") + "| Id: " + element.get("Id");

        return elementString;
    }

    public String getTime() throws IOException, InterruptedException {
        JSONObject obj = callGetInventory();

        String time = obj.getString("DateTime");

        return time;
    }
}
