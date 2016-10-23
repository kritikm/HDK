package com.first.kritikm.hdk;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Transition;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        final ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();
        final Transition  exitTrans=new Fade();

//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        this.setContentView(R.layout.splash);
        Thread timerThread=new Thread(){
            public void run(){
                try{
                    sleep(1500);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    Intent intent=new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(intent,options.toBundle());
                    getWindow().setExitTransition(exitTrans);
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}