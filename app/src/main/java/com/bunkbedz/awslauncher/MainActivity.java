package com.bunkbedz.awslauncher;

import androidx.appcompat.app.AppCompatActivity;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.model.InstanceStateName;

import android.graphics.ColorFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private ImageView powerButton;
    private AwsServer awsServer;
    private ExecutorService requestThread;
    private InstanceStateName serverState = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        powerButton = findViewById(R.id.power_button);
        powerButton.setColorFilter(getColor(android.R.color.darker_gray));
        powerButton.setAlpha(0.3f);

        awsServer = new AwsServer(getString(R.string.instanceId),
                Region.of(getString(R.string.region)),
                getString(R.string.access_key_id),
                getString(R.string.secret_access_key));
        requestThread = Executors.newSingleThreadExecutor();
        requestThread.execute(() -> {
            serverState = awsServer.getInstanceStateName();
            updateButtonByState();
        });

        powerButton.setOnClickListener(new ButtonClickListener());
    }

    private void updateButtonByState() {
        runOnUiThread(() ->{
            powerButton.setAlpha(1f);

            switch (serverState) {
                case STOPPED:
                    powerButton.setColorFilter(getColor(android.R.color.holo_red_dark));
                    break;
                case RUNNING:
                    powerButton.setColorFilter(getColor(android.R.color.holo_green_dark));
                    break;
                case PENDING:
                    powerButton.setColorFilter(getColor(android.R.color.holo_orange_light));
                    break;
                case STOPPING:
                    powerButton.setColorFilter(getColor(android.R.color.holo_orange_dark));
                    break;
                default:
                    break;
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (requestThread != null) {
            requestThread.shutdown();
        }
    }

    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (serverState != null) {
                switch (serverState) {
                    case STOPPED:
                        serverState = InstanceStateName.PENDING;
                        updateButtonByState();
                        requestThread.execute(() -> {
                            awsServer.startServer();
                            serverState = InstanceStateName.RUNNING;
                            updateButtonByState();
                        });
                        break;
                    case RUNNING:
                        serverState = InstanceStateName.STOPPING;
                        updateButtonByState();
                        requestThread.execute(() -> {
                            awsServer.stopServer();
                            serverState = InstanceStateName.STOPPED;
                            updateButtonByState();
                        });
                        break;
                    case PENDING:
                        // FALLTHROUGH
                    case STOPPING:
                        // FALLTHROUGH
                    default:
                        break;
                }
            }
        }
    }
}
