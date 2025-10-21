package org.freedu.sharedpreferenceb6

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.freedu.sharedpreferenceb6.databinding.ActivityMainBinding
import androidx.core.content.edit

class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding
    private  val PREFS_NAME = "my_prefs"
    private  val KEY_NAME = "key_name"
    private  val KEY_NAME2 = "key_name2"

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveBtn.setOnClickListener {
            val name = binding.nameET.text.toString().trim()
            val name2 = binding.nameETtwo.text.toString().trim()
            if (name.isNotEmpty() && name2.isNotEmpty())
            {
                val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                prefs.edit {
                    putString(KEY_NAME, name)
                    putString(KEY_NAME2, name2)
                }
                val saveName = prefs.getString(KEY_NAME,null)
                val saveName2 = prefs.getString(KEY_NAME2,null)
                binding.savedNameTV.text = "Saved: $saveName $saveName2 "
            }
        }

    }


}