package com.nguyenhongnhien.iot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;

import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttAsyncClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;

public class MainActivity extends AppCompatActivity {
    Switch lightSwitch;
    ImageView lightImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lightSwitch = (Switch)findViewById(R.id.lightSwitch);
        lightImage = (ImageView) findViewById(R.id.lightImage);

        lightSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String topic        = "temp";
            String content;
            int qos             = 0;
            String broker       = "tcp://103.173.154.157:1883";
            String clientId     = "JavaSample";
            MemoryPersistence persistence = new MemoryPersistence();
            if(isChecked){
                lightImage.setImageResource(R.drawable.light);
                content="1";
            }
            else {
                lightImage.setImageResource(R.drawable.off);
                content="0";
            }

            try {
                MqttConnectionOptions connOpts = new MqttConnectionOptions();
                connOpts.setCleanStart(false);
                connOpts.setUserName("nhien");
                connOpts.setPassword("123456".getBytes());
                MqttAsyncClient sampleClient = new MqttAsyncClient(broker, clientId, persistence);
                System.out.println("Connecting to broker: " + broker);
                IMqttToken token = sampleClient.connect(connOpts);
                token.waitForCompletion();
                System.out.println("Connected");
                System.out.println("Publishing message: "+content);
                MqttMessage message = new MqttMessage(content.getBytes());
                message.setQos(qos);
                token = sampleClient.publish(topic, message);
                token.waitForCompletion();
                System.out.println("Disconnected");
                System.out.println("Close client.");
                sampleClient.close();
                System.exit(0);
            } catch(MqttException me) {
                System.out.println("reason " + me.getReasonCode());
                System.out.println("msg " + me.getMessage());
                System.out.println("loc " + me.getLocalizedMessage());
                System.out.println("cause " + me.getCause());
                System.out.println("excep " + me);
                me.printStackTrace();

            }
        });
    }
}