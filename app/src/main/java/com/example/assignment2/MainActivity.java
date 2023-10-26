package com.example.assignment2;

import android.content.Intent;
import android.os.Bundle;

import com.example.assignment2.Controller.DatabaseHelper;
import com.example.assignment2.Model.AccessDAO;
import com.example.assignment2.Model.AccessType;
import com.example.assignment2.Model.ProfileDAO;
import com.example.assignment2.View.InsertProfile;
import com.example.assignment2.View.ProfileActivity;
import com.google.android.material.appbar.MaterialToolbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private static final String TAG = "MainActivity";
    private ArrayAdapter<String> adapter;
    private HashMap<Integer, Long> entryToId = new HashMap<>();
    private boolean isDefaultDisplay = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView profilesList = findViewById(R.id.list_profiles);
        MaterialToolbar toolbar = findViewById(R.id.toolbar_main);
        FloatingActionButton fab = findViewById(R.id.fab);

        setSupportActionBar(toolbar);

        dbHelper = new DatabaseHelper(getApplicationContext());

        List<ProfileDAO> profiles = dbHelper.getOrderedProfiles(isDefaultDisplay);
        adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, displayProfileList(profiles));
        profilesList.setAdapter(adapter);

        fab.setOnClickListener((view) -> {
            Log.d(TAG, "FAB pressed!");
            InsertProfile dialog = new InsertProfile();
            dialog.show(getSupportFragmentManager(), "InsertProfile");

            loadTextView();
        });

        profilesList.setOnItemClickListener((adapterView, view, i, l) -> {
            long profileId = getProfileId(i);
            Timestamp date = new Timestamp(System.currentTimeMillis());

            dbHelper.addAccess(new AccessDAO(-1, profileId, AccessType.OPENED, date.toString()));

            goToProfileActivity(profileId);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.tgl_order) {
            isDefaultDisplay = !isDefaultDisplay;
            loadTextView();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.tgl_order);
        // Toggle the title of the option so it is clear on what it does.
        if (isDefaultDisplay) item.setTitle(getResources().getString(R.string.tgl_order_default));
        else item.setTitle(getResources().getString(R.string.tgl_order_id));

        return super.onPrepareOptionsMenu(menu);
    }

    public void loadTextView() {
        List<ProfileDAO> profiles = dbHelper.getOrderedProfiles(isDefaultDisplay);
        adapter.clear();
        adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, displayProfileList(profiles));
        adapter.notifyDataSetChanged();
    }

    private void goToProfileActivity(long id) {
        Intent intent = new Intent(getBaseContext(), ProfileActivity.class);
        intent.putExtra("profileId", id);

        startActivity(intent);
    }

    private List<String> displayProfileList(List<ProfileDAO> list) {
        List<String> displayList = new ArrayList<>();
        entryToId.clear();
        // count is used for both, the number in the display list and the key for the map table.
        int count = 0;
        // This might work using the toString() function on the Profile and Access DAO.
        for (ProfileDAO item : list) {
            entryToId.put(count, item.getId());
            if (isDefaultDisplay) {
                String surname = item.getSurname();
                String name = item.getName();
                // Increase the number first, to prevent the list from starting at 0.
                displayList.add(++count + ". " + surname + ", " + name);
            } else {
                long profileId = item.getId();
                displayList.add(++count + ". " + profileId);
            }
        }
        return displayList;
    }

    private long getProfileId(int i) {
        Log.i("MainActivity", "From id location: "+i+" to profileID: " + entryToId.get(i));
        return entryToId.get(i);
    }
}