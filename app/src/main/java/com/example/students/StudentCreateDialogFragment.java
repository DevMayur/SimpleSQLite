package com.example.students;


import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.students.database.Config;
import com.example.students.database.DatabaseQueryClass;

public class StudentCreateDialogFragment extends DialogFragment {

    private static StudentCreateListener studentCreateListener;

    private EditText nameEditText;
    private EditText registrationEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private Button createButton;
    private Button cancelButton;

    private String nameString = "";
    private long registrationNumber = -1;
    private String phoneString = "";
    private String emailString = "";

    public StudentCreateDialogFragment() {
        // Required empty public constructor
    }

    public static StudentCreateDialogFragment newInstance(String title, StudentCreateListener listener){
        studentCreateListener = listener;
        StudentCreateDialogFragment studentCreateDialogFragment = new StudentCreateDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        studentCreateDialogFragment.setArguments(args);

        studentCreateDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return studentCreateDialogFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_student_create_dialog, container, false);

        nameEditText = view.findViewById(R.id.studentNameEditText);
        registrationEditText = view.findViewById(R.id.registrationEditText);
        phoneEditText = view.findViewById(R.id.phoneEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        createButton = view.findViewById(R.id.createButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        String title = getArguments().getString(Config.TITLE);
        getDialog().setTitle(title);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (registrationEditText.getText().toString().length() > 6) {
                    Log.d("reg_err","Registration number length");
                } else {
                    nameString = nameEditText.getText().toString();
                    registrationNumber = Integer.parseInt(registrationEditText.getText().toString());
                    phoneString = phoneEditText.getText().toString();
                    emailString = emailEditText.getText().toString();

                    Student student = new Student(-1, nameString, registrationNumber, phoneString, emailString);

                    DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(getContext());

                    long id = databaseQueryClass.insertStudent(student);

                    if (id > 0) {
                        student.setId(id);
                        studentCreateListener.onStudentCreated(student);
                        getDialog().dismiss();
                    }
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            //noinspection ConstantConditions
            dialog.getWindow().setLayout(width, height);
        }
    }

}
