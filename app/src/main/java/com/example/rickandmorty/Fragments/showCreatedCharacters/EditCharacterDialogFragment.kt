package com.example.rickandmorty.Fragments.showCreatedCharacters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.rickandmorty.R
import com.example.rickandmorty.Fragments.createCharacters.Character

class EditCharacterDialogFragment(
    private val character: Character,
    private val onCharacterUpdated: (Character) -> Unit // Lambda برای به‌روزرسانی شخصیت
) : DialogFragment() {

    private lateinit var editTextName: EditText
    private lateinit var editTextSpecies: EditText
    private lateinit var editTextStatus: EditText
    private lateinit var buttonSave: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_character_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextName = view.findViewById(R.id.editTextName)
        editTextSpecies = view.findViewById(R.id.editTextSpecies)
        editTextStatus = view.findViewById(R.id.editTextStatus)
        buttonSave = view.findViewById(R.id.buttonSave)

        // پر کردن فیلدها با داده‌های شخصیت
        editTextName.setText(character.name)
        editTextSpecies.setText(character.species)
        editTextStatus.setText(character.status)

        // تنظیم کلیک برای دکمه ذخیره
        buttonSave.setOnClickListener {
            // اعتبارسنجی فیلدها
            if (editTextName.text.isEmpty() || editTextSpecies.text.isEmpty() || editTextStatus.text.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedCharacter = character.copy(
                name = editTextName.text.toString(),
                species = editTextSpecies.text.toString(),
                status = editTextStatus.text.toString()
            )
            onCharacterUpdated(updatedCharacter) // فراخوانی Lambda برای به‌روزرسانی
            dismiss() // بستن دیالوگ
        }
    }
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT) //

    }
}
