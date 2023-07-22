package com.example.studentmanager1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentmanager1.R;
import com.example.studentmanager1.model.Class;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {
    private List<Class> listClass;
    private Context mContext;
    private OnClickItem onClickItem;

    public ClassAdapter(Context mContext, OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
        this.mContext = mContext;
    }

    public void setData(List<Class> listClass) {
        this.listClass = listClass;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class, parent, false);
        return new ClassViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        Class mClass = listClass.get(position);
        if (mContext == null) {
            return;
        }

        holder.tv_nameClass.setText(mClass.getNameClass());
        holder.cv_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItem.onClick(mClass, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listClass.size();
    }

    public class ClassViewHolder extends RecyclerView.ViewHolder {
        private CardView cv_item;
        private TextView tv_nameClass;

        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nameClass = itemView.findViewById(R.id.tv_name_class);
            cv_item = itemView.findViewById(R.id.item_class);
        }
    }

    public interface OnClickItem {
        void onClick(Class mClass, int position);
    }
}
