package com.prajwal.textrecognision;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class Player extends AppCompatActivity {
    Button play,stop;
    Context context;
    Toolbar toolbar;
    TextToSpeech tts;
    String text;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        play = findViewById(R.id.player_play);
        stop = findViewById(R.id.player_stop);
        textView = findViewById(R.id.player_text);

        //Adding Custom toolbar
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle( "Readify - Player");

        context = this;
        Bundle bundle = getIntent().getExtras();
        text = bundle.getString("text");
        textView.setText(text);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    tts.stop();
                }
                catch (Exception  e){
                    Log.e("Stop_Player", e.getMessage());
                }
            }
        });
    }
    public Player(){}
    public Player(String t,Context c){
        text = t;
        context = c;
    }
    public void play(){
        try {
            //Toast.makeText(getApplicationContext(), "Name:" + User.getUsername(), Toast.LENGTH_SHORT).show();
            //showHistory();
                tts =new TextToSpeech(context,new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                            if (status == TextToSpeech.SUCCESS) {
                                int res = tts.setLanguage(Locale.US);
                                if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
                                    Log.e("TTs", "Lang unavailable");
                                } else {
                                    if (text!= null) {

                                        tts.setPitch(1);
                                        tts.setSpeechRate(1);
                                        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                                    } else {
                                        tts.setPitch(1);
                                        tts.setSpeechRate(1);
                                        tts.speak("Please detect text", TextToSpeech.QUEUE_FLUSH, null);
                                    }
                                }
                            }
                            else {
                                textView.setText("Player Unavailable!");
                            }
                        }
                });



        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error:" +e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Player_play", e.getMessage());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            tts.stop();
        }
        catch (Exception e){
            Log.e("PausePlayer", e.getMessage());
        }
    }
}