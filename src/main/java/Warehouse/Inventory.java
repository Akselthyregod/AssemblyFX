package Warehouse;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private String[] inventory;


    public Inventory(String[] string){
        inventory = new String[12];

        System.arraycopy(string, 0, inventory, 0, 12);

        /*
        2022-05-16T08:58:03.6480577+00:00
        ->
        08:58:03
        */
        String time = inventory[0];
        inventory[0] = time.substring(11,19);
    }

    public String getX(int x){
        return inventory[x];
    }

    public String getTime(){
        return inventory[0];
    }
    public String getTray1(){
        return inventory[1];
    }
    public String getTray2(){
        return inventory[2];
    }
    public String getTray3(){
        return inventory[3];
    }
    public String getTray4(){
        return inventory[4];
    }
    public String getTray5(){
        return inventory[5];
    }
    public String getTray6(){
        return inventory[6];
    }
    public String getTray7(){
        return inventory[7];
    }
    public String getTray8(){
        return inventory[8];
    }
    public String getTray9(){
        return inventory[9];
    }
    public String getTray10(){
        return inventory[10];
    }
    public String getState(){
        return inventory[11];
    }
}
