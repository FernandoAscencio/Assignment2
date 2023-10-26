package com.example.assignment2.Controller;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.assignment2.Model.ACCESS;
import com.example.assignment2.Model.AccessDAO;
import com.example.assignment2.Model.AccessType;
import com.example.assignment2.Model.PROFILE;
import com.example.assignment2.Model.ProfileDAO;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "StudentsDB";
    private Context context = null;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String profileQuery = "CREATE TABLE " + PROFILE.TABLE_NAME + " (" +
                PROFILE.id + " INTEGER PRIMARY KEY, " +
                PROFILE.name + " TEXT NOT NULL, " +
                PROFILE.surname + " TEXT, " +
                PROFILE.gpa + " REAL, " +
                PROFILE.date + " TEXT)";
        String accessQuery = "CREATE TABLE " + ACCESS.TABLE_NAME + " (" +
                ACCESS.id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ACCESS.number + " TEXT NOT NULL, " +
                ACCESS.type + " INTEGER, " +
                ACCESS.timestamp + " TEXT)";
        // Create both tables when the databaseHelper is created.
        sqLiteDatabase.execSQL(profileQuery);
        sqLiteDatabase.execSQL(accessQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // No upgrade expected. Drop all tables
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PROFILE.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ACCESS.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public ProfileDAO addProfile(ProfileDAO profile) {
        long id = -1;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(PROFILE.id, profile.getId());
        contentValues.put(PROFILE.name, profile.getName());
        contentValues.put(PROFILE.surname, profile.getSurname());
        contentValues.put(PROFILE.gpa, profile.getGpa());
        contentValues.put(PROFILE.date, profile.getDate());

        try {
            id = db.insertOrThrow(PROFILE.TABLE_NAME, null, contentValues);
        } catch (Exception e) {
            Toast.makeText(context, "Profile insert failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
        profile.setId(id);
        return profile;
    }
    public ProfileDAO getProfileById(long searchId) {
        String where = PROFILE.id + " = ?";
        String[] whereArgs = new String[]{String.valueOf(searchId)};
        List<ProfileDAO> results = getProfiles(where, whereArgs, null);
        if (results.size() > 1) {
            Toast.makeText(context, "DB get failed: More than one entry with that ID", Toast.LENGTH_LONG).show();
            return new ProfileDAO(-1, null, null, 0, null);
        }
        return results.get(0);
    }
    public List<ProfileDAO> getOrderedProfiles (String orderBy) {
        return getProfiles(null, null, orderBy);
    }
    public List<ProfileDAO> getOrderedProfiles(boolean isDefault) {
        if (isDefault) return getOrderedProfiles(PROFILE.surname + " ASC");
        else return getOrderedProfiles(PROFILE.id + " ASC");
    }
    public List<ProfileDAO> getAllProfiles() {
        return getProfiles(null, null,null);
    }
    protected List<ProfileDAO> getProfiles(String where, String[] whereArgs, String orderBy) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        List<ProfileDAO> profiles = new ArrayList<>();

        try {
            cursor = db.query(PROFILE.TABLE_NAME, null, where, whereArgs, null, null, orderBy);
            if(cursor != null) {
                if(cursor.moveToFirst()) {
                    do {
                        @SuppressLint("Range")
                        long id = cursor.getLong(cursor.getColumnIndex(PROFILE.id));
                        @SuppressLint("Range")
                        String name = cursor.getString(cursor.getColumnIndex(PROFILE.name));
                        @SuppressLint("Range")
                        String surname = cursor.getString(cursor.getColumnIndex(PROFILE.surname));
                        @SuppressLint("Range")
                        double gpa = cursor.getDouble(cursor.getColumnIndex(PROFILE.gpa));
                        @SuppressLint("Range")
                        String date = cursor.getString(cursor.getColumnIndex(PROFILE.date));

                        profiles.add(new ProfileDAO(id, name, surname, gpa, date));
                    } while(cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            Toast.makeText(context, "DB get failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            db.close();
            if(cursor != null) cursor.close();
        }
        return profiles;
    }

    public AccessDAO addAccess(AccessDAO access) {
        long id = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ACCESS.number, access.getProfileId());
        contentValues.put(ACCESS.type, access.getType().ordinal());
        contentValues.put(ACCESS.timestamp, access.getTimestamp());

        try {
            id = db.insertOrThrow(ACCESS.TABLE_NAME, null, contentValues);
        } catch (Exception e) {
            Toast.makeText(context, "Profile insert failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
        access.setId(id);
        return access;
    }
    public AccessDAO getAccessById(long searchId) {
        String where = ACCESS.id + " = ?";
        String[] whereArgs = new String[]{String.valueOf(searchId)};
        List<AccessDAO> results = getAccesses(where, whereArgs, null);
        if (results.size() > 1) {
            Toast.makeText(context, "DB get failed: More than one entry with that ID", Toast.LENGTH_LONG).show();
            return new AccessDAO(-1, -1, null, null);
        }
        return results.get(0);
    }
    public List<AccessDAO> getOrderedAccesses(String orderBy) {
        return getAccesses(null, null, orderBy);
    }
    public List<AccessDAO> getAllAccesses() {
        return getAccesses(null, null, null);
    }
    public List<AccessDAO> getAccessesByProfile(long id) {
        String where = ACCESS.number + " = ?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        return getAccesses(where, whereArgs, null);
    }
    protected List<AccessDAO> getAccesses(String where, String[] whereArgs, String orderBy) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        List<AccessDAO> accesses = new ArrayList<>();

        try {
            cursor = db.query(ACCESS.TABLE_NAME, null, where, whereArgs, null, null, orderBy);
            if(cursor != null) {
                if(cursor.moveToFirst()) {
                    do {
                        @SuppressLint("Range")
                        long id = cursor.getLong(cursor.getColumnIndex(ACCESS.id));
                        @SuppressLint("Range")
                        long profileId = cursor.getLong(cursor.getColumnIndex(ACCESS.number));
                        @SuppressLint("Range")
                        int ordinalType = cursor.getInt(cursor.getColumnIndex(ACCESS.type));
                        AccessType type = AccessType.values()[ordinalType];
                        @SuppressLint("Range")
                        String timestamp = cursor.getString(cursor.getColumnIndex(ACCESS.timestamp));

                        accesses.add(new AccessDAO(id, profileId, type, timestamp));
                    } while(cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            Toast.makeText(context, "DB get failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            db.close();
            if(cursor != null) cursor.close();
        }
        return accesses;
    }
}
