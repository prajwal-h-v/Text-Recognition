package com.prajwal.textrecognision;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.prajwal.textrecognision.Item;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;


import java.io.IOException;
import java.util.List;

public class TextRecognition extends AppCompatActivity {
    Button capture, detect, play,logout;
    ImageView imageView;
    TextView textView, uname_disp,email_disp;
    String text;
    searchHistory history;
    FirebaseAuth firebaseAuth;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    Uri imageUri;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    LinearLayout header_layout;
    LayoutInflater mInflater;
    MenuItem login,signup;// = menu.findItem(R.id.menu_my_item);
    Menu menu;
    private ProgressBar progressBar;
    private static final String appInfo = "Designed and Developed by:\n" +
            "Prajwal H V\n" +
            "Shubhang C S\n" +
            "Sahil Sadik";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PICK_IMAGE = 100;

    private FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onStart() {
        firebaseAuth.addAuthStateListener(authStateListener);
        super.onStart();


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_recognition);
        capture = findViewById(R.id.capture_image);
        detect = findViewById(R.id.detect_text);
        imageView = findViewById(R.id.image_view);
        textView = findViewById(R.id.text_display);
        play = findViewById(R.id.btn_play);
        mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        logout = findViewById(R.id.btn_logout);
        progressBar = findViewById(R.id.progressBar3);
        progressBar.setVisibility(View.GONE);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               confirmSignout();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();



        //Adding Custom toolbar
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.nav_menu);
        navigationView.getMenu().setGroupVisible(R.id.menu_group, true);
        navigationView.getMenu().setGroupVisible(R.id.account_menu, false);
        drawerLayout = (DrawerLayout)findViewById(R.id.text_recog_activity);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open,R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_login:
                        if (User.islogin){
                            already_loggedin();

                        }
                        else {
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_signup:
                        if (User.islogin) {
                            already_loggedin();
                        }
                        else {
                            startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_info:
                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(TextRecognition.this, R.style.myDialog));
                        builder.setMessage(appInfo).setTitle(R.string.app_name).setIcon(R.drawable.icon_info);
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK button
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_logout:
                        confirmSignout();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_history:
                        showHistory();
                        break;
                }
                return true;
            }
        });
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null){
                    User.email = firebaseUser.getEmail();

                    Toast.makeText(getApplicationContext(), "You are logged in:"+User.email, Toast.LENGTH_SHORT).show();
                    history = new searchHistory(User.getUsername());
                    User.islogin = true;

                    try {
                        checkLogin();
                    }
                    catch (Exception e){
                        Log.d("LoginStatus", "Error::"+e.getMessage());
                    }
                    //Toast.makeText(getApplicationContext(), User.email+User.getUsername(), Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(getApplicationContext(), TextRecognition.class));
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_SHORT).show();
                    User.islogin=false;
                    checkLogin();
                }
            }
        };

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (User.islogin) {
                        Toast.makeText(getApplicationContext(), "Name:" + User.getUsername(), Toast.LENGTH_SHORT).show();
                        showHistory();
                    } else {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Error:" +e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
                imageView.setImageDrawable(null);
                textView.setText("");
                text = "";
            }
        });
        detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("");
                text = "";
                try {
                    detectTextFromImage();

                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), "detectTextFromImage:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
    void showHistory(){
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(TextRecognition.this, R.style.myDialog));
        String data ="";
        try {
            data = history.getData();
            data.replace(", ", "\n");
        }
        catch (Exception e){
            Log.e("History error", e +":"+e.getMessage() );
        }
        builder.setMessage(data).setTitle(User.getUsername()).setIcon(R.drawable.icon_info);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        System.out.println(data);
    }
    /*
    #######---------------End of onCreate---------------#######
    */
        private void already_loggedin(){

            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(TextRecognition.this, R.style.myDialog));
            builder.setMessage("You are already logged in").setTitle(User.getUsername()).setIcon(R.drawable.icon_info);
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    /*
    #######---------------End of already_loggedin---------------#######
    */
        private void checkLogin(){
            if (!User.islogin){
                navigationView.getMenu().setGroupVisible(R.id.account_menu, false);

            }
            else {
                navigationView.getMenu().setGroupVisible(R.id.account_menu, true);
            }
        }
    /*
    #######---------------End of checkLogin---------------#######
    */
        private void detectTextFromImage() throws IOException {
            progressBar.setVisibility(View.VISIBLE);
            FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromFilePath(getApplicationContext(), imageUri);
            FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
            firebaseVisionTextRecognizer.processImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                @Override
                public void onSuccess(FirebaseVisionText firebaseVisionText) {
                    displayTextFromImage(firebaseVisionText);
                    progressBar.setVisibility(View.GONE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Error in reading text", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });

        }
    /*
    #######---------------End of detectTextFromImage---------------#######
    */
        private void displayTextFromImage(FirebaseVisionText firebaseVisionText) {
            try {
                List<FirebaseVisionText.TextBlock> blockList = firebaseVisionText.getTextBlocks();
                if (blockList.size() == 0) {
                    Toast.makeText(getApplicationContext(), "No Text Found", Toast.LENGTH_SHORT).show();
                } else {
                    for (FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks()) {
                        text = text + "\n" + block.getText();

                        textView.setText(text);
                    }
                    history.storeData(text);
                }
            }catch (Exception e){
                Log.e("displayText", e.getMessage());
            }
        }
    /*
    ######---------------End of displayTextFromImage---------------#######
    */

        private void dispatchTakePictureIntent() {
        try {
            final Item[] items = {
                    new Item("Camera", R.drawable.icon_camera),
                    new Item("Gallery", R.drawable.icon_gallerys)
            };
            ListAdapter adapter = new ArrayAdapter<Item>(this, android.R.layout.select_dialog_item,android.R.id.text1,items){
              public View getView(int position, View convertView, ViewGroup parent){
                View v = super.getView(position, convertView, parent);
                TextView textView = v.findViewById(android.R.id.text1);
                  textView.setCompoundDrawablesRelativeWithIntrinsicBounds(items[position].icon,0,0,0);

                  int dp5 =(int) (5 * getResources().getDisplayMetrics().density +0.5f);
                  textView.setCompoundDrawablePadding(dp5);

                  return v;
              }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(TextRecognition.this);
            builder.setTitle("Add Photo");
            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (items[i].text.equals("Camera")) {
                        cameraIntent();

                    } else if (items[i].text.equals( "Gallery")) {
                        galleryIntent();
                    } else {
                        dialogInterface.dismiss();
                    }
                }
            });
            builder.show();
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }




        }
    /*
    #######---------------End of dispatchTakePictureIntent---------------#######
    */
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                if (requestCode == REQUEST_IMAGE_CAPTURE) {
                    /*Bundle extras = data.getExtras();
                    imageBitmap = (Bitmap) extras.get("data");
                    imageView.setImageBitmap(imageBitmap);*/
                    imageView.setImageURI(imageUri);
                }
                else if (requestCode == PICK_IMAGE) {
                    imageUri = data.getData();
                    imageView.setImageURI(imageUri);
                }

            }

        }
    /*
    #######---------------End of onActivityResult---------------#######
    */
        private void  cameraIntent(){
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "New Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
            imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        }
    /*
    #######---------------End of cameraIntent---------------#######
    */
        private void galleryIntent(){
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            try {
                startActivityForResult(galleryIntent, PICK_IMAGE);
            } catch (ActivityNotFoundException e) {
                // display error state to the user
                Toast.makeText(getApplicationContext(), "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    /*
    #######---------------End of galleryIntent()---------------#######
    */
        private void confirmSignout(){
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
            builder.setMessage("Are you sure?").setTitle("Confirm Logout");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    firebaseAuth.signOut();
                    User.islogin = false;
                    checkLogin();
                    Log.d("Logout", "True");
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                    Log.d("Logout", "False");
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();



        }
    /*
    #######---------------End of confirmSignout---------------#######
    */
}