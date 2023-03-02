package com.example.chef;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{
    interface OnItemClickListener{
        void onItemClick(Item state, int position);
    }
    private final OnItemClickListener onClickListener;
    private final LayoutInflater inflater;
    private final List<Item> items;
    private Context context;

    ItemAdapter(Context context, List<Item> items, OnItemClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.items = items;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }
    public Bitmap getImg(String path){
        FileInputStream fin = null;
        Bitmap selectedImage = null;
        try {
            fin = context.openFileInput(path);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            selectedImage =BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
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
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = items.get(position);
        if(!item.getImg().isEmpty())
            holder.imgView.setImageBitmap(getImg(item.getImg()));
        holder.nameView.setText(item.getName());
        holder.ingredientsView.setText("Ингредиенты: "+item.getSIngredients());

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                onClickListener.onItemClick(item, position);
            }
        });
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView;
        TextView nameView, ingredientsView;
        ViewHolder(View view){
            super(view);
            imgView = view.findViewById(R.id.imgl);
            nameView = view.findViewById(R.id.name);
            ingredientsView = view.findViewById(R.id.ingredients);
        }
    }
}