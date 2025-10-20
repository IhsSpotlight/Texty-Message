package org.freedu.sharedpreferenceb6

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.freedu.sharedpreferenceb6.databinding.ActivityMainBinding
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val PREFS_NAME = "my_prefs"
    private val KEY_NAME = "key_name"

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.saveBtn.setOnClickListener {
            val name = binding.nameET.text.toString().trim()
            if (name.isNotEmpty()) {
                val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                prefs.edit { putString(KEY_NAME, name) }
                    val savedName = prefs.getString(KEY_NAME, null)
                    binding.savedNameTV.text =  "Saved: ${savedName ?: "(none)"}"

            }
        }
    }


}
