package com.stivinsonmartinez.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

public class SplashActivity extends Activity {

    public static final int segundos=4;
    public static int milisegundos=segundos*1000;
    public static final int delay=2;
    private ProgressBar pbprogreso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        pbprogreso=(ProgressBar)findViewById(R.id.progressbar);
        empezaranimacion();

    }

    public void empezaranimacion() {

        new CountDownTimer(milisegundos, 1300) {
            @Override
            public void onTick(long millisUntilFinished) {
                pbprogreso.setProgress(establecer_progreso(millisUntilFinished));
                pbprogreso.setMax(maximo_progreso());
            }

            @Override
            public void onFinish() {
                Intent main = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(main);
                finish();
            }
        }.start();

    }

    public int establecer_progreso(long miliseconds){

        return (int) ((milisegundos-miliseconds)/1000);
    }

    public int maximo_progreso(){
        return segundos-delay;
    }


}
