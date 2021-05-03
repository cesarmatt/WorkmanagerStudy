package com.csr.workmangerstudy.create

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.core.view.isVisible
import com.csr.workmangerstudy.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateActivity : AppCompatActivity() {

    private val viewModel: CreateViewModel by viewModel()

    private var progress: ProgressBar? = null
    private var titleInput: EditText? = null
    private var bodyInput: EditText? = null
    private var submitForm: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        titleInput = findViewById(R.id.titleInput)
        bodyInput = findViewById(R.id.bodyInput)
        submitForm = findViewById(R.id.submitForm)
        progress = findViewById(R.id.progress)

        setupViews()
        setupBindings()
    }

    private fun setupViews() {
        submitForm?.setOnClickListener {
            createPost()
            finish()
        }
    }

    private fun setupBindings() {
        viewModel.uiState.observe(this) {
            progress?.isVisible = it.loading
        }
    }

    private fun createPost() {
        val title = titleInput?.text?.trim().toString()
        val body = bodyInput?.text?.trim().toString()

        viewModel.save(title, body)
    }
}