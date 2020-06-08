package com.example.notificationscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void scheduleJob(View view) {
        RadioGroup networkOptions = findViewById(R.id.networkOptions);
        int selectedNetworkID = networkOptions.getCheckedRadioButtonId();
        int selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE;
    }

    public void cancelJobs(View view) {
    }
}
