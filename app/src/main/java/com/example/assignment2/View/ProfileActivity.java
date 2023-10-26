package com.example.assignment2.View;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment2.Controller.DatabaseHelper;
import com.example.assignment2.Model.AccessDAO;
import com.example.assignment2.Model.AccessType;
import com.example.assignment2.Model.ProfileDAO;
import com.example.assignment2.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private TextView profileInfo;
    private ListView accessList;
    private DatabaseHelper dbHelper;
    private long profileId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_activity);

        MaterialToolbar toolbar = findViewById(R.id.toolbar_profile);
        profileInfo = findViewById(R.id.txt_profileInfo);
        accessList = findViewById(R.id.list_access);

        dbHelper = new DatabaseHelper(getApplicationContext());

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            Log.e("ProfileActivity", "No Support Action Bar found!");
        }

        setProfilePage();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Timestamp date = new Timestamp(System.currentTimeMillis());
            dbHelper.addAccess(new AccessDAO(-1, profileId, AccessType.CLOSED, date.toString()));
            Log.i("ProfileActivity", "Back to Home Button Pressed");
        }
        return super.onOptionsItemSelected(item);
    }

    private void setProfilePage() {
        Bundle extras = getIntent().getExtras();
        long id = 0;
        if (extras != null) {
            id = extras.getLong("profileId", -1);
        }
        if (id <= 0) {
            Log.e("ProfileActivity", "No valid profile ID passed");
            return;
        }
        profileId = id;
        setProfileText(id);
        setProfileAccessList(id);
    }

    private void setProfileText(long id) {

        ProfileDAO profile = dbHelper.getProfileById(id);
        String text = String.format(getResources().getString(R.string.template_user_profile),
                profile.getSurname(),
                profile.getName(),
                profile.getId(),
                profile.getGpa(),
                profile.getDate());
        profileInfo.setText(text);
    }

    public void setProfileAccessList(long id) {
        List<AccessDAO> accesses = dbHelper.getAccessesByProfile(id);
        List<String> displayList = new ArrayList<>();
        for (AccessDAO item : accesses) {
            String date = item.getTimestamp();
            String activity = item.getType().name();
            displayList.add(date + " - " + activity);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, displayList);
        accessList.setAdapter(adapter);
    }
}
