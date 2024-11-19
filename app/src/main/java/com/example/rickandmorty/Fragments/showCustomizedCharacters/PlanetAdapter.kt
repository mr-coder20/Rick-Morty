package com.example.rickandmorty.Fragments.showCustomizedCharacters

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.R

// داده‌های هر سیاره
data class Location(
    val name: String,
    val residents: List<String>, // لیست آدرس URL کاراکترهای ساکن
    val type: String,
    val dimension: String
)

class PlanetAdapter : RecyclerView.Adapter<PlanetAdapter.PlanetViewHolder>(), Filterable {

    private var planetList: List<Location> = listOf()
    private var filteredList: List<Location> = listOf()

    // متد برای تنظیم داده‌های جدید
    fun submitList(planets: List<Location>?) {
        planetList = planets ?: listOf()
        filteredList = planetList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_planet_card, parent, false)
        return PlanetViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlanetViewHolder, position: Int) {
        val planet = filteredList[position]
        holder.bind(planet)
    }

    override fun getItemCount(): Int = filteredList.size

    inner class PlanetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nameText: TextView = view.findViewById(R.id.planet_name)
        private val residentsText: TextView = view.findViewById(R.id.planet_residents)
        private val favoriteButton: ImageView = view.findViewById(R.id.favorite_button)

        // متد برای پر کردن داده‌ها در کارت‌ها
        fun bind(planet: Location) {
            nameText.text = planet.name
            residentsText.text = "Residents: ${planet.residents.size}"

            favoriteButton.setOnClickListener {
                it.animate().scaleX(1.2f).scaleY(1.2f).setDuration(100).withEndAction {
                    it.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                }.start()
                saveToFavorites(planet.name)
                Toast.makeText(itemView.context, "${planet.name} added to favorites!", Toast.LENGTH_SHORT).show()
            }
        }

        // ذخیره سیاره در علاقه‌مندی‌ها
        private fun saveToFavorites(planetName: String) {
            val sharedPreferences: SharedPreferences = itemView.context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(planetName, planetName)
            editor.apply()
        }
    }

    // پیاده‌سازی قابلیت جستجو
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint.toString()
                filteredList = if (query.isEmpty()) {
                    planetList
                } else {
                    planetList.filter { it.name.contains(query, ignoreCase = true) }
                }
                return FilterResults().apply { values = filteredList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as List<Location>
                notifyDataSetChanged()
            }
        }
    }
}
