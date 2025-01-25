package com.example.consumerbasket

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
        companion object {
            private val DATABASE_NAME = "PERSON_DATABASE"
            private val DATABASE_VERSION = 1
            val TABLE_NAME = "product_table"
            val KEY_ID = "id"
            val KEY_NAME = "name"
            val KEY_WEIGHT = "weight"
            val KEY_PRICE = "price"
        }

    override fun onCreate(db: SQLiteDatabase) {
        val PRODUCT_TABLE = ("CREATE TABLE " + TABLE_NAME + " (" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_NAME + " TEXT," +
                KEY_WEIGHT + " TEXT," +
                KEY_PRICE + " TEXT" + ")")
        db.execSQL(PRODUCT_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
    }

    fun addProduct(product: Product) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, product.productId)
        contentValues.put(KEY_NAME, product.name)
        contentValues.put(KEY_WEIGHT, product.weight)
        contentValues.put(KEY_PRICE, product.price)
        db.insert(TABLE_NAME, null, contentValues)
        db.close()
    }

    @SuppressLint("Range")
    fun readProduct(product: Product): MutableList<Product> {
        val productList: MutableList<Product> = mutableListOf()
        val selectQuery = "SELECT FROM $TABLE_NAME"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return productList
        }

        var productId: Int
        var productName: String
        var productWeight: Int
        var productPrice: Int
        if (cursor.moveToFirst()) {
            do {
                productId = cursor.getInt(cursor.getColumnIndex("id"))
                productName = cursor.getString(cursor.getColumnIndex("name"))
                productWeight = cursor.getInt(cursor.getColumnIndex("weight"))
                productPrice = cursor.getInt(cursor.getColumnIndex("price"))
                val product = Product(productId = productId, name = productName, weight = productWeight, price = productPrice)
                productList.add(product)
            } while (cursor.moveToNext())
        }
        return productList
    }


    fun updateProduct(product: Product) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, product.productId)
        contentValues.put(KEY_NAME, product.name)
        contentValues.put(KEY_WEIGHT, product.weight)
        contentValues.put(KEY_PRICE, product.price)
        db.update(TABLE_NAME, contentValues, "id=" + product.productId, null)
        db.close()
    }


    fun deleteProduct(product: Product) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, product.productId)
        db.delete(TABLE_NAME, "id=" + product.productId, null)
        db.close()
    }
}