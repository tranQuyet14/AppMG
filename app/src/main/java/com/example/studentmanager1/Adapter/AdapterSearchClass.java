package com.example.studentmanager1.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentmanager1.R;
import com.example.studentmanager1.model.Class;

import java.util.ArrayList;
import java.util.List;

public class AdapterSearchClass extends RecyclerView.Adapter<AdapterSearchClass.SearchClassViewHolder> implements Filterable {
    private OnClickItem onClickItem;
    private List<Class> mlistClass;
    private List<Class> mlistClassOld;
    public void setData(List<Class> mlistClass){
        this.mlistClassOld=mlistClass;
        this.mlistClass=mlistClass;
        notifyDataSetChanged();
    }

    public AdapterSearchClass(OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    @NonNull
    @Override
    public SearchClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class,parent,false);
        return new SearchClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchClassViewHolder holder, int position) {
        Class mclass=mlistClass.get(position);
        if (mclass==null){
            return;
        }
        holder.tvNameClass.setText(mclass.getNameClass());
        holder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItem.onClick(mclass);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlistClass.size();
    }

    public class SearchClassViewHolder extends RecyclerView.ViewHolder{

        private TextView tvNameClass;
        private CardView cvItem;
        public SearchClassViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameClass=itemView.findViewById(R.id.tv_name_class);
            cvItem=itemView.findViewById(R.id.item_class);
        }
    }
    public interface OnClickItem {
        void onClick(Class mClass);
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String search= constraint.toString();
                if (search.isEmpty()){
                    mlistClass=mlistClassOld;
                }else {
                    List<Class> list=new ArrayList<>();
                    for (Class mclass:mlistClassOld){
                        if (mclass.getNameClass().toLowerCase().contains(search.toLowerCase())){
                            list.add(mclass);
                        }
                    }
                    mlistClass=list;
                }

                FilterResults filterResults=new FilterResults();
                filterResults.values=mlistClass;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mlistClass= (List<Class>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
