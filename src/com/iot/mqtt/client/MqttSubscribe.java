package com.iot.mqtt.client;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.iot.sql.SqlManager;
import com.mysql.fabric.xmlrpc.Client;

public class MqttSubscribe implements MqttCallback {

	private SqlManager sqlManager = new SqlManager();
	private MqttClient client;

	public static void main(String[] args) {

		MqttSubscribe mqttSubscribe = new MqttSubscribe();
		mqttSubscribe.subscribeToMqtt();

	}

	private void subscribeToMqtt() {
		String topic = "Project2";
		String content = "MqttPublishSample";
		int qos = 2;
		String broker = "tcp://iot.eclipse.org:1883";
		String clientId = "JavaSample89";
		//MemoryPersistence persistence = new MemoryPersistence();
		try {
			client = new MqttClient(broker, clientId);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			System.out.println("Connecting to broker: " + broker);
			client.connect(connOpts);
			client.setCallback(this);
			client.subscribe(topic);
			System.out.println("Connected");
			System.out.println("Publishing message: " + content);
			MqttMessage message = new MqttMessage(content.getBytes());
			//message.setQos(qos);
			client.publish(topic, message);
			System.out.println("Message published");
			//client.disconnect();
			// System.exit(0);
		} catch (MqttException me) {
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();
		}
	}

	@Override
	public void connectionLost(Throwable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void messageArrived(String topic, MqttMessage message)
			throws Exception {
		System.out.println("callback msg=" + message.toString());
		sqlManager.updateSqlTable(message.toString());
		client.unsubscribe(topic);
		client.disconnect();
	}
}