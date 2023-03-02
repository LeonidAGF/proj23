package com.example.chef;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class EditActivity extends AppCompatActivity{
    private Item item = null;
    private int Pick_image = 1;
    private EditText etName = null;
    private EditText etRecipe = null;
    private EditText etTime = null;
    private EditText etCalories = null;
    private EditText etIngredients = null;
    private EditText etCuisine = null;
    private ImageView iv = null;
    private int type = 0;
    private DB db = null;

    private void back(){
        Intent intent = null;
        if(type == 1){
            intent = new Intent(this,ItemActivity.class);
            intent.putExtra("name" , item.getName());
            intent.putExtra("recipe" , item.getRecipe());
            intent.putExtra("Ingredients" , item.getIngredients());
            intent.putExtra("img" , item.getImg());
            intent.putExtra("time" , item.getTime()+"");
            intent.putExtra("calories" , item.getCalories()+"");
            intent.putExtra("cuisine" , item.getCuisine());
            intent.putExtra("favorites" , item.getFavorites()+"");
            intent.putExtra("id" , item.getId());
        }
        else if (type == 2){
            intent = new Intent(this,ExchangeActivity.class);
            intent.putExtra("type" , 3+"");
        }
        startActivity(intent);
        finish();
    }
    private void request(){
        if(type == 1){
            db.UPDATE(Static.tableName,"name = '"+item.getName()+"', recipe = '"+item.getRecipe()+"', time= "+item.getTime()+", calories= "+item.getCalories()+", Ingredients= '"+item.getIngredients()+"', Cuisine= '"+item.getCuisine()+"', favorites= "+item.getFavorites()+", img = '"+item.getImg()+"' WHERE id = '"+item.getId()+"'");
        }
        else if(type == 2){
            if(item.getName() != ""){
                db.INSERT(Static.tableName,"'"+item.getName()+"','"+item.getRecipe()+"', "+item.getTime()+", "+item.getCalories()+",'"+item.getIngredients()+"','"+item.getCuisine()+"',"+item.getFavorites()+",'"+item.getImg()+"'",true);
            }
        }
    }
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
    public void onButtonClicked(View view){
        if(R.id.btnBackToItem == view.getId()){
            back();
        }
        else if(R.id.btnEditItem == view.getId()){
            if(String.valueOf(etName.getText()).isEmpty()){
                Snackbar.make(view, "Напиши название рецепта!!!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                return;
            }
            try {
                item.setName(String.valueOf(etName.getText()));
                item.setRecipe(String.valueOf(etRecipe.getText()));
                item.setIngredients(String.valueOf(etIngredients.getText()));
                item.setCuisine(String.valueOf(etCuisine.getText()));
                if(String.valueOf(etTime.getText()).isEmpty()) item.setTime("0");
                else item.setTime(String.valueOf(etTime.getText()));
                if(String.valueOf(etCalories.getText()).isEmpty()) item.setCalories("0");
                else item.setCalories(String.valueOf(etCalories.getText()));
                request();
                back();
            }
            catch (NumberFormatException e) {}
        }
        else if(R.id.btnSelectIMG == view.getId() || R.id.simgItem == view.getId()){
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, Pick_image);
        }
    }
    @Override
    public void onBackPressed() {
        back();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if(requestCode == Pick_image){
            if(resultCode == RESULT_OK){
                try {
                    Uri imageUri = imageReturnedIntent.getData();
                    InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    iv.setImageBitmap(selectedImage);
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.PNG,100,os);
                    byte[] img = os.toByteArray();
                    os.close();
                    FileOutputStream ws = null;
                    UUID code = UUID.randomUUID();
                    String nameIMG = "name"+code.toString();
                    ws = openFileOutput(nameIMG, MODE_PRIVATE);
                    ws.write(img);
                    ws.close();
                    item.setImg(nameIMG);
                }
                catch (FileNotFoundException e) {}
                catch (IOException e) {}
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        db = new DB(this,Static.dbName);
        Intent intent = getIntent();
        iv = findViewById(R.id.simgItem);
        etName = findViewById(R.id.enameItemEdit);
        etRecipe = findViewById(R.id.erecipeItemEdit);
        etTime = findViewById(R.id.etimeItemEdit);
        etCalories = findViewById(R.id.ecaloriesItemEdit);
        etIngredients = findViewById(R.id.eIngredientsItemEdit);
        etCuisine = findViewById(R.id.ecuisineItemEdit);
        type = Integer.parseInt(intent.getStringExtra("type").toString());
        item = new Item("","","","","0","0","","0","");
        if(type == 1){
            String name = intent.getStringExtra("name").toString();
            String recipe = intent.getStringExtra("recipe").toString();
            String Ingredients = intent.getStringExtra("Ingredients").toString();
            String img = intent.getStringExtra("img");
            String time = intent.getStringExtra("time").toString();
            String calories = intent.getStringExtra("calories").toString();
            String cuisine = intent.getStringExtra("cuisine").toString();
            String favorites = intent.getStringExtra("favorites").toString();
            String id = intent.getStringExtra("id").toString();
            item = new Item(name,recipe,Ingredients,img,time,calories,cuisine,favorites,id);
            if(!item.getImg().isEmpty())
                iv.setImageBitmap(getImg(item.getImg()));
            etName.setText(item.getName());
            etRecipe.setText(item.getRecipe());
            etTime.setText(item.getTime()+"");
            etCalories.setText(item.getCalories()+"");
            etIngredients.setText(item.getIngredients());
            etCuisine.setText(item.getCuisine());
        }
    }
}
