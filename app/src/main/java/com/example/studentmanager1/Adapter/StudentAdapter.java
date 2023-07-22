package com.example.studentmanager1.Adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.studentmanager1.R;
import com.example.studentmanager1.model.Student;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder>{
    private Context mContext;
    private Resources resources;
    private List<Student> listStudent;
    private AssetManager assetManager;
    private OnClickItem onClickItem;


    public StudentAdapter(AssetManager assetManager,Resources resources,Context mContext) {
        this.resources=resources;
        this.mContext = mContext;
        this.assetManager=assetManager;
    }

    public void setOnClickItem(OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    public void setData(List<Student> list){
        this.listStudent=list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student,parent,false);
         return new StudentViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student=listStudent.get(position);
        if (student == null) {
            return;
        }
        holder.tv_name.setText(student.getNameStudent());


        holder.cv_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItem.onClick(student,holder.getAdapterPosition());
            }
        });
        if (student.getPathImage()!=null){
            if (student.getTypeImage()==Student.TYPE_STORAGE){
                Glide.with(mContext).load(student.getPathImage()).centerCrop().into(holder.img_student);
            } else if (student.getTypeImage()==Student.TYPE_DRAWABLE) {
                int resourceId= resources.getIdentifier(student.getPathImage(), "drawable", mContext.getPackageName());
                holder.img_student.setImageResource(resourceId);
            }else if (student.getTypeImage()==Student.TYPE_ASSETS) {
                try {
                    InputStream inputStream=assetManager.open(student.getPathImage());
                    Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                    holder.img_student.setImageBitmap(bitmap);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return listStudent.size();
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder{
        private CardView cv_student;
        private TextView tv_name;
        private CircleImageView img_student;
        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.tv_name_student);
            cv_student=itemView.findViewById(R.id.item_student);
            img_student=itemView.findViewById(R.id.img_student);
        }
    }
    public interface OnClickItem{
        void onClick(Student student,int position);
    }
}
