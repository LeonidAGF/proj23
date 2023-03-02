package com.example.chef;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class ItemActivity extends AppCompatActivity {
    private Item item = null;
    private DB db = null;
    private CheckBox checkBoxFavorites = null;

    public Bitmap getImg(String path){
        FileInputStream fin = null;
        Bitmap selectedImage = null;
        try {
            fin = openFileInput(path);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            selectedImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        catch(IOException ex) {}
        finally{
            try{
                if(fin!=null)
                    fin.close();
            }
            catch(IOException ex){}
        }
        return selectedImage;
    }
    public void onCheckboxClicked(View view) {
        if(R.id.cb1 == view.getId()){
            int i = 0;
            if(checkBoxFavorites.isChecked()) {
                i = 1;
            }
            item.setFavorites(i+"");
            db.UPDATE(Static.tableName,"favorites= "+item.getFavorites()+" WHERE id = '"+item.getId()+"'");
        }
    }
    private void OpenStartWindow(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void onButtonClicked(View view){
        if(R.id.btnDelete == view.getId()){
            db.DELETE(Static.tableName,"id = '"+item.getId()+"'");
            OpenStartWindow();
        }
        else if(R.id.btnBack == view.getId()){
            OpenStartWindow();
        }
        else if(R.id.btnEdit == view.getId()){
            Intent intent = new Intent(this,EditActivity.class);
            intent.putExtra("type" , 1+"");
            intent.putExtra("name" , item.getName());
            intent.putExtra("recipe" , item.getRecipe());
            intent.putExtra("Ingredients" , item.getIngredients());
            intent.putExtra("img" , item.getImg()+"");
            intent.putExtra("time" , item.getTime()+"");
            intent.putExtra("calories" , item.getCalories()+"");
            intent.putExtra("cuisine" , item.getCuisine());
            intent.putExtra("favorites" , item.getFavorites()+"");
            intent.putExtra("id",item.getId());
            startActivity(intent);
            finish();
        }
    }
    @Override
    public void onBackPressed() {OpenStartWindow();}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        db = new DB(this,Static.dbName);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name").toString();
        String recipe = intent.getStringExtra("recipe").toString();
        String Ingredients = intent.getStringExtra("Ingredients").toString();
        String img = intent.getStringExtra("img").toString();
        String time = intent.getStringExtra("time").toString();
        String calories = intent.getStringExtra("calories").toString();
        String cuisine = intent.getStringExtra("cuisine").toString();
        int favorites = Integer.parseInt(intent.getStringExtra("favorites").toString());
        String id = intent.getStringExtra("id").toString();
        item = new Item(name,recipe,Ingredients,img,time,calories,cuisine,favorites+"",id);
        ImageView imageview = findViewById(R.id.img);
        if(!item.getImg().isEmpty())
            imageview.setImageBitmap(getImg(item.getImg()));
        TextView tv = findViewById(R.id.textItem);
        tv.setText("Название: "+name+"\n"+"Рецепт: "+ recipe+ "\n"+"Ингредиенты: "+ Ingredients + "\n"+"Время приготавления: "+ time+ " минут\n"+"Каллории: "+calories+" каллорий\n"+"Кухня: "+cuisine+"\n");
        checkBoxFavorites = findViewById(R.id.cb1);
        checkBoxFavorites.setChecked(favorites==1);
    }
}

