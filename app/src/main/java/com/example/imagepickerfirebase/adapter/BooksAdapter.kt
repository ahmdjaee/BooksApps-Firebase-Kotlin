package com.example.imagepickerfirebase.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.imagepickerfirebase.R
import com.example.imagepickerfirebase.model.Books
class BooksAdapter(private var listBook: ArrayList<Books>, val context: Context) :
    RecyclerView.Adapter<BooksAdapter.MyViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener) {
        mListener = clickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setFilteredList (listBooks: ArrayList<Books>){
        this.listBook = listBooks
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pdf_row_data, parent, false)
        return MyViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: BooksAdapter.MyViewHolder, position: Int) {
        val currentItem = listBook[position]
        holder.tvTitle.text = currentItem.booksTitle
        holder.tvAuthor.text = currentItem.booksAuthor

        holder.btnDelete.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Delete Data")
                .setMessage("Are you sure want to delete this data?")
                .setPositiveButton("Yes"){_, _ ->

                }.setNegativeButton("No"){_, _ ->

                }.create().show()
        }
    }

    override fun getItemCount(): Int {
        return listBook.size
    }
    class MyViewHolder(view: View, clickListener: onItemClickListener) : ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvAuthor: TextView = view.findViewById(R.id.tvAuthor)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDeleteData)

        init {
            itemView.setOnClickListener {
                //adapterPosition berfungsi untuk mengambil posisi item ketika diklik kemudian di tampung kedalam fun onitemclick
                clickListener.onItemClick(adapterPosition)
            }
        }
    }
}