package com.example.consumerbasket

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class MyListAdapter(
    private val context: Context,
    private val productList: MutableList<Product>
) : ArrayAdapter<Product>(context, R.layout.list_item, productList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val product = getItem(position)
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }

        val nameText = view?.findViewById<TextView>(R.id.nameItemTV)
        val weightText = view?.findViewById<TextView>(R.id.weightItemTV)
        val priceText = view?.findViewById<TextView>(R.id.priceItemTV)

        nameText?.text = "название: ${product?.name}"
        weightText?.text = "вес: ${product?.weight}"
        priceText?.text = "цена: ${product?.price}"

        return view!!
    }
}