package com.csr.workmangerstudy.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.csr.workmangerstudy.R
import com.csr.workmangerstudy.data.Post

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private var items: List<Post> = listOf()

    fun setItems(items: List<Post>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val view: View): RecyclerView.ViewHolder(view) {

        private var post: Post? = null

        private val postTitle: TextView = view.findViewById(R.id.postTitle)
        private val postBody: TextView = view.findViewById(R.id.postBody)

        fun bind(post: Post) {
            this.post = post

            postTitle.text = post.postTitle
            postBody.text = post.postBody
        }
    }
}