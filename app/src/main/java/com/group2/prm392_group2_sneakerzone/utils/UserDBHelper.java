package com.group2.prm392_group2_sneakerzone.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.group2.prm392_group2_sneakerzone.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SneakerZoneDB";
    private static final int DATABASE_VERSION = 1;

    // Singleton instance
    private static UserDBHelper instance;

    // Tên bảng và các cột của bảng Users
    private static final String TABLE_USERS = "Users";
    private static final String COLUMN_USER_ID = "UserId";
    private static final String COLUMN_NAME = "Name";
    private static final String COLUMN_EMAIL = "Email";
    private static final String COLUMN_PASSWORD = "Password";
    private static final String COLUMN_PHONE_NUMBER = "PhoneNumber";
    private static final String COLUMN_ADDRESS = "Address";
    private static final String COLUMN_IS_ACTIVE = "IsActive";
    private static final String COLUMN_ROLE = "Role";

    // Singleton getInstance method
    public static synchronized UserDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new UserDBHelper(context.getApplicationContext());
        }
        return instance;
    }

    // Private constructor
    private UserDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng Users
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_PHONE_NUMBER + " TEXT, " +
                COLUMN_ADDRESS + " TEXT, " +
                COLUMN_IS_ACTIVE + " BOOLEAN, " +
                COLUMN_ROLE + " INTEGER)";
        db.execSQL(CREATE_USERS_TABLE);

        if (!hasUsers()) {
            seedUsers(db);
        }
    }

    // Seed dữ liệu mẫu cho từng role
    private void seedUsers(SQLiteDatabase db) {
        addUserSeed(db, "Admin", "admin@gmail.com", "123456", "0123456789", "Admin Address", 1, true);
        addUserSeed(db, "Store Owner", "storeowner@gmail.com", "123456", "0123456789", "Store Owner Address", 2, true);
        addUserSeed(db, "Manager", "manager@gmail.com", "123456", "0123456789", "Manager Address", 3, true);
        addUserSeed(db, "Customer", "customer@gmail.com", "123456", "0123456789", "Customer Address", 4, true);
    }

    // Phương thức seed một user vào DB
    private void addUserSeed(SQLiteDatabase db, String name, String email, String password, String phone, String address, int role, boolean isActive) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_PHONE_NUMBER, phone);
        values.put(COLUMN_ADDRESS, address);
        values.put(COLUMN_ROLE, role);
        values.put(COLUMN_IS_ACTIVE, isActive ? 1 : 0);

        db.insert(TABLE_USERS, null, values);
    }

    public boolean hasUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_USERS;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Thêm User
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, user.getName());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_PHONE_NUMBER, user.getPhoneNumber());
        values.put(COLUMN_ADDRESS, user.getAddress());
        values.put(COLUMN_IS_ACTIVE, user.isActive());
        values.put(COLUMN_ROLE, user.getRole());

        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    // Lấy tất cả Users
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ROLE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_ACTIVE)) == 1
                );
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return userList;
    }

    // Lấy User bằng email và password
    public User getUserByEmailAndPassword(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});

        if (cursor.moveToFirst()) {
            User user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ROLE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_ACTIVE)) == 1
            );
            cursor.close();
            return user;
        }
        cursor.close();
        return null;
    }

    // Cập nhật User
    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, user.getName());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_PHONE_NUMBER, user.getPhoneNumber());
        values.put(COLUMN_ADDRESS, user.getAddress());
        values.put(COLUMN_IS_ACTIVE, user.isActive());
        values.put(COLUMN_ROLE, user.getRole());

        return db.update(TABLE_USERS, values, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(user.getUserId())});
    }

    // Xóa User
    public void deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)});
        db.close();
    }
}
