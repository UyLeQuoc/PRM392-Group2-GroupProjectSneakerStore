package com.group2.prm392_group2_sneakerzone.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.group2.prm392_group2_sneakerzone.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SneakerZoneDB";
    private static final int DATABASE_VERSION = 2; // Tăng version để kích hoạt onUpgrade

    // Singleton instance
    private static ProductDBHelper instance;

    // Tên bảng và các cột của bảng Products
    private static final String TABLE_PRODUCTS = "Products";
    private static final String COLUMN_PRODUCT_ID = "ProductId";
    private static final String COLUMN_PRODUCT_NAME = "ProductName";
    private static final String COLUMN_BRAND_ID = "BrandId";
    private static final String COLUMN_STORE_ID = "StoreId";
    private static final String COLUMN_PRICE = "Price";
    private static final String COLUMN_DESCRIPTION = "Description";
    private static final String COLUMN_CREATED_DATE = "CreatedDate";
    private static final String COLUMN_UPDATED_DATE = "UpdatedDate";

    // Singleton getInstance method
    public static synchronized ProductDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new ProductDBHelper(context.getApplicationContext());
        }
        return instance;
    }

    // Private constructor
    private ProductDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng Products nếu chưa tồn tại
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCTS + " (" +
                COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PRODUCT_NAME + " TEXT, " +
                COLUMN_BRAND_ID + " INTEGER, " +
                COLUMN_STORE_ID + " INTEGER, " +
                COLUMN_PRICE + " REAL, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_CREATED_DATE + " TEXT, " +
                COLUMN_UPDATED_DATE + " TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE);

        seedProducts(db); // Seed dữ liệu mẫu nếu cần thiết
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa bảng Products nếu tồn tại khi nâng cấp cơ sở dữ liệu
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db); // Gọi lại onCreate để tạo lại bảng
    }

    // Phương thức seed data cho Products
    private void seedProducts(SQLiteDatabase db) {
        addProductSeed(db, "Nike Air Max", 1, 1, 120.99, "Nike's popular Air Max sneakers", "2023-01-01", "2023-01-01");
        addProductSeed(db, "Adidas Ultraboost", 2, 1, 150.50, "Adidas' high-performance running shoes", "2023-02-15", "2023-02-15");
        addProductSeed(db, "Puma RS-X", 3, 2, 99.99, "Puma RS-X reinvention series sneakers", "2023-03-10", "2023-03-10");
        addProductSeed(db, "Converse Chuck Taylor", 4, 3, 75.00, "Classic Converse high-top sneakers", "2023-04-22", "2023-04-22");
        addProductSeed(db, "Vans Old Skool", 5, 3, 60.00, "Skateboarding shoes with classic side stripe", "2023-05-10", "2023-05-10");
    }

    // Phương thức hỗ trợ để thêm từng sản phẩm seed vào bảng Products
    private void addProductSeed(SQLiteDatabase db, String productName, int brandId, int storeId, double price, String description, String createdDate, String updatedDate) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, productName);
        values.put(COLUMN_BRAND_ID, brandId);
        values.put(COLUMN_STORE_ID, storeId);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_CREATED_DATE, createdDate);
        values.put(COLUMN_UPDATED_DATE, updatedDate);

        db.insert(TABLE_PRODUCTS, null, values);
    }

    // Thêm Product
    public void addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, product.getProductName());
        values.put(COLUMN_BRAND_ID, product.getBrandId());
        values.put(COLUMN_STORE_ID, product.getStoreId());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_DESCRIPTION, product.getDescription());
        values.put(COLUMN_CREATED_DATE, product.getCreatedDate());
        values.put(COLUMN_UPDATED_DATE, product.getUpdatedDate());

        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
    }

    // Lấy tất cả Products
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BRAND_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STORE_ID)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UPDATED_DATE))
                );
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    // Lấy Product bằng ID
    public Product getProductById(int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCT_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(productId)});

        if (cursor.moveToFirst()) {
            Product product = new Product(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BRAND_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STORE_ID)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UPDATED_DATE))
            );
            cursor.close();
            return product;
        }
        cursor.close();
        return null;
    }

    // Cập nhật Product
    public int updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, product.getProductName());
        values.put(COLUMN_BRAND_ID, product.getBrandId());
        values.put(COLUMN_STORE_ID, product.getStoreId());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_DESCRIPTION, product.getDescription());
        values.put(COLUMN_CREATED_DATE, product.getCreatedDate());
        values.put(COLUMN_UPDATED_DATE, product.getUpdatedDate());

        return db.update(TABLE_PRODUCTS, values, COLUMN_PRODUCT_ID + " = ?", new String[]{String.valueOf(product.getProductId())});
    }

    // Xóa Product
    public void deleteProduct(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, COLUMN_PRODUCT_ID + " = ?", new String[]{String.valueOf(productId)});
        db.close();
    }
}
