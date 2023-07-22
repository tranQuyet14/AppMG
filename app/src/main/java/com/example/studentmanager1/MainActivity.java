package com.example.studentmanager1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.studentmanager1.Adapter.ClassAdapter;
import com.example.studentmanager1.database.ManageDatabase;
import com.example.studentmanager1.fragment.FragmentSearch;
import com.example.studentmanager1.fragment.FragmentStudent;
import com.example.studentmanager1.model.Class;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getName();
    private static final int MY_REQUEST_CODE_PERMISSION = 10;
    private RecyclerView rcvClass;
    private FloatingActionButton btn_add;
    private ClassAdapter classAdapter;
    public static boolean FLAG_ACCEPT_PERMISSION=true;
    private ImageView img_search;
    private List<Class> listClass = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        initView();
        loadData();
    }

    public void initView() {

        img_search=findViewById(R.id.ic_search);
        btn_add = findViewById(R.id.btn_add_class);
        rcvClass = findViewById(R.id.rcv_class);
        classAdapter = new ClassAdapter(this, new ClassAdapter.OnClickItem() {
            @Override
            public void onClick(Class mClass, int position) {
                FragmentStudent fragmentStudent = new FragmentStudent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("object_class", mClass);
                fragmentStudent.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.ln_main, fragmentStudent)
                        .addToBackStack(MainActivity.TAG)
                        .commit();
            }
        });
        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSearch fragmentSearch=new FragmentSearch();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.ln_main,fragmentSearch)
                        .addToBackStack(MainActivity.TAG)
                        .commit();
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogAddClass(Gravity.CENTER);
            }
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvClass.setLayoutManager(linearLayoutManager);
        rcvClass.setAdapter(classAdapter);


        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvClass.addItemDecoration(itemDecoration);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Confirm delete class")
                        .setMessage("Are you sure")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int position = viewHolder.getAdapterPosition();
                                Class mclass = listClass.get(position);
                                listClass.remove(position);
                                ManageDatabase.getInstance(MainActivity.this).dbDAO().deleteClass(mclass);
                                Toast.makeText(MainActivity.this,"Delete Class succeed",Toast.LENGTH_SHORT).show();
                                loadData();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                loadData();
                            }
                        })
                        .show();
            }
        });
        itemTouchHelper.attachToRecyclerView(rcvClass);
    }



    public void loadData() {
        listClass = ManageDatabase.getInstance(this).dbDAO().getListClass();
        classAdapter.setData(listClass);
    }

    public void openDialogAddClass(int gravity) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_addclass);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(false);
        dialog.show();


        EditText edtAddNameClass = dialog.findViewById(R.id.et_add_name_class);
        EditText edtAddCodeClass = dialog.findViewById(R.id.ed_add_code_class);

        Button btnCancelAdd = dialog.findViewById(R.id.btn_cancel_add);
        Button btnSubmitAdd = dialog.findViewById(R.id.btn_submit_add);

        btnCancelAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSubmitAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strNameClass = edtAddNameClass.getText().toString().trim();
                String strCodeClass = edtAddCodeClass.getText().toString().trim();
                if (strNameClass.equals("") || strCodeClass.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter full information", Toast.LENGTH_SHORT).show();
                } else {
                    Class mClass = new Class(strCodeClass, strNameClass);
                    if (isClassExists(mClass)) {
                        Toast.makeText(MainActivity.this, "User Exists", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ManageDatabase.getInstance(MainActivity.this).dbDAO().insertClass(mClass);
                    loadData();
                    dialog.dismiss();
                }
            }
        });
    }

    public boolean isClassExists(Class mClass) {
        List<Class> class1 = ManageDatabase.getInstance(this).dbDAO().checkExistsNameClass(mClass.getNameClass());
        List<Class> class2 = ManageDatabase.getInstance(this).dbDAO().checkExistsCodeClass(mClass.getCodeClass());
        return !class1.isEmpty() || !class2.isEmpty();
    }
    private void requestPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES)==PackageManager.PERMISSION_GRANTED) {
                FLAG_ACCEPT_PERMISSION=true;
                Toast.makeText(this,"Permission granted",Toast.LENGTH_SHORT).show();
            }else{
                String[] permisstion={Manifest.permission.READ_MEDIA_IMAGES};
                requestPermissions(permisstion,MY_REQUEST_CODE_PERMISSION);
            }
        }else {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                FLAG_ACCEPT_PERMISSION=true;
                Toast.makeText(this,"Permission granted",Toast.LENGTH_SHORT).show();
            }else{
                String[] permisstion={Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permisstion,MY_REQUEST_CODE_PERMISSION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==MY_REQUEST_CODE_PERMISSION){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
                FLAG_ACCEPT_PERMISSION=true;
            }else {
                Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }
}