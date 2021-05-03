package com.csr.workmangerstudy.home

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkInfo
import com.csr.workmangerstudy.R
import com.csr.workmangerstudy.create.CreateActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.net.InetAddress

class HomeActivity : AppCompatActivity() {

    private var postsList: RecyclerView? = null
    private var uploadButton: Button? = null
    private var progress: ProgressBar? = null

    private val viewModel: HomeViewModel by viewModel()
    private val adapter = HomeAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        postsList = findViewById(R.id.posts)
        uploadButton = findViewById(R.id.uploadButton)
        progress = findViewById(R.id.progress)

        setupViews()
        setupBindings()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        goToCreate()
        return super.onOptionsItemSelected(item)
    }

    private fun setupBindings() {
        viewModel.posts.observe(this) {
            if (!it.isNullOrEmpty()) {
                adapter.setItems(it)
            }
        }

        viewModel.uiState.observe(this) {
            progress?.isVisible = it.loading
        }

        viewModel.progressWorkInfoItems.observe(this) { listOfWorkInfo ->
            if (listOfWorkInfo.isNullOrEmpty()) {
                return@observe
            }

            listOfWorkInfo.forEach {
                if (it.state == WorkInfo.State.SUCCEEDED) {
                    Toast.makeText(this, "Work succeded!", Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.outputWorkInfoItems.observe(this) {
            println(it.joinToString { it.toString() })
        }
    }

    private fun setupViews() {
        postsList?.let {
            it.adapter = adapter
        }

        uploadButton?.setOnClickListener {
            if (viewModel.posts.value?.isEmpty() == false) {
                it.isVisible = true
                viewModel.uploadPosts()
            } else {
                it.isVisible = false
            }
        }
    }

    private fun goToCreate() {
        val intent = Intent(this, CreateActivity::class.java)
        startActivity(intent)
    }
}