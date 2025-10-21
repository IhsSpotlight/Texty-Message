package org.freedu.sharedpreferenceb6

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import org.freedu.sharedpreferenceb6.databinding.ActivityMainBinding
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    // IMPORTANT: Assuming the generated layout XML is mapped to ActivityMainBinding
    private lateinit var binding: ActivityMainBinding
    private val PREFS_NAME = "chat_prefs"
    private val KEY_DRAFT_MESSAGE = "key_draft_message"
    private val KEY_LAST_SENT = "key_last_sent_message"

    // Mock data for the ListView (replace with a real message model in a full app)
    private lateinit var messageAdapter: ArrayAdapter<String>
    private val messageList = ArrayList<String>()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Setup ListView Adapter
        // In a real chat app, you'd use a custom Adapter to handle message bubbles, sender, etc.
        messageAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, messageList)
        binding.messageListView.adapter = messageAdapter

        // Load previously saved draft and last sent message
        loadSavedData()

        // 2. Draft Saving (Saves input text to SharedPreferences as the user types)
        binding.messageInputET.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                saveDraft(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // 3. Send Button Logic (Saves and displays the message, then clears the input field)
        binding.sendBtn.setOnClickListener {
            val message = binding.messageInputET.text.toString().trim()

            if (message.isNotEmpty()) {
                // a. Add message to the mock list (simulating sending)
                messageList.add("You: $message")
                messageAdapter.notifyDataSetChanged()
                binding.messageListView.setSelection(messageAdapter.count - 1) // Scroll to bottom

                // b. Save the sent message to SharedPreferences (for demonstration)
                saveLastSent(message)

                // c. Clear input field and clear the draft in SharedPreferences
                binding.messageInputET.setText("")
                saveDraft("")

                Toast.makeText(this, "Message Sent and Saved!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Message cannot be empty.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveDraft(value: String) {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit {
            putString(KEY_DRAFT_MESSAGE, value)
        }
    }

    private fun saveLastSent(value: String) {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit {
            putString(KEY_LAST_SENT, value)
        }
    }

    private fun loadSavedData() {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedDraft = prefs.getString(KEY_DRAFT_MESSAGE, "")
        val lastSent = prefs.getString(KEY_LAST_SENT, "N/A")

        // Load the draft message into the EditText
        binding.messageInputET.setText(savedDraft)

        // Update the header to show the last message sent (simulating a useful shared preference load)
        binding.chatHeaderTV.text = "Chat Messenger (Last Sent: $lastSent)"

        // If a draft exists, tell the user (UX enhancement)
        if (!savedDraft.isNullOrEmpty()) {
            Toast.makeText(this, "Draft loaded: $savedDraft", Toast.LENGTH_LONG).show()
        }
    }
}
