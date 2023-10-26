package com.example.assignment2.View;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.assignment2.Controller.DatabaseHelper;
import com.example.assignment2.MainActivity;
import com.example.assignment2.Model.ProfileDAO;
import com.example.assignment2.R;


import java.sql.Timestamp;

public class InsertProfile extends DialogFragment {
    private EditText edt_name, edt_surname, edt_gpa, edt_id;
    private Button cancelButton, saveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_insert_profile_dialog, container, false);

        edt_name = view.findViewById(R.id.txt_edit_name);
        edt_surname = view.findViewById(R.id.txt_edit_surname);
        edt_gpa = view.findViewById(R.id.txt_edit_gpa);
        edt_id = view.findViewById(R.id.txt_edit_id);
        cancelButton = view.findViewById(R.id.btn_cancel);
        saveButton = view.findViewById(R.id.btn_save);

        cancelButton.setOnClickListener((v) -> dismiss());

        saveButton.setOnClickListener(v -> {
            String name = edt_name.getText().toString();
            String surname = edt_surname.getText().toString();
            double gpa = Double.parseDouble(edt_gpa.getText().toString());
            long id = Long.parseLong(edt_id.getText().toString());
            Timestamp date = new Timestamp(System.currentTimeMillis());
            if (!(name.isEmpty() || surname.isEmpty())) {

                ProfileDAO profile = new ProfileDAO(id, name, surname, gpa, date.toString());
                DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
                ProfileDAO testProfile = dbHelper.addProfile(profile);
                if(testProfile.getId() == -1) {
                    Log.e("InsertProfile", "Error Profile not inserted.");
                }

                ((MainActivity) getActivity()).loadTextView();

                dismiss();
            } else {
                Toast.makeText(getContext(), "Invalid input!", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}