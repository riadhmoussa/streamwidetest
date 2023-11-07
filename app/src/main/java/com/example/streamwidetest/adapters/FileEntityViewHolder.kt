package com.example.streamwidetest.adapters

import androidx.recyclerview.widget.RecyclerView
import com.example.streamwidetest.databinding.ListItemFileEntityBinding
import com.example.streamwidetest.model.FileEntity

/**
 * ViewHolder class for displaying a single FileEntity item in a RecyclerView.
 *
 * @param binding The data binding object that holds the view references.
 * @param clickListener The click listener to handle item click events.
 */
class FileEntityViewHolder(
    private val binding: ListItemFileEntityBinding,
    private val clickListener: FileEntityClickListener
) : RecyclerView.ViewHolder(binding.root) {

    /**
     * Binds a FileEntity to the ViewHolder, updating the view with the file's information.
     *
     * @param fileEntity The FileEntity to display.
     */
    fun bind(fileEntity: FileEntity) {
        binding.fileName.text = fileEntity.fileName

        // Set a click listener on the ViewHolder to handle item click events
        binding.root.setOnClickListener {
            clickListener.onItemClick(fileEntity)
        }
    }
}
