package com.sto_opka91.notesapproom.activites

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.sto_opka91.notesapproom.databinding.ActivityAddNoteBinding
import com.sto_opka91.notesapproom.models.ModelNote
import java.text.SimpleDateFormat
import java.util.Date


class AddNote : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var newNote: ModelNote
    private lateinit var oldNote: ModelNote
    var isUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        try {
            oldNote = intent.getSerializableExtra("current_note") as ModelNote
            binding.editTitle.setText(oldNote.title)
            binding.editNote.setText(oldNote.note)
            isUpdate = true
        }catch(e:Exception){
            e.printStackTrace()
        }
        binding.imDone.setOnClickListener {
            val title = binding.editTitle.text.toString()
            val note_desc = binding.editNote.text.toString()
            if(title.isNotEmpty() || note_desc.isNotEmpty()){
                val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm a")

                if(isUpdate){
                    newNote = ModelNote(
                        oldNote.id,
                        title,
                        note_desc,
                        formatter.format(Date())
                    )
                }else{
                    newNote = ModelNote(
                        null,
                        title,
                        note_desc,
                        formatter.format(Date())
                    )

                }
                val intent = Intent()
                intent.putExtra("note", newNote)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }else{
                Toast.makeText(this@AddNote, "Please Enter some Data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }
        binding.imBack.setOnClickListener {
            onBackPressed()
        }
    }
}