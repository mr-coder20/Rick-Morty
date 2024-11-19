package com.example.rickandmorty.Fragments.showApiCharacters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.R

class ShowFragment : Fragment() {

    private val characterViewModel: CharacterViewModel by viewModels()
    private lateinit var characterAdapter: CharacterAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var emptyStateView: View // To show when no characters are available

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_show, container, false)

        // Find the views
        recyclerView = view.findViewById(R.id.recyclerView)
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar)
        emptyStateView = view.findViewById(R.id.emptyStateView) // Assume this is a TextView or layout to show "No Data"

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        characterAdapter = CharacterAdapter { character ->
            // Handle click event if needed
            Toast.makeText(context, "Clicked on ${character.name}", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = characterAdapter
        recyclerView.setHasFixedSize(true) // Set this if all items have the same size

        // Observe ViewModel data
        characterViewModel.characters.observe(viewLifecycleOwner, Observer { characters ->
            if (characters.isEmpty()) {
                // Show empty state view if no characters are returned
                emptyStateView.visibility = View.VISIBLE
            } else {
                // Hide empty state and show the list
                emptyStateView.visibility = View.GONE
                characterAdapter.submitList(characters)
            }
            loadingProgressBar.visibility = View.GONE // Hide progress bar when data is loaded
        })

        characterViewModel.error.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                loadingProgressBar.visibility = View.GONE // Hide progress bar if there's an error
                emptyStateView.visibility = View.VISIBLE // Optionally show an error state here
            }
        })

        characterViewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                loadingProgressBar.visibility = View.VISIBLE // Show progress bar when loading
            } else {
                loadingProgressBar.visibility = View.GONE // Hide progress bar when not loading
            }
        })

        // Fetch characters from ViewModel
        characterViewModel.fetchNextPage() // Now, fetching data page by page

        // Set up scrolling listener to load more characters when reaching the end of the list
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (!recyclerView.canScrollVertically(1)) {
                    // If we have reached the end of the list, load the next page
                    characterViewModel.fetchNextPage()
                }
            }
        })

        return view
    }
}
