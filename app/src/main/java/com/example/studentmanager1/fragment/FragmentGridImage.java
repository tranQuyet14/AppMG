package com.example.studentmanager1.fragment;

import android.content.ContentResolver;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.loader.AssetsProvider;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentmanager1.Adapter.AdapterImage;
import com.example.studentmanager1.R;
import com.example.studentmanager1.database.ManageDatabase;
import com.example.studentmanager1.model.Student;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FragmentGridImage extends Fragment {
    private View rootView;
    private Student student;
    private AdapterImage adapterImage;
    private RecyclerView recyclerView;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_list_image, container, false);
        initView();
        return rootView;
    }

    private void initView() {
        student = (Student) getArguments().get("object_student");
        int checkType= (int) getArguments().get("type");

        recyclerView=rootView.findViewById(R.id.rcv_list_img);
        recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(),3));
        adapterImage=new AdapterImage(checkType,this.getContext(),requireActivity().getAssets(),getResources(), new AdapterImage.OnClickImage() {
            @Override
            public void onClick(String img) {
                student.setPathImage(img);
                student.setTypeImage(checkType);
                ManageDatabase.getInstance(getContext()).dbDAO().updateStudent(student);
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });
        if (checkType==Student.TYPE_STORAGE){
            adapterImage.setData(getListImage());
        } else if (checkType==Student.TYPE_DRAWABLE) {
            adapterImage.setData(getImageFromDrawable());
        } else if (checkType==Student.TYPE_ASSETS) {
            adapterImage.setData(listNameAssets());
        }
        recyclerView.setAdapter(adapterImage);

    }

    public ArrayList<String> getListImage(){
        Uri uri;
        Cursor cursor;
        int column_index_data;
        ArrayList<String> listImage=new ArrayList<>();
        String absolutePathOfImgae;
        uri= MediaStore.Images.Media.EXTERNAL_CONTENT_URI;


        String[] projection={MediaStore.MediaColumns.DATA,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        String orderBy=MediaStore.Video.Media.DATE_TAKEN;
        cursor=requireContext().getContentResolver().query(uri,projection,null,null,orderBy+" DESC");

        column_index_data=cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        while (cursor.moveToNext()){
            absolutePathOfImgae=cursor.getString(column_index_data);
            File fIle = new File(absolutePathOfImgae);
            listImage.add(fIle.getPath());
        }
        cursor.close();
        return listImage;
    }

    private List<String> getImageFromDrawable(){
        List<String> listPath=new ArrayList<>();
//        int[] resourceId={R.drawable.img,R.drawable.img_1,R.drawable.img_2,
//                R.drawable.img_3,R.drawable.img_4,R.drawable.img_5,
//                R.drawable.img_6,R.drawable.img_7,R.drawable.img_8,
//                R.drawable.img_9,R.drawable.img_10,R.drawable.img_11,
//                R.drawable.img_13,R.drawable.img_14};
        listPath.add("img");
        listPath.add("img_1");
        listPath.add("img_2");
        listPath.add("img_3");
        listPath.add("img_4");
        listPath.add("img_5");
        listPath.add("img_6");
        listPath.add("img_7");
        listPath.add("img_8");
        listPath.add("img_9");
        listPath.add("img_10");
        listPath.add("img_11");
        listPath.add("img_12");
        listPath.add("img_13");
        listPath.add("img_14");
        return listPath;
    }

    private List<String> listNameAssets(){
        List<String> list =new ArrayList<>();
        list.add("img.png");
        list.add("img_1.png");
        list.add("img_2.png");
        list.add("img_3.png");
        list.add("img_4.png");
        list.add("img_5.png");
        list.add("img_6.png");
        list.add("img_7.png");
        return list;
    }
}
