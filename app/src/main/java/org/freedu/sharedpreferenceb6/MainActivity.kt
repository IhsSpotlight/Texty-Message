package org.freedu.sharedpreferenceb6

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager // Import LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.freedu.sharedpreferenceb6.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val PREFS_NAME = "chat_prefs"
    private val KEY_DRAFT_MESSAGE = "key_draft_message"
    private val KEY_LAST_SENT = "key_last_sent_message"
    private val KEY_MESSAGE_LIST = "key_message_list"

    // Use your new custom adapter
    private lateinit var messageAdapter: MessageAdapter
    private val messageList = ArrayList<String>()
    private val gson = Gson()

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1) Load message list (history) from prefs into messageList
        messageList.clear()
        messageList.addAll(loadMessageList())

        // 2) Setup RecyclerView
        messageAdapter = MessageAdapter(messageList)// Change this line:
        binding.messageListView.adapter = messageAdapter
        binding.messageListView.layoutManager = LinearLayoutManager(this)
        binding.messageListView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // If we loaded messages, refresh adapter and scroll to bottom
        messageAdapter.notifyDataSetChanged()
        if (messageAdapter.itemCount > 0) {
            binding.messageListView.scrollToPosition(messageAdapter.itemCount - 1)
        }

        // 3) Load draft text and last sent summary
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedDraft = prefs.getString(KEY_DRAFT_MESSAGE, "") ?: ""
        val lastSent = prefs.getString(KEY_LAST_SENT, "N/A") ?: "N/A"
        // Restore draft into EditText before attaching TextWatcher
        binding.messageInputET.setText(savedDraft)

        if (savedDraft.isNotEmpty()) {
            Toast.makeText(this, "Draft loaded: $savedDraft", Toast.LENGTH_SHORT).show()
        }

        // 4) Attach TextWatcher to auto-save drafts
        binding.messageInputET.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                saveDraft(s?.toString() ?: "")
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // 5) Send button logic
        binding.sendBtn.setOnClickListener {
            val message = binding.messageInputET.text.toString().trim()
            if (message.isNotEmpty()) {
                messageList.add("You: $message")
                // Notify the adapter that an item was inserted at the end
                messageAdapter.notifyItemInserted(messageList.size - 1)
                // Scroll to the new message
                binding.messageListView.scrollToPosition(messageAdapter.itemCount - 1)

                // Save last sent and full message list
                saveLastSent(message)
                saveMessageList()

                // Clear input and draft
                binding.messageInputET.setText("")
                saveDraft("")

                // Update header to show last sent

                Toast.makeText(this, "Message Sent & Saved!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Message cannot be empty.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Persist the messageList as JSON in SharedPreferences
    private fun saveMessageList() {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = gson.toJson(messageList)
        prefs.edit {
            putString(KEY_MESSAGE_LIST, json)
        }
    }

    // Load the saved message list (returns an empty list if none saved)
    private fun loadMessageList(): ArrayList<String> {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_MESSAGE_LIST, null)
        return if (!json.isNullOrEmpty()) {
            val type = object : TypeToken<ArrayList<String>>() {}.type
            gson.fromJson(json, type)
        } else {
            ArrayList()
        }
    }

    // Save draft value (current input text)
    private fun saveDraft(value: String) {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit {
            putString(KEY_DRAFT_MESSAGE, value)
        }
    }

    // Save last sent message (simple helper)
    private fun saveLastSent(value: String) {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit {
            putString(KEY_LAST_SENT, value)
        }
    }

    // Make sure we persist the message list (and optionally the draft) when the activity is paused
    override fun onPause() {
        super.onPause()
        // Save the latest message list and the current draft so nothing is lost if the app is backgrounded
        saveMessageList()
        saveDraft(binding.messageInputET.text.toString())
    }
}
