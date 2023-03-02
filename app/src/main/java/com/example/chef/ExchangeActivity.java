package com.example.chef;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class ExchangeActivity extends AppCompatActivity{
    private int type = 0;
    private DB db = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent fileReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, fileReturnedIntent);
        if(requestCode == type){
            if(resultCode == RESULT_OK){
                try {
                    String name,recipe,Ingredients,Cuisine,img;
                    int time,calories,favorites;
                    Uri fileUri = fileReturnedIntent.getData();
                    InputStream iStream = getContentResolver().openInputStream(fileUri);
                    BufferedReader streamReader = new BufferedReader(new InputStreamReader(iStream, "UTF-8"));
                    StringBuilder responseStrBuilder = new StringBuilder();
                    String inputStr;
                    while ((inputStr = streamReader.readLine()) != null)
                        responseStrBuilder.append(inputStr);
                    JSONObject jo = new JSONObject(responseStrBuilder.toString());
                    name = jo.getString("name");
                    recipe = jo.getString("recipe");
                    Ingredients = jo.getString("Ingredients");
                    Cuisine = jo.getString("cuisine");
                    img = jo.getString("img");
                    favorites = jo.getInt("favorites");
                    calories = jo.getInt("calories");
                    time = jo.getInt("time");
                    byte[] bytesIMG = Base64.decode(img, Base64.DEFAULT);
                    FileOutputStream ws = null;
                    UUID code = UUID.randomUUID();
                    String nameIMG = "name"+code.toString();
                    ws = openFileOutput(nameIMG, MODE_PRIVATE);
                    ws.write(bytesIMG);
                    ws.close();
                    db.INSERT(Static.tableName,"'"+name+"','"+recipe+"', "+time+", "+calories+",'"+Ingredients+"','"+Cuisine+"',"+favorites+",'"+nameIMG+"'",true);
                }
                catch (FileNotFoundException e) {}
                catch (IOException e) {} catch (JSONException e) {}
            }
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = null;
        if(type == 1){
            intent = new Intent(this,MainActivity.class);
        }
        else if(type == 2){
            intent = new Intent(this,SearchActivity.class);
            intent.putExtra("type" , 2+"");
        }
        else if(type == 3){
            intent = new Intent(this,EditActivity.class);
            intent.putExtra("type" , 2+"");
        }
        startActivity(intent);
        finish();
    }
    public String getImg(String path){
        FileInputStream fin = null;
        String img = "";
        try {
            fin = openFileInput(path);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            img = Base64.encodeToString(bytes, 0);
        }
        catch(IOException ex) {}
        finally{
            try{
                if(fin!=null)
                    fin.close();
            }
            catch(IOException ex){}
        }
        return img;
    }
    public void saveFile(View view){
        FileOutputStream fos = null;
        String name,recipe,Ingredients,Cuisine,favorites,img;
        int time,calories,id;
        String file_name = "";
        try {
            String folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
            Cursor cursor =  db.SELECT(Static.tableName,"");
            while(cursor.moveToNext()){
                name = cursor.getString(0);
                recipe = cursor.getString(1);
                time = cursor.getInt(2);
                calories = cursor.getInt(3);
                Ingredients = cursor.getString(4);
                Cuisine = cursor.getString(5);
                favorites = String.valueOf(cursor.getInt(6));
                img = cursor.getString(7);
                id = cursor.getInt(8);
                file_name = name+"_"+id+".json";
                String imgS = getImg(img);
                String text = "{\"name\": \""+name+"\",\"recipe\": \""+recipe+"\",\"time\": "+time+",\"calories\": "+calories+",\"Ingredients\": \""+Ingredients+"\",\"cuisine\": \""+Cuisine+"\",\"favorites\": "+favorites+"," + "\n\"img\": \""+imgS+"\"}";
                ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, 1);
                fos = new FileOutputStream(folder+"/"+file_name);
                fos.write(text.getBytes());
            }
            db.STOP();
            Snackbar.make(view, "файлы сохранены", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        catch(IOException ex) {
            Snackbar.make(view, "ошибка", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        finally{
            try{
                if(fos!=null)
                    fos.close();
            }
            catch(IOException ex){
            }
        }
    }
    public void onButtonClicked(View view) {
        if(R.id.menu_btn13 == view.getId()){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        else if(R.id.menu_btn23 == view.getId()){
            Intent intent = new Intent(this,SearchActivity.class);
            intent.putExtra("type" , 2+"");
            startActivity(intent);
            finish();
        }
        else if(R.id.export_btn == view.getId()){
            saveFile(view);
        }
        else if(R.id.import1_btn == view.getId()){
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent,type);
        }
        else if(R.id.import2_btn == view.getId()){
            Intent intent = new Intent(this,EditActivity.class);
            intent.putExtra("type" , 2+"");
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        db = new DB(this,Static.dbName);
        Intent intent = getIntent();
        type = Integer.parseInt(intent.getStringExtra("type").toString());
    }

}
