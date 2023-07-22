package com.example.studentmanager1.Adapter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.studentmanager1.R;
import com.example.studentmanager1.model.Student;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class AdapterImage extends RecyclerView.Adapter<AdapterImage.ImageViewHolder>{

    private Resources resources;
    private List<String> listImg;
    private OnClickImage onClickImage;
    private int type;
    private AssetManager assetManager;

    private Context context;

    public AdapterImage(int type,Context context,AssetManager assetManager,Resources resources,OnClickImage onClickImage) {
        this.type=type;
        this.onClickImage=onClickImage;
        this.context = context;
        this.resources=resources;
        this.assetManager=assetManager;
    }

    public void setData(List<String> listImg){
        this.listImg=listImg;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_student, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String img=listImg.get(position);
        if (type== Student.TYPE_STORAGE){
            Glide.with(context).load(img).centerCrop().into(holder.imgOfGallery);
        } else if (type==Student.TYPE_DRAWABLE) {
            int resourceId= resources.getIdentifier(img, "drawable", context.getPackageName());
            holder.imgOfGallery.setImageResource(resourceId);
        } else if (type==Student.TYPE_ASSETS) {
            try {
                InputStream inputStream=assetManager.open(img);
                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                holder.imgOfGallery.setImageBitmap(bitmap);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

//        Glide.with(context).load(getFileUri(holder.itemView.getContext(), new File(img)))
//                .centerCrop()
//                .into(holder.imgOfGallery);
        holder.imgOfGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickImage.onClick(img);
            }
        });
    }


    @Override
    public int getItemCount() {
        return listImg.size();
    }
    public static Uri getFileUri(Context context, File file) {
        return FileProvider.getUriForFile(context, context.getPackageName(), file);
    }



    public class ImageViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgOfGallery;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imgOfGallery=itemView.findViewById(R.id.item_img_student);
        }
    }
    public interface OnClickImage{
        void onClick(String img);
    }
}
