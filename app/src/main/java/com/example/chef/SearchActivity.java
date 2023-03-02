package com.example.chef;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity{
    private int favorite = 0;
    private int type = 0;

    public void onButtonClicked(View view) {
        if(R.id.menu_btn12 == view.getId()){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        else if(R.id.menu_btn32 == view.getId()){
            Intent intent = new Intent(this,ExchangeActivity.class);
            intent.putExtra("type" , 2+"");
            startActivity(intent);
            finish();
        }
        else if(R.id.btn == view.getId()){
            EditText textBox = findViewById(R.id.et1);
            String text = textBox.getText().toString();
            String filter = " WHERE (name LIKE '%"+text+"%' OR Ingredients LIKE '%"+text+"%' OR cuisine LIKE '%"+text+"%'";
            Intent intent = new Intent(this,MainActivity.class);
            if(text != ""){
                try{
                    int number = Integer.parseInt(text);
                    filter += " OR time LIKE "+number+" OR calories LIKE "+number;
                }
                catch (Exception e){}
                filter+=") ";
                if(favorite != 0){
                    filter = filter+"AND favorites = "+favorite;
                }
                intent.putExtra("filter" , filter);
            }
            startActivity(intent);
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent;
        if(type == 1){
            intent = new Intent(this,MainActivity.class);
        }
        else{
            intent = new Intent(this,ExchangeActivity.class);
            intent.putExtra("type" , 2+"");
        }
        startActivity(intent);
        finish();
    }
    public void onCheckboxClicked(View view) {
        if(R.id.cbf == view.getId()){
            favorite = 0;
            CheckBox cb = findViewById(R.id.cbf);
            if(cb.isChecked()) {
                favorite = 1;
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent intent = getIntent();
        type = Integer.parseInt(intent.getStringExtra("type").toString());
    }
}