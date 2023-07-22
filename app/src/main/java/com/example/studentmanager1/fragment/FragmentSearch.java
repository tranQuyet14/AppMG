package com.example.studentmanager1.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentmanager1.Adapter.AdapterSearchClass;
import com.example.studentmanager1.MainActivity;
import com.example.studentmanager1.R;
import com.example.studentmanager1.database.ManageDatabase;
import com.example.studentmanager1.model.Class;

import java.util.ArrayList;
import java.util.List;

public class FragmentSearch extends Fragment {
    private List<Class> mlistClass=new ArrayList<>();
    private AdapterSearchClass adapterSearchClass;
    private EditText edtSearch;
    private View rootView;
    private RecyclerView rcvSearched;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        unitView();
        loadData();
        return rootView;
    }

    private void unitView() {
        rcvSearched=rootView.findViewById(R.id.rcv_search);
        edtSearch=rootView.findViewById(R.id.edt_search);
        adapterSearchClass=new AdapterSearchClass(new AdapterSearchClass.OnClickItem() {
            @Override
            public void onClick(Class mClass) {
                FragmentStudent fragmentStudent = new FragmentStudent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("object_class", mClass);
                fragmentStudent.setArguments(bundle);
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.ln_main, fragmentStudent)
                        .addToBackStack(MainActivity.TAG)
                        .commit();
            }
        });
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL,false);
        rcvSearched.setLayoutManager(linearLayoutManager);
        rcvSearched.setAdapter(adapterSearchClass);
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_SEARCH){
                    hideSoftKeyBoard();
                }
                return false;
            }
        });
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapterSearchClass.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void loadData(){
        mlistClass = ManageDatabase.getInstance(this.getContext()).dbDAO().getListClass();
        adapterSearchClass.setData(mlistClass);
    }
    private void hideSoftKeyBoard() {
        try {
            InputMethodManager inputMethodManager= (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(edtSearch.getWindowToken(),0);
        }catch (NullPointerException exception){
            exception.printStackTrace();
        }
    }

}
