package com.example.notificationscheduler;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class NotificationJobService extends JobService {

    NotificationManager mNotifyManager;

    private JobTask jobTask;

    // Notification Channel ID.
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        jobTask = new JobTask(this);
        jobTask.execute(jobParameters);

        // Set up the notification content intent to launch the app when clicked
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder
                (this, PRIMARY_CHANNEL_ID)
                .setContentTitle("Job Service")
                .setContentText("Your Job ran to completion!")
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(R.drawable.ic_job_running)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true);

        mNotifyManager.notify(0, builder.build());
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (jobTask != null) {
            jobTask.cancel(true);
            Toast.makeText(getApplicationContext(), "Job Interrupted", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private class JobTask extends AsyncTask<JobParameters, Void, Boolean> {
        private final JobService jobService;

        private JobParameters parameters;

        public JobTask(JobService jobService) {
            this.jobService = jobService;
        }

        @Override
        protected Boolean doInBackground(JobParameters... params) {
            // Create the notification channel
            try {
                createNotificationChannel();
                parameters = params[0];
                Thread.sleep(5000);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                Toast.makeText(getApplicationContext(), "Job Complete", Toast.LENGTH_SHORT).show();
            }
            jobService.jobFinished(parameters, !aBoolean);
        }

        /**
         * Creates a Notification channel, for OREO and higher
         */
        public void createNotificationChannel() {
            // Define notification manager object.
            mNotifyManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            // Notification channels are only available in OREO and higher.
            // So, add a check on SDK version.
            if (Build.VERSION.SDK_INT >=
                    Build.VERSION_CODES.O) {
                // Create the NotificationChannel with all the parameters.
                NotificationChannel notificationChannel = new NotificationChannel
                        (PRIMARY_CHANNEL_ID,
                                "Job Service notification",
                                NotificationManager.IMPORTANCE_HIGH);

                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.enableVibration(true);
                notificationChannel.setDescription
                        ("Notification from Job Service");

                mNotifyManager.createNotificationChannel(notificationChannel);
            }
        }
    }
}
