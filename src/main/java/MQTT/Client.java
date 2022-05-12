package MQTT;

import org.eclipse.paho.client.mqttv3.MqttException;

public class Client {
    public static void main(String[] args) throws MqttException {
        AssemblyMQTT instance = AssemblyMQTT.getInstance();

        instance.connect();
        instance.publishMessage(9999);
    }
}
