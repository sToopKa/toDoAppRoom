package com.sto_opka91.notesapproom

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.room.RoomDatabase
import com.sto_opka91.notesapproom.activites.AddNote
import com.sto_opka91.notesapproom.adapters.NotesAdapter
import com.sto_opka91.notesapproom.database.NoteDatabase
import com.sto_opka91.notesapproom.databinding.ActivityMainBinding
import com.sto_opka91.notesapproom.databinding.ListItemBinding
import com.sto_opka91.notesapproom.models.ModelNote
import com.sto_opka91.notesapproom.models.NoteViewModel

class MainActivity : AppCompatActivity(), NotesAdapter.NotesItemClickListener, PopupMenu.OnMenuItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: NoteDatabase
    lateinit var viewModel: NoteViewModel
    lateinit var adapter: NotesAdapter
    lateinit var selectedNote: ModelNote


    private val updateNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == Activity.RESULT_OK){
            val note = result.data?.getSerializableExtra("note") as? ModelNote
            if(note!=null){
                viewModel.updateNote(note)
        }
    }}

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
        viewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)
        viewModel.allNotes.observe(this) { list ->
            list?.let {
                adapter.submitList(list)
            }
        }
        binding.sView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    val filteredList = viewModel.allNotes.value?.filter { note ->
                        note.title!!.contains(newText, ignoreCase = true) ||
                                note.note!!.contains(newText, ignoreCase = true)
                    }
                    adapter.submitList(filteredList)
                }
                return true
            }
        })
        database = NoteDatabase.getDatabase(this)
        super.onCreate(savedInstanceState)
    }


    private fun initUI() {
        binding.rcView.setHasFixedSize(true)
        binding.rcView.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        adapter = NotesAdapter(this)
        binding.rcView.adapter = adapter

        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
            if(result.resultCode==Activity.RESULT_OK){
                val note = result.data?.getSerializableExtra("note") as? ModelNote
                if(note!=null){
                    viewModel.insertNote(note)
                }
            }
        }
        binding.fabAdd.setOnClickListener{
            val intent = Intent(this, AddNote::class.java)
            getContent.launch(intent)
        }

    }

    override fun onItemClick(note: ModelNote) {
        val intent = Intent(this@MainActivity, AddNote::class.java)
        intent.putExtra("current_note", note)
        updateNote.launch(intent)
    }

    override fun onLongItemClicked(note: ModelNote, cardView: CardView) {
        selectedNote = note
        popupDisplay(cardView)
    }

    private fun popupDisplay(cardView: CardView) {
        val popup = PopupMenu(this, cardView)
        popup.setOnMenuItemClickListener(this@MainActivity,)
        popup.inflate(R.menu.pop_up_menu)
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.delete_note){
            viewModel.deleteNote(selectedNote)
            return true
        }
        return false
    }
}