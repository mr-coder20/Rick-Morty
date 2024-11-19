package com.example.rickandmorty.Fragments.showCustomizedCharacters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.rickandmorty.R
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Defining the data models for the API response
data class Character2(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: Origin2,
    val location: Location2,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String
)

data class Origin2(
    val name: String,
    val url: String
)

data class Location2(
    val name: String,
    val url: String
)

class ShowCustomizedFragment : Fragment() {

    private lateinit var tvQuestion: TextView
    private lateinit var rgOptions: RadioGroup
    private lateinit var btnSubmit: Button
    private lateinit var tvResult: TextView
    private lateinit var ivCharacterImage: ImageView
    private lateinit var btnNextQuestion: Button

    private var correctAnswer: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_customized, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvQuestion = view.findViewById(R.id.tvQuestion)
        rgOptions = view.findViewById(R.id.rgOptions)
        btnSubmit = view.findViewById(R.id.btnSubmit)
        tvResult = view.findViewById(R.id.tvResult)
        ivCharacterImage = view.findViewById(R.id.ivCharacterImage)
        btnNextQuestion = view.findViewById(R.id.btnNextQuestion)

        // Load the question from API
        loadQuestion()

        btnSubmit.setOnClickListener {
            checkAnswer()
        }

        btnNextQuestion.setOnClickListener {
            loadQuestion() // Load next question
            btnNextQuestion.visibility = View.GONE // Hide the "Next Question" button
            tvResult.visibility = View.GONE // Hide the result message
        }
    }

    // Function to load a random question from the API
    private fun loadQuestion() {
        val randomId = (1..826).random() // Generate a random ID
        RickAndMortyApiService2.create().getRandomCharacter(randomId).enqueue(object : Callback<Character2> {
            override fun onResponse(call: Call<Character2>, response: Response<Character2>) {
                if (response.isSuccessful) {
                    val character = response.body()
                    character?.let {
                        tvQuestion.text = "What is the species of ${character.name}?"
                        correctAnswer = character.species

                        // Load character image
                        Glide.with(requireContext())
                            .load(character.image)
                            .placeholder(R.drawable.ic_person)
                            .into(ivCharacterImage)

                        // Create options dynamically
                        val allSpecies = listOf("Human", "Alien", "Robot", "Unknown") // Example pool of species
                        val options = mutableListOf(correctAnswer)
                        options.addAll(allSpecies.filter { it != correctAnswer }.shuffled().take(3)) // Exclude correct answer
                        val finalOptions = options.shuffled()

                        // Display options in RadioGroup
                        rgOptions.removeAllViews()
                        for (option in finalOptions) {
                            val radioButton = RadioButton(requireContext())
                            radioButton.text = option
                            radioButton.id = View.generateViewId()
                            rgOptions.addView(radioButton)
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to load question", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Character2>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    // Function to check if the selected answer is correct
    private fun checkAnswer() {
        val selectedOptionId = rgOptions.checkedRadioButtonId
        if (selectedOptionId == -1) {
            Toast.makeText(requireContext(), "Please select an answer", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedOption = view?.findViewById<RadioButton>(selectedOptionId)?.text
        if (selectedOption == correctAnswer) {
            tvResult.text = "Correct! 🎉"
        } else {
            tvResult.text = "Wrong! The correct answer is $correctAnswer."
        }

        tvResult.visibility = View.VISIBLE
        btnNextQuestion.visibility = View.VISIBLE // Show the "Next Question" button
    }
}