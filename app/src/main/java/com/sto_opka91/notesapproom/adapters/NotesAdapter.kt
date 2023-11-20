package com.sto_opka91.notesapproom.adapters


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sto_opka91.notesapproom.R
import com.sto_opka91.notesapproom.databinding.ListItemBinding
import com.sto_opka91.notesapproom.models.ModelNote


class NotesAdapter( val listener: NotesItemClickListener): ListAdapter<ModelNote, NotesAdapter.NoteViewHolder>(Comparator()) {


    inner class NoteViewHolder(itemView: View, val listener: NotesItemClickListener?): RecyclerView.ViewHolder(itemView) {
        val binding = ListItemBinding.bind(itemView)
        var itemTemp: ModelNote? = null

        fun bind(item: ModelNote) = with(binding) {
            itemTemp = item
            tvTitle.text = item.title
            tvTitle.isSelected = true
            tvNote.text = item.note
            tvDate.text = item.date
            tvDate.isSelected = true
            cvMain.setCardBackgroundColor(itemView.resources.getColor(randomColor(), null))
            binding.cvMain.setOnClickListener {
                listener?.onItemClick(item)
            }
            binding.cvMain.setOnLongClickListener {
                listener?.onLongItemClicked(
                    item,
                    binding.cvMain
                )
                true
            }
        }

    }

    class Comparator : DiffUtil.ItemCallback<ModelNote>(){
        override fun areItemsTheSame(oldItem: ModelNote, newItem: ModelNote): Boolean {
            return oldItem==newItem
        }
        override fun areContentsTheSame(oldItem: ModelNote, newItem: ModelNote): Boolean {
            return oldItem==newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent,false)
        return NoteViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


   private fun randomColor(): Int{
        val seed = System.currentTimeMillis().toInt()
        val randomIndex = kotlin.random.Random(seed).nextInt(RANDOM_COLOR.size)
        return RANDOM_COLOR[randomIndex]
    }



    interface NotesItemClickListener{
        fun onItemClick(note: ModelNote)
        fun onLongItemClicked(note: ModelNote, cardView: CardView)
    }

    companion object{
         val RANDOM_COLOR = listOf(
            R.color.note_blue,
            R.color.note_braun,
            R.color.note_greenYellow,
            R.color.note_orangeYellow,
            R.color.note_pink,
            R.color.note_red,
            R.color.note_yellow
        )

    }
}