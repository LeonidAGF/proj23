package com.example.chef;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Item {
    private String name = ""; // название
    private String Ingredients = "";  //ингредиенты
    private String img = ""; // название картинки
    private String time = "0" ;//время приготовления
    private String calories= "0";//калории блюда
    private String cuisine = "";//кухня
    private String recipe = "";//рецепт
    private String favorites = "0";//избр ли элимент
    private String id = "";//id item
    public Item(String name, String recipe, String Ingredients, String img,String time,String calories,String cuisine,String favorites,String id){
        this.name=name;
        this.recipe =recipe;
        this.Ingredients=Ingredients;
        this.img=img;
        this.time = time;
        this.calories = calories;
        this.cuisine = cuisine;
        this.favorites = favorites;
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getIngredients() {
        return this.Ingredients;
    }
    public String getSIngredients() {
        return this.Ingredients.substring(0, 50)+"...";
    }

    public void setIngredients(String capital) {
        this.Ingredients = capital;
    }
    public String getImg() {
        return this.img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTime(){return this.time;}

    public void setTime(String time){this.time = time;}

    public String getCalories(){return this.calories;}

    public void setCalories(String calories){this.calories = calories;}

    public String getCuisine() {
        return this.cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getRecipe() {
        return this.recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getFavorites(){return this.favorites;}

    public void setFavorites(String favorites){this.favorites = favorites;}

    public String getId(){return this.id;}

    public void setId(String id){this.id = id;}

}
