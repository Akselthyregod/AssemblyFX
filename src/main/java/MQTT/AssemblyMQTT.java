package MQTT;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;


public class AssemblyMQTT {
    private final String SUB_TOPIC = "emulator/status";
    private final String SUB_TOPIC2 = "emulator/checkhealth";
    private final String SUB_TOPIC3 = "emulator/response";
    private final String PUB_TOPIC = "emulator/operation";
    private final String BROKER_ID = "tcp://mqtt.localhost:1883";
    private static String CLIENT_ID = "AssemblyMQTT";
    private final String[] topicArray= {SUB_TOPIC2,SUB_TOPIC,SUB_TOPIC3};
    private static AssemblyMQTT MQTT_instance = null;
    private String[] content;
    private String health;
    private String response;

    MqttClient client;
    private AssemblyMQTT() throws MqttException {
        content = new String[4];
        MemoryPersistence persistence = new MemoryPersistence();
        client = new MqttClient(BROKER_ID,CLIENT_ID,persistence);

    }
    public void connect() throws MqttException {
        //Hvad fanden skal vi bruge det her til
        MqttConnectOptions options= new MqttConnectOptions();
        options.setUserName("testMQTTClient");
        options.setPassword("testPassword".toCharArray());
      //  options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        //options.setMaxInflight(1000);
        // ???


        //Vores egen implementering af et MQTTcallback interface, håndterer hvad der skal ske når man modtager en besked osv.
        //Vi skal ændre implementeringen så den gør det vi vil med beskeden i fremtiden, lige nu tester vi bare.
        client.setCallback(new PushCallback());

        //connecter *crossing fingers*
        client.connect(options);

        //subscribe til topics
        client.subscribe(topicArray);

    }

    public void publishMessage(int message) throws MqttException {

            String s = "{\"ProcessID\":"+ message +"}";
            MqttMessage mqttMessage = new MqttMessage(s.getBytes(StandardCharsets.UTF_8));
            // qos er enten 0,1,2. Vi skal altid vælge to fordi det sikrer at beskeden altid bliver sendt 1 gang.
            mqttMessage.setQos(2);
            client.publish(PUB_TOPIC,mqttMessage);

    }
    public static AssemblyMQTT getInstance() throws MqttException {
        if (MQTT_instance == null) {
            MQTT_instance = new AssemblyMQTT();
        }
        return MQTT_instance;
    }
    public String removeChar(String s) {
        s = s.substring(1,s.length()-1);
        return s;
    }
    public String[] processMessage(String message) throws InterruptedException {
        String s = removeChar(message);
        content = s.split(",");
        return content;
    }

    /*
    Assembly keys:
    "lastOP"
    "currentOP"
    "state"
    "time"
    "health"
    */
    public Map<String, String> getContent(){
        Map<String, String> map = new HashMap<>();

        map.put("lastOP", content[0]);
        map.put("currentOP", content[1]);
        map.put("state", content[2]);
        map.put("time", content[3]);
        map.put("health", getHealth());

        return map;
    }
    public void disconnectClient() throws MqttException {
        client.disconnect();
        client.close();
        System.exit(0);
    }
    public String getLastOperation(){
        return (content[0]);
    }
    public String getCurrentOperation(){
        return (content[1]);
    }
    public String getAssemblyStationState(){
        return (content[2]);
    }
    public String getAssemblyTimeStamp(){
        return content[3];
    }
    public String getHealth(){
        return this.health;
    }
    public void printContent() {
        System.out.println("Last Operation:"+ getLastOperation());
        System.out.println("operation: "+ getCurrentOperation());
        System.out.println("Assembly State: "+getAssemblyStationState());
        System.out.println("Time: "+getAssemblyTimeStamp());
        System.out.println(getHealth());

    }
    public void checkHealth(String message) {
        this.health=removeChar(message);
    }

    public void setResponse(String message){
        this.response= removeChar(message);

    }
}
