package com.example.rickandmorty.Fragments.showCreatedCharacters

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.rickandmorty.Fragments.createCharacters.Character
import com.example.rickandmorty.R

// اینترفیس برای مدیریت رویدادها
interface OnItemActionListener {
    fun onEdit(position: Int, character: Character)
    fun onDelete(position: Int, character: Character)
}

class CharacterAdapter(
    private var characters: List<Character>,
    private val listener: OnItemActionListener // پذیرش اینترفیس
) : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    class CharacterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.characterName) // استفاده از شناسه جدید
        val speciesTextView: TextView = view.findViewById(R.id.characterSpecies) // استفاده از شناسه جدید
        val statusTextView: TextView = view.findViewById(R.id.characterStatus) // استفاده از شناسه جدید
        val imageView: ImageView = view.findViewById(R.id.characterImage) // استفاده از شناسه جدید
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_character, parent, false)
        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = characters[position]
        holder.nameTextView.text = character.name
        holder.speciesTextView.text = character.species
        holder.statusTextView.text = character.status

        // تنظیم رنگ متن وضعیت بر اساس وضعیت شخصیت
        if (character.status.equals("Alive", ignoreCase = true)) {
            holder.statusTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.md_green_A400)) // سبز برای Alive
        } else if (character.status.equals("Dead", ignoreCase = true)) {
            holder.statusTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.md_red_A200)) // قرمز برای Dead
        } else {
            holder.statusTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.md_green_A400)) // رنگ پیش‌فرض برای وضعیت‌های دیگر
        }

        // بارگذاری تصویر با استفاده از Glide
        Glide.with(holder.itemView.context)
            .load(character.imageUrl) // URL تصویر کاراکتر
            .placeholder(R.drawable.ic_person) // تصویر پیش‌فرض در هنگام بارگذاری
            .error(R.drawable.ic_launcher_foreground) // تصویر در صورت بروز خطا
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.e("Glide", "Load failed", e)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            })
            .into(holder.imageView)

        // تنظیم Listener برای ویرایش
        holder.itemView.setOnClickListener {
            listener.onEdit(position, character)
        }

        // تنظیم Listener برای حذف
        holder.itemView.setOnLongClickListener {
            listener.onDelete(position, character)
            true
        }
    }

    override fun getItemCount(): Int = characters.size

    // متد برای به‌روزرسانی لیست شخصیت‌ها
    fun updateCharacters(newCharacters: List<Character>) {
        this.characters = newCharacters
        notifyDataSetChanged() // به‌روزرسانی RecyclerView
    }

    // متد برای حذف یک شخصیت از لیست
    fun removeCharacter(position: Int) {
        if (position < characters.size) {
            val updatedList = characters.toMutableList()
            updatedList.removeAt(position)
            characters = updatedList
            notifyItemRemoved(position)
        }
    }

    // متد برای اضافه کردن یک شخصیت به لیست
    fun addCharacter(character: Character) {
        val updatedList = characters.toMutableList()
        updatedList.add(character)
        characters = updatedList
        notifyItemInserted(updatedList.size - 1)
    }
}