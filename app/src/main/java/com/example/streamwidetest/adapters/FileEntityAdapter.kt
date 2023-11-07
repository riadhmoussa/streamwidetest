package com.example.streamwidetest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.streamwidetest.databinding.ListItemFileEntityBinding
import com.example.streamwidetest.model.FileEntity

interface FileEntityClickListener {
    fun onItemClick(file: FileEntity)
}

/**
 * A RecyclerView Adapter for displaying a list of FileEntity items.
 *
 * @param fileEntities The initial list of FileEntity items to be displayed.
 * @param clickListener The click listener to handle item click events.
 */
class FileEntityAdapter(
    private var fileEntities: List<FileEntity>,
    private val clickListener: FileEntityClickListener
) : RecyclerView.Adapter<FileEntityViewHolder>() {

    /**
     * Inflates the layout for a FileEntity item and creates a ViewHolder.
     *
     * @param parent The parent view group.
     * @param viewType The type of view to be created.
     * @return A FileEntityViewHolder for the inflated view.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileEntityViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemFileEntityBinding.inflate(layoutInflater, parent, false)
        return FileEntityViewHolder(binding, clickListener)
    }

    /**
     * Binds a FileEntity to a ViewHolder, updating the view with the file's information.
     *
     * @param holder The ViewHolder to bind the data to.
     * @param position The position of the item in the list.
     */
    override fun onBindViewHolder(holder: FileEntityViewHolder, position: Int) {
        val fileEntity = fileEntities[position]
        holder.bind(fileEntity)
    }

    /**
     * Returns the total number of items in the list.
     *
     * @return The number of FileEntity items in the list.
     */
    override fun getItemCount(): Int {
        return fileEntities.size
    }

    /**
     * Updates the list of FileEntity items and notifies the adapter of the data change.
     *
     * @param newFileEntities The new list of FileEntity items to replace the current data.
     */
    fun updateData(newFileEntities: List<FileEntity>) {
        fileEntities = newFileEntities
        notifyDataSetChanged()
    }
}
