package com.blauhaus.android.redwood.app.todomvvm

import androidx.recyclerview.widget.DiffUtil

// my version:
// don't use @CodingDoug's QueryItem Abstraction
open class GenericDiffCallback: DiffUtil.ItemCallback<Todo>() {
    override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem == newItem
    }
}
