package com.example.consumerbasket

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var db = DBHelper(this)
    private var id = 1

    private lateinit var toolbar: Toolbar
    private lateinit var productNameET: EditText
    private lateinit var productWeightET: EditText
    private lateinit var productPriceET: EditText
    private lateinit var productListLV: ListView
    private lateinit var saveBTN: Button
    private lateinit var updateBTN: Button
    private lateinit var deleteBTN: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        init()

        saveBTN.setOnClickListener {
            val newName = productNameET.text.toString()
            val newWeight = productWeightET.text.toString().toInt()
            val newPrice = productPriceET.text.toString().toInt()
            val newProduct = Product(id, newName, newWeight, newPrice)
            db.addProduct(newProduct)
            Toast.makeText(this, "$newName, $newWeight и $newPrice добавлены в базу данных", Toast.LENGTH_LONG).show()
            productNameET.text.clear()
            productWeightET.text.clear()
            productPriceET.text.clear()

        }

        updateBTN.setOnClickListener {
            val newName = productNameET.text.toString()
            val newWeight = productWeightET.text.toString().toInt()
            val newPrice = productPriceET.text.toString().toInt()
            val newProduct = Product(id, newName, newWeight, newPrice)
            db.updateProduct(newProduct)
        }

        deleteBTN.setOnClickListener {
            val newName = productNameET.text.toString()
            val newWeight = productWeightET.text.toString().toInt()
            val newPrice = productPriceET.text.toString().toInt()
            val newProduct = Product(id, newName, newWeight, newPrice)
            db.deleteProduct(newProduct)
        }

    }

    private fun init() {
        toolbar = findViewById(R.id.mainToolbarTB)
        productNameET = findViewById(R.id.productNameET)
        productWeightET = findViewById(R.id.productWeightET)
        productPriceET = findViewById(R.id.productPriceET)
        productListLV = findViewById(R.id.productListLV)
        saveBTN = findViewById(R.id.saveBTN)
        updateBTN = findViewById(R.id.updateBTN)
        deleteBTN = findViewById(R.id.deleteBTN)
        setSupportActionBar(toolbar)
    }



}