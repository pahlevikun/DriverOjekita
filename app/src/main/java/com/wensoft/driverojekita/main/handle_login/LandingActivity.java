package com.wensoft.driverojekita.main.handle_login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wensoft.driverojekita.R;
import com.wensoft.driverojekita.main.MainActivity;

public class LandingActivity extends AppCompatActivity {

    private TextView tvLogin, tvRegister;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        boolean pertamaJalan = getSharedPreferences("DATA",MODE_PRIVATE).getBoolean("perdana",true);
        boolean session = getSharedPreferences("DATA",MODE_PRIVATE).getBoolean("session",false);

        if(pertamaJalan==true){
            introAct();
        }

        if(session==true){
            Intent intent = new Intent(LandingActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        tvLogin = (TextView) findViewById(R.id.tvLandingLogin);

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LandingActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

    }

    private void introAct() {
        SharedPreferences sharedPreferences =  getSharedPreferences("DATA",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("perdana",false);
        editor.commit();

        //Intent intro = new Intent(LandingActivity.this, IntroActivity.class);
        //LandingActivity.this.startActivity(intro);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tekan lagi untuk keluar!", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
