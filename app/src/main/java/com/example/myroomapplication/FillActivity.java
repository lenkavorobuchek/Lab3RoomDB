package com.example.myroomapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FillActivity extends AppCompatActivity {

    Button applyButton;
    EditText nameField, surnameField, patronymicField, phoneField, emailField, studyField, workField;
    int saved_id = 0, saved_position = 0;
    boolean edit, only_view = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        applyButton = findViewById(R.id.finish_button);
        nameField = findViewById(R.id.name);
        surnameField = findViewById(R.id.surname);
        patronymicField = findViewById(R.id.patronymic);
        if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            phoneField = findViewById(R.id.editTextPhone);
            emailField = findViewById(R.id.editTextTextEmailAddress);
            studyField = findViewById(R.id.Study);
            workField = findViewById(R.id.editTextWork);
        }

        Bundle receivedData = getIntent().getExtras();
        Contacts receivedContacts;
        Other receivedOtherData;
        if (receivedData != null) {
            User receivedUserData = receivedData.getParcelable("modified user");
            receivedContacts = receivedData.getParcelable("modified contacts");
            receivedOtherData = receivedData.getParcelable("modified other info");
            edit = true;
            nameField.setText(receivedUserData.firstName);
            surnameField.setText(receivedUserData.lastName);
            patronymicField.setText(receivedUserData.patronymicName);
            saved_id = receivedUserData.uid;
            saved_position = getIntent().getIntExtra("position", 0);
            only_view = getIntent().getBooleanExtra("view", false);
            if (only_view) {
                nameField.setEnabled(false);
                surnameField.setEnabled(false);
                patronymicField.setEnabled(false);
                if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    phoneField.setEnabled(false);
                    emailField.setEnabled(false);
                    studyField.setEnabled(false);
                    workField.setEnabled(false);
                }
            }
            if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                phoneField.setText(receivedContacts.phoneNumber);
                emailField.setText(receivedContacts.email);
                studyField.setText(receivedOtherData.studyPlace);
                workField.setText(receivedOtherData.workPlace);
            }
        } else {
            receivedContacts = new Contacts("----------------------", "------------------");
            receivedOtherData = new Other("------", "----------");
        }

        Intent result = getIntent();

        applyButton.setOnClickListener(view -> {
            String name = nameField.getText().toString();
            String surname = surnameField.getText().toString();
            String patronymic = patronymicField.getText().toString();
            String pattern = "[a-zA-ZА-Яа-я]+|-" + (only_view ? "*" : "+");
            String phonePattern = "(\\+[0-9]{1,3}|\\+1-[0-9]{3}|[0-9])[(-]?[0-9]{3}[)-]?[0-9]{3}-?[0-9]{2}-?[0-9]{2}|-"  + (only_view ? "*" : "+");
            String emailPattern = "[a-zA-Z0-9]+@[a-z]{2,10}.[a-z]{2,10}|-" + (only_view ? "*" : "+");

            if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (studyField.getText().toString().matches(pattern) && workField.getText().toString().matches(pattern) &&
                        phoneField.getText().toString().matches(phonePattern) && emailField.getText().toString().matches(emailPattern)) {
                    result.putExtra("study", studyField.getText().toString());
                    result.putExtra("work", workField.getText().toString());
                    result.putExtra("phone", phoneField.getText().toString());
                    result.putExtra("email", emailField.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Поля не соответствуют нужному формату", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                result.putExtra("study", receivedOtherData.studyPlace);
                result.putExtra("work", receivedOtherData.workPlace);
                result.putExtra("phone", receivedContacts.phoneNumber);
                result.putExtra("email", receivedContacts.email);
            }
            if (name.matches(pattern) && surname.matches(pattern) && patronymic.matches(pattern)) {
                result.putExtra("name", name);
                result.putExtra("surname", surname);
                result.putExtra("patronymic", patronymic);
                result.putExtra("id", saved_id);
                result.putExtra("position", saved_position);
                result.putExtra("view", only_view);
                setResult(edit ? 2 : 1 , result);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Поля не соответствуют нужному формату", Toast.LENGTH_SHORT).show();
            }
        });
    }
}