package com.example.imagepickerfirebase.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.imagepickerfirebase.R
import com.example.imagepickerfirebase.model.Books
import com.example.imagepickerfirebase.view.PdfViewActivity


class BooksAdapter(private val listBook: ArrayList<Books> ) :
    RecyclerView.Adapter<BooksAdapter.MyViewHolder>() {

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position    : Int)
    }
    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pdf_row_data, parent, false)
        return MyViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: BooksAdapter.MyViewHolder, position: Int) {
        val currentItem = listBook[position]
        holder.tvTitle.text = currentItem.booksTitle

    }

    override fun getItemCount(): Int {
        return listBook.size
    }

    class MyViewHolder(view: View,  clickListener: onItemClickListener) : ViewHolder(view) {
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)

        init {
            itemView.setOnClickListener {
                //adapterPosition berfungsi untuk mengambil posisi item ketika diklik kemudian di tampung kedalam fun onitemclick
                clickListener.onItemClick(adapterPosition)
            }
        }
    }
}