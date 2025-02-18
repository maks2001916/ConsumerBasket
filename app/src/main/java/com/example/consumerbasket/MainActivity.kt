package com.example.consumerbasket

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var db = DBHelper(this)

    private lateinit var toolbar: Toolbar
    private lateinit var productNameET: EditText
    private lateinit var productWeightET: EditText
    private lateinit var productPriceET: EditText
    private lateinit var productListLV: ListView
    private lateinit var saveBTN: Button
    private lateinit var updateBTN: Button
    private lateinit var deleteBTN: Button

    private var product: Product? = null
    private var products: MutableList<Product> = mutableListOf()
    private var listAdapter: MyListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        init()

        saveBTN.setOnClickListener {
            if (checkFieldsIsNotEmpty()) {
                val newName = productNameET.text.toString()
                val newWeight = productWeightET.text.toString().toInt()
                val newPrice = productPriceET.text.toString().toInt()
                product = Product(name = newName, weight =  newWeight, price =  newPrice)
                products.add(product!!)
                db.addProduct(product!!)

                Toast.makeText(this, "$newName, $newWeight и $newPrice добавлены в базу данных", Toast.LENGTH_LONG).show()
                productNameET.text.clear()
                productWeightET.text.clear()
                productPriceET.text.clear()
                viewDataAdapter()
            } else {
                printMessageEmptyFields()
            }
        }



    }

    override fun onResume() {
        super.onResume()
        updateBTN.setOnClickListener { updateRecord() }
        deleteBTN.setOnClickListener { deleteRecord() }
    }

    private fun updateRecord() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.update_dialog, null)
        dialogBuilder.setView(dialogView)
        val editName = dialogView.findViewById<EditText>(R.id.editProductNameET)
        val editWeight = dialogView.findViewById<EditText>(R.id.editProductWeightET)
        val editPrice = dialogView.findViewById<EditText>(R.id.editProductPriceET)

        dialogBuilder.setTitle(getString(R.string.update_record))
        dialogBuilder.setMessage(getString(R.string.enter_details_below))
        dialogBuilder.setPositiveButton(getString(R.string.update)) { _,_ ->
            val updateName = editName.text.toString()
            val updateWeight = editWeight.text.toString()
            val updatePrice = editPrice.text.toString()
            if (updateName.trim() != "" &&
                updateWeight.trim() != "" &&
                updatePrice.trim() != "")
                product = Product(
                    name = updateName,
                    weight =  Integer.parseInt(updateWeight),
                    price =  Integer.parseInt(updatePrice))
            db.updateProduct(product!!)
            viewDataAdapter()
            Toast.makeText(this, getString(R.string.the_record_has_been_updated), Toast.LENGTH_LONG).show()
        }
        dialogBuilder.setNegativeButton(getString(R.string.cancel)) { dialog, which -> }
        dialogBuilder.create().show()
    }

    private fun deleteRecord() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.delete_dialog, null)
        dialogBuilder.setView(dialogView)

        val chooseDeleteName = dialogView.findViewById<EditText>(R.id.deleteProductET)
        dialogBuilder.setTitle(getString(R.string.delete_record))
        dialogBuilder.setMessage(getString(R.string.enter_details_below))
        dialogBuilder.setPositiveButton(getString(R.string.delete)) { _,_ ->
            if (chooseDeleteName.text.trim() != "") {
                val isDeleted = db.deleteProductByName(chooseDeleteName.text.toString())
                if (isDeleted) {
                    Toast.makeText(this,
                        getString(R.string.the_entry_was_added_successfully), Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this,
                        getString(R.string.no_product_with_that_name_was_found), Toast.LENGTH_LONG).show()
                }
                viewDataAdapter()
            }
        }
        dialogBuilder.setNegativeButton(getString(R.string.cancel)) { dialog, which -> }
        dialogBuilder.create().show()

    }

    private fun checkFieldsIsNotEmpty(): Boolean {
        if (productNameET.text.toString().isNotEmpty() &&
            productWeightET.text.toString().isNotEmpty() &&
            productPriceET.text.toString().isNotEmpty()) {
            return true
        } else {
            return false
        }
    }

    private fun printMessageEmptyFields() {
        Toast.makeText(
            this,
            getString(R.string.not_all_fields_are_filled_in),
            Toast.LENGTH_LONG)
            .show()
    }


    private fun viewDataAdapter() {
        products = db.readProduct()
        listAdapter = MyListAdapter(this, products)
        productListLV.adapter = listAdapter
        listAdapter?.notifyDataSetChanged()
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
        viewDataAdapter()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {R.id.exit -> finishAffinity() }
        return super.onOptionsItemSelected(item)
    }

}