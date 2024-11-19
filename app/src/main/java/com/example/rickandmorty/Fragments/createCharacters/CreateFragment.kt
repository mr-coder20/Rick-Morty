package com.example.rickandmorty.Fragments.createCharacters

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.rickandmorty.R

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class CreateFragment : Fragment() {

    private lateinit var nameEditText: EditText
    private lateinit var imageView: ImageView
    private lateinit var speciesEditText: EditText
    private lateinit var statusEditText: EditText
    private lateinit var originEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var selectImageButton: Button

    private var imageUri: Uri? = null

    private val PICK_IMAGE_REQUEST = 1
    private val CAMERA_REQUEST = 2
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = AppDatabase.getDatabase(requireContext()) // Initialize Room Database
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create, container, false)

        nameEditText = view.findViewById(R.id.nameEditText)
        imageView = view.findViewById(R.id.imageView)
        speciesEditText = view.findViewById(R.id.speciesEditText)
        statusEditText = view.findViewById(R.id.statusEditText)
        originEditText = view.findViewById(R.id.originEditText)
        saveButton = view.findViewById(R.id.saveButton)
        selectImageButton = view.findViewById(R.id.selectImageButton)

        selectImageButton.setOnClickListener {
            showImagePickerDialog()
        }

        saveButton.setOnClickListener {
            saveCharacter()
        }

        return view
    }

    private fun showImagePickerDialog() {
        val options = arrayOf("Camera", "Gallery", "Cancel")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select Image From")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> {
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            android.Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        openCamera()
                    } else {
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST
                        )
                    }
                }

                1 -> openGallery()
                2 -> dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST)}

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    imageUri = data?.data
                    imageView.setImageURI(imageUri)
                }

                CAMERA_REQUEST -> {
                    val bitmap = data?.extras?.get("data") as Bitmap
                    imageView.setImageBitmap(bitmap)
                    // Save the bitmap to a file and get its URI
                    imageUri = saveImageToFile(bitmap)
                }
            }
        }
    }

    private fun saveImageToFile(bitmap: Bitmap): Uri? {
        val file = File(requireContext().cacheDir, "image_${System.currentTimeMillis()}.jpg")
        return try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            }
            Uri.fromFile(file) // بازگشت Uri
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    private fun saveCharacter() {
        val name = nameEditText.text.toString()
        val species = speciesEditText.text.toString()
        val status = statusEditText.text.toString()
        val origin = originEditText.text.toString()
        val imageUrl = imageUri.toString()

        if (name.isEmpty() || species.isEmpty() || status.isEmpty() || origin.isEmpty() || imageUrl.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val character = Character(name = name, imageUrl = imageUrl, species = species, status = status, origin = origin)

        CoroutineScope(Dispatchers.IO).launch {
            database.characterDao().insert(character)
            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "Character saved successfully", Toast.LENGTH_SHORT).show()
                clearFields()
            }
        }
    }

    private fun clearFields() {
        nameEditText.text.clear()
        speciesEditText.text.clear()
        statusEditText.text.clear()
        originEditText.text.clear()
        imageView.setImageResource(0)
        imageUri = null
    }
}