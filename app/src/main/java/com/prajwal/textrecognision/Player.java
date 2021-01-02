package com.prajwal.textrecognision;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;

public class Player extends AppCompatActivity {

    Button play,stop,share;
    Context context;
    Toolbar toolbar;
    TextToSpeech tts,mtts;
    String text;
    TextView textView;
    String mAudioFilename = "";
    String mUtteranceID = "totts";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        play = findViewById(R.id.player_play);
        stop = findViewById(R.id.player_stop);
        textView = findViewById(R.id.player_text);
        share=findViewById(R.id.player_share);

        //Adding Custom toolbar
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle( "Readify - Player");

        context = this;
        final Bundle bundle = getIntent().getExtras();
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
        mtts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {


            @Override
            public void onInit(int status) {

                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        saveToAudioFile(text);

                    }

                    });


                //abcd();
            }

            //public void abcd() {


                    //f (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                      //  requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 50);

                    // Create audio file location
                   // File sddir = new File( Environment.getExternalStorageDirectory()+"/My File/");
                    //sddir.mkdir();


                    //mAudioFilename = sddir.getAbsolutePath() + "/" + mUtteranceID + ".mp4";



               // }

                public void saveToAudioFile (String text){
                    File mAudioFilename = new File(getCacheDir(), mUtteranceID + ".wav");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mtts.synthesizeToFile(text, null, (mAudioFilename), mUtteranceID);
                        Toast.makeText(Player.this, "Saved to 1" + mAudioFilename, Toast.LENGTH_LONG).show();
                    }else {

                        HashMap<String, String> hm = new HashMap();
                        hm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, mUtteranceID);
                        mtts.synthesizeToFile(text, hm, mAudioFilename.getPath());
                        Toast.makeText(Player.this, "Saved to " + mAudioFilename, Toast.LENGTH_LONG).show();
                    }
                    Intent share = new Intent(Intent.ACTION_SEND);

                    share.setType("audio/*");
                    share.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    share.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(Player.this,"com.prajwal.fileprovider",mAudioFilename));
                    startActivity(Intent.createChooser(share, "Share Sound File"));

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