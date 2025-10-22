package org.freedu.sharedpreferenceb6

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageAdapter(private val messages: List<String>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    // Describes an item view and metadata about its place within the RecyclerView.
    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // We'll use the default Android simple list item which has a TextView with id 'text1'
        val messageTextView: TextView = itemView.findViewById(android.R.id.text1)
    }

    // Creates new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            // We can reuse the same simple layout that ArrayAdapter used
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return MessageViewHolder(view)
    }

    // Replaces the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        holder.messageTextView.text = messages[position]
    }

    // Returns the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = messages.size
}
