package com.example.studentmanager1.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.studentmanager1.MainActivity;
import com.example.studentmanager1.R;
import com.example.studentmanager1.database.ManageDatabase;
import com.example.studentmanager1.model.Class;
import com.example.studentmanager1.model.Student;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentDetailStudent extends Fragment {
    public static final String TAG = FragmentDetailStudent.class.getName();
    private View rootView;
    private CardView cv_detail;
    private LinearLayout ln_edit;
    private TextView tvName, tvCodeStudent, tvCodeClass;
    private EditText et_name, et_codeStudent, et_codeClass;
    private ImageView ic_arrow_back;
    private CircleImageView imgdetail1,imgdetail2;

    private Button btnEdit, btnSubmit, btnChangeImage;
    private Student student;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.detail_student, container, false);
        initView();
        return rootView;
    }

    private void initView() {
        imgdetail1=rootView.findViewById(R.id.img_student_detail);
        imgdetail2=rootView.findViewById(R.id.img_student_edit_detail);
        btnChangeImage = rootView.findViewById(R.id.btn_change);
        tvName = rootView.findViewById(R.id.tv_name_student_detail);
        tvCodeStudent = rootView.findViewById(R.id.tv_code_student);
        tvCodeClass = rootView.findViewById(R.id.tv_code_class);
        ic_arrow_back = rootView.findViewById(R.id.icon_arrow_back_to_list_student);
        et_name = rootView.findViewById(R.id.et_name);
        et_codeClass = rootView.findViewById(R.id.et_code_class);
        et_codeStudent = rootView.findViewById(R.id.et_code_student);
        btnEdit = rootView.findViewById(R.id.btn_edit);
        btnSubmit = rootView.findViewById(R.id.btn_save);
        cv_detail = rootView.findViewById(R.id.cv_detail);
        ln_edit = rootView.findViewById(R.id.ln_edit);
        student = (Student) getArguments().get("object_student");
        tvName.setText(student.getNameStudent());
        tvCodeClass.setText(student.getNameClass());
        tvCodeStudent.setText(student.getCodeStudent());

        if (student.getPathImage()!=null){

            if (student.getTypeImage()==Student.TYPE_STORAGE){
                Glide.with(getContext()).load(student.getPathImage()).centerCrop().into(imgdetail1);
                Glide.with(getContext()).load(student.getPathImage()).centerCrop().into(imgdetail2);
            } else if (student.getTypeImage()==Student.TYPE_DRAWABLE) {
                int resourceId= getResources().getIdentifier(student.getPathImage(), "drawable",getContext().getPackageName());
                imgdetail2.setImageResource(resourceId);
                imgdetail1.setImageResource(resourceId);
            }else if (student.getTypeImage()==Student.TYPE_ASSETS) {
                try {
                    InputStream inputStream=requireActivity().getAssets().open(student.getPathImage());
                    Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                    imgdetail1.setImageBitmap(bitmap);
                    imgdetail2.setImageBitmap(bitmap);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
        et_name.setText(student.getNameStudent());
        et_codeStudent.setText(student.getCodeStudent());
        et_codeClass.setText(student.getNameClass());


        ic_arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getParentFragmentManager() != null) {
                    getParentFragmentManager().popBackStack();
                }
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cv_detail.setVisibility(View.GONE);
                ln_edit.setVisibility(View.VISIBLE);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strName = et_name.getText().toString().trim();
                String strCodeStudent = et_codeStudent.getText().toString().trim();
                String strNameClass = et_codeClass.getText().toString().trim();
                if (strNameClass.equals("") || strName.equals("") || strCodeStudent.equals("")) {
                    Toast.makeText(getContext(), "Please enter full information", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isStudentExists(strCodeStudent, strNameClass)) {
                    Toast.makeText(getContext(), "Code student exists or class not exists ", Toast.LENGTH_SHORT).show();
                    return;
                }

                tvName.setText(strName);
                tvCodeStudent.setText(strCodeStudent);
                tvCodeClass.setText(strNameClass);
                student.setNameStudent(strName);
                student.setCodeStudent(strCodeStudent);
                student.setNameClass(strNameClass);
                Toast.makeText(getContext(), "Succeed", Toast.LENGTH_SHORT).show();
                ManageDatabase.getInstance(getContext()).dbDAO().updateStudent(student);
                cv_detail.setVisibility(View.VISIBLE);
                ln_edit.setVisibility(View.GONE);
            }
        });

        btnChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.FLAG_ACCEPT_PERMISSION) {
                    openBottomSheetChooseFolder();
                } else {
                    openSetting();
                }
            }
        });
    }

    private void openBottomSheetChooseFolder() {
        RelativeLayout chooseStorage,chooseDrawable,chooseAssets;
        View viewDialog= getLayoutInflater().inflate(R.layout.layout_bottom_sheet,null);
        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(this.getContext());
        bottomSheetDialog.setContentView(viewDialog);
        bottomSheetDialog.show();

        chooseStorage=viewDialog.findViewById(R.id.choose_storage);
        chooseDrawable=viewDialog.findViewById(R.id.choose_drawable);
        chooseAssets=viewDialog.findViewById(R.id.choose_assets);
        FragmentGridImage fragmentGridImage=new FragmentGridImage();


        Bundle bundle = new Bundle();
        bundle.putSerializable("object_student", student);
        fragmentGridImage.setArguments(bundle);
        chooseStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putSerializable("type",Student.TYPE_STORAGE);
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.ln_main, fragmentGridImage)
                        .addToBackStack(FragmentDetailStudent.TAG)
                        .commit();
                bottomSheetDialog.dismiss();
            }
        });
        chooseDrawable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putSerializable("type",Student.TYPE_DRAWABLE);
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.ln_main,fragmentGridImage)
                        .addToBackStack(FragmentDetailStudent.TAG)
                        .commit();
                bottomSheetDialog.dismiss();
            }
        });
        chooseAssets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putSerializable("type",Student.TYPE_ASSETS);
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.ln_main,fragmentGridImage)
                        .addToBackStack(FragmentDetailStudent.TAG)
                        .commit();
                bottomSheetDialog.dismiss();
            }
        });
    }

    private void openSetting() {
        new AlertDialog.Builder(this.getContext())
                .setTitle("Open setting")
                .setMessage("You don't accept the allow, so can not use this service")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private boolean isStudentExists(String strCodeStudent, String strNameClass) {
        List<Student> list = ManageDatabase.getInstance(getContext()).dbDAO().checkExistCodeStudent(strCodeStudent);
        List<Class> classes = ManageDatabase.getInstance(getContext()).dbDAO().checkExistsNameClass(strNameClass);
        return !list.isEmpty() || classes.isEmpty();
    }


}
