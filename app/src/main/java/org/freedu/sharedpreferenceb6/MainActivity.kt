package org.freedu.sharedpreferenceb6

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.freedu.sharedpreferenceb6.databinding.ActivityMainBinding
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val PREFS_NAME = "my_prefs"
    private val KEY_NAME = "key_name"
    private val KEY_NAME2 = "key_name2"

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load previously saved names & draft
        loadSavedNames()

        // Save as draft whenever text changes
        binding.messageInputET.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                saveDraft(KEY_NAME, s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


        // Save button still works for explicit save
        binding.sendBtn.setOnClickListener {
            val name = binding.messageInputET.text.toString().trim()
            val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit {
                putString(KEY_NAME, name)
            }

            val savedFirst = prefs.getString(KEY_NAME, "")

            binding.chatHeaderTV.text = "Saved: ${savedFirst}"
            Toast.makeText(this, "Names saved", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveDraft(key: String, value: String) {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit {
            putString(key, value)
        }
    }

    private fun loadSavedNames() {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedFirst = prefs.getString(KEY_NAME, "")
        val savedSecond = prefs.getString(KEY_NAME2, "")

        // Load into EditTexts as draft
        binding.messageInputET .setText(savedFirst)

        // Show saved values in TextView
        if (!savedFirst.isNullOrEmpty() || !savedSecond.isNullOrEmpty()) {
            binding.messageListView.text = "Saved: ${savedFirst ?: "(none)"}"
        } else {
            binding.messageListView.text = "Saved: (none)"
        }
    }
}