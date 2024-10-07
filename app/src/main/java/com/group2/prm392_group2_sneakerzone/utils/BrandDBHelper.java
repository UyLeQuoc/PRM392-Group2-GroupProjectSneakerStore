package com.group2.prm392_group2_sneakerzone.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.group2.prm392_group2_sneakerzone.model.Brand;

import java.util.ArrayList;
import java.util.List;

public class BrandDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SneakerZoneDB";
    private static final int DATABASE_VERSION = 1;

    // Singleton instance
    private static BrandDBHelper instance;

    // Tên bảng và các cột của bảng Brands
    private static final String TABLE_BRANDS = "Brands";
    private static final String COLUMN_BRAND_ID = "BrandId";
    private static final String COLUMN_BRAND_NAME = "BrandName";
    private static final String COLUMN_CREATED_BY = "CreatedBy";
    private static final String COLUMN_CREATED_DATE = "CreatedDate";

    // Singleton getInstance method
    public static synchronized BrandDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new BrandDBHelper(context.getApplicationContext());
        }
        return instance;
    }

    // Private constructor
    private BrandDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng Brands
        String CREATE_BRANDS_TABLE = "CREATE TABLE " + TABLE_BRANDS + " (" +
                COLUMN_BRAND_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BRAND_NAME + " TEXT, " +
                COLUMN_CREATED_BY + " INTEGER, " +
                COLUMN_CREATED_DATE + " TEXT)";
        db.execSQL(CREATE_BRANDS_TABLE);

        seedBrands(db);  // Seed dữ liệu mẫu
    }

    // Seed dữ liệu thương hiệu mẫu
    private void seedBrands(SQLiteDatabase db) {
        addBrandSeed(db, "Nike", 1, "2024-01-01");
        addBrandSeed(db, "Adidas", 1, "2024-01-01");
        addBrandSeed(db, "Puma", 2, "2024-01-01");
    }

    // Thêm dữ liệu mẫu cho bảng Brands
    private void addBrandSeed(SQLiteDatabase db, String brandName, int createdBy, String createdDate) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_BRAND_NAME, brandName);
        values.put(COLUMN_CREATED_BY, createdBy);
        values.put(COLUMN_CREATED_DATE, createdDate);
        db.insert(TABLE_BRANDS, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BRANDS);
        onCreate(db);
    }

    // Thêm Brand mới
    public void addBrand(Brand brand) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BRAND_NAME, brand.getBrandName());
        values.put(COLUMN_CREATED_BY, brand.getCreatedBy());
        values.put(COLUMN_CREATED_DATE, brand.getCreatedDate());

        db.insert(TABLE_BRANDS, null, values);
        db.close();
    }

    // Lấy tất cả Brands
    public List<Brand> getAllBrands() {
        List<Brand> brandList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_BRANDS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Brand brand = new Brand(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BRAND_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BRAND_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CREATED_BY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_DATE))
                );
                brandList.add(brand);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return brandList;
    }

    // Lấy Brand theo ID
    public Brand getBrandById(int brandId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_BRANDS + " WHERE " + COLUMN_BRAND_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(brandId)});

        if (cursor.moveToFirst()) {
            Brand brand = new Brand(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BRAND_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BRAND_NAME)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CREATED_BY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_DATE))
            );
            cursor.close();
            return brand;
        }
        cursor.close();
        return null;
    }

    // Cập nhật Brand
    public int updateBrand(Brand brand) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BRAND_NAME, brand.getBrandName());
        values.put(COLUMN_CREATED_BY, brand.getCreatedBy());
        values.put(COLUMN_CREATED_DATE, brand.getCreatedDate());

        return db.update(TABLE_BRANDS, values, COLUMN_BRAND_ID + " = ?", new String[]{String.valueOf(brand.getBrandId())});
    }

    // Xóa Brand
    public void deleteBrand(int brandId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BRANDS, COLUMN_BRAND_ID + " = ?", new String[]{String.valueOf(brandId)});
        db.close();
    }
}

