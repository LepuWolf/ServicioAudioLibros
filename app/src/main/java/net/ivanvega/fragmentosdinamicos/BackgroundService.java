package net.ivanvega.fragmentosdinamicos;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.PlatformVpnProfile;
import android.net.Uri;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.IOException;

public class BackgroundService extends Service implements MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl, View.OnTouchListener {
    MediaPlayer player;
    MediaController mediaController;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String uri="";
        String titulo;
        String contenido;
        uri=intent.getStringExtra("uri");
        contenido = "Reproduciendo: "+uri;
        titulo = intent.getStringExtra("titulo");
        Toast.makeText(this, "uri: "+uri, Toast.LENGTH_SHORT).show();
        Intent intent1 = new Intent(this, MainActivity.class);

        mostrarNotificacion(this, intent1,titulo,contenido);

        if(player == null){
            player = new MediaPlayer();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    onDestroy();
                }
            });
            mediaController = new MediaController(this);
            player.setOnPreparedListener(this);
            try {
                player.setDataSource(this,
                        Uri.parse(uri));
                player.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{
            player.stop();
            player.release();
            player = new MediaPlayer();
            mediaController = new MediaController(this);
            player.setOnPreparedListener(this);
            try {
                player.setDataSource(this,
                        Uri.parse(uri));
                player.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return START_NOT_STICKY;
    }

    private void mostrarNotificacion(Context context, Intent intent, String titulo, String contenido) {
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "My Notification")
                .setContentTitle(titulo)
                .setContentText(contenido)
                .setSmallIcon(R.drawable.ic_baseline_book_24)
                .setAutoCancel(true)
                //.setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(resultPendingIntent);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1001, builder.build());
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Servicio Terminado", Toast.LENGTH_SHORT).show();
        player.stop();
        player.release();
        try{
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(1001);
        }catch (Exception ex){

        }
        super.onDestroy();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        player.start();
        mediaController.setMediaPlayer(this);
        mediaController.setEnabled(true);
        mediaController.setPadding(0,0,0,110);
        mediaController.show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void start() {

    }

    @Override
    public void pause() {

    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public void seekTo(int pos) {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
}
