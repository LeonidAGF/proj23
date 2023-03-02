package com.example.chef;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    private String filter = "";
    private DB db = null;
    private ArrayList<Item> items = new ArrayList<Item>();

    private void showItem(Item item){
        Intent intent = new Intent(this,ItemActivity.class);
        intent.putExtra("name" , item.getName());
        intent.putExtra("recipe" , item.getRecipe());
        intent.putExtra("Ingredients" , item.getIngredients());
        intent.putExtra("img" , item.getImg());
        intent.putExtra("time" , item.getTime()+"");
        intent.putExtra("calories" , item.getCalories()+"");
        intent.putExtra("cuisine" , item.getCuisine());
        intent.putExtra("favorites" , item.getFavorites()+"");
        intent.putExtra("id" , item.getId());
        startActivity(intent);
        finish();
    }
    private void getItems(){
        items.clear();
        db.CREATE_TABLE(Static.tableName,"name TEXT, recipe TEXT, time INTEGER, calories INTEGER, Ingredients TEXT, cuisine TEXT, favorites INTEGER, img TEXT, id TEXT");
        Cursor cursor =  db.SELECT(Static.tableName,filter);
        while(cursor.moveToNext()){
            String name = cursor.getString(0);
            String recipe = cursor.getString(1);
            int time = cursor.getInt(2);
            int calories = cursor.getInt(3);
            String Ingredients = cursor.getString(4);
            String Cuisine = cursor.getString(5);
            String favorites = String.valueOf(cursor.getInt(6));
            String img = cursor.getString(7);
            String id = cursor.getString(8);
            items.add(new Item (name,recipe, Ingredients, img,time+"",calories+"",Cuisine,favorites+"",id));
        }
        db.STOP();
    }
    private void updateItems(){
        getItems();
        RecyclerView recyclerView = findViewById(R.id.list);
        ItemAdapter.OnItemClickListener itemClickListener = new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Item item, int position) {
                showItem(item);
            }
        };
        ItemAdapter adapter = new ItemAdapter(this, items, itemClickListener);
        recyclerView.setAdapter(adapter);
    }
    public void onButtonClicked(View view) {
        if(R.id.menu_btn1 == view.getId()){
            filter = "";
            updateItems();
        }
        else if(R.id.menu_btn2 == view.getId()){
            Intent intent = new Intent(this,SearchActivity.class);
            intent.putExtra("type" , 1+"");
            startActivity(intent);
            finish();
        }
        else if(R.id.menu_btn3 == view.getId()){
            Intent intent = new Intent(this,ExchangeActivity.class);
            intent.putExtra("type" , 1+"");
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        db = new DB(this,Static.dbName);
        try {
            Intent intent = getIntent();
            filter = intent.getStringExtra("filter").toString();
        }
        catch (NullPointerException e){}
        updateItems();
    }
}