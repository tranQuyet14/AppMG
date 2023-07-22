package com.example.studentmanager1.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentmanager1.Adapter.StudentAdapter;
import com.example.studentmanager1.R;
import com.example.studentmanager1.database.ManageDatabase;
import com.example.studentmanager1.model.Class;
import com.example.studentmanager1.model.Student;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FragmentStudent extends Fragment {
    public static final String TAG = FragmentStudent.class.getName();
    private Toolbar toolbar;
    private List<Student> listStudent=new ArrayList<>();
    private FloatingActionButton btnAddStudent;
    private RecyclerView rcvStudent;
    private View rootView;
    private Class mClass;
    private StudentAdapter studentAdapter;
    private ImageView icon_back_to_list_class;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_student, container, false);
        initview();
        loadData();
        return rootView;
    }

    public void loadData() {

        listStudent = ManageDatabase.getInstance(this.getContext()).dbDAO().getListStudent1(mClass.getNameClass());
        studentAdapter.setData(listStudent);

    }

    private void initview() {
        btnAddStudent = rootView.findViewById(R.id.btn_add_student);
        icon_back_to_list_class = rootView.findViewById(R.id.icon_arrow_back_to_list_class);
        rcvStudent = rootView.findViewById(R.id.rcv_student);
        toolbar = rootView.findViewById(R.id.toolbar_student);
        studentAdapter = new StudentAdapter(requireActivity().getAssets(),getResources(),this.getContext());
        studentAdapter.setOnClickItem(new StudentAdapter.OnClickItem() {
            @Override
            public void onClick(Student student, int position) {
                FragmentDetailStudent fragmentDetailStudent = new FragmentDetailStudent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("object_student", student);
                fragmentDetailStudent.setArguments(bundle);
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.ln_main, fragmentDetailStudent)
                        .addToBackStack(FragmentStudent.TAG)
                        .commit();
            }
        });

        icon_back_to_list_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });
        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogAddStudent();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false);
        rcvStudent.setLayoutManager(linearLayoutManager);
        rcvStudent.setAdapter(studentAdapter);
        mClass = (Class) getArguments().get("object_class");
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
        rcvStudent.addItemDecoration(itemDecoration);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Student student = listStudent.get(position);

                new AlertDialog.Builder(getContext())
                        .setTitle("Confirm delete student")
                        .setMessage("Are you sure")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ManageDatabase.getInstance(getContext()).dbDAO().deleteStudent(student);
                                listStudent.remove(position);
                                loadData();
                                Toast.makeText(getContext(), "Delete student succeed", Toast.LENGTH_SHORT).show();
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
        itemTouchHelper.attachToRecyclerView(rcvStudent);
    }

    public void openDialogAddStudent() {
        Dialog dialog = new Dialog(this.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_addstudent);

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


        EditText edtAddNameStudent = dialog.findViewById(R.id.et_add_name_student);
        EditText edtAddCodeStudent = dialog.findViewById(R.id.ed_add_code_student);

        Button btnCancelAdd = dialog.findViewById(R.id.btn_cancel_add_student);
        Button btnSubmitAdd = dialog.findViewById(R.id.btn_submit_add_student);

        btnCancelAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSubmitAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strNameStudent = edtAddNameStudent.getText().toString().trim();
                String strCodeStudent = edtAddCodeStudent.getText().toString().trim();
                if (strNameStudent.equals("") || strCodeStudent.equals("")) {
                    Toast.makeText(getContext(), "Please enter full information", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isStudentExists(strCodeStudent)) {
                    Toast.makeText(getContext(), "Student Code is exists", Toast.LENGTH_SHORT).show();
                    return;
                }
                Student newStudent = new Student(strCodeStudent, strNameStudent, mClass.getNameClass());
                ManageDatabase.getInstance(getContext()).dbDAO().insertStudent(newStudent);
                Toast.makeText(getContext(), "Add student succeed", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                loadData();
            }


        });
    }

    private boolean isStudentExists(String strCodeStudent) {
        List<Student> list = ManageDatabase.getInstance(getContext()).dbDAO().checkExistCodeStudent(strCodeStudent);
        return !list.isEmpty();
    }
}
