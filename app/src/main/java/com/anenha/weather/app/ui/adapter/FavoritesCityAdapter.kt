package com.anenha.weather.app.ui.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.anenha.weather.R
import com.anenha.weather.app.entity.FavoritesEntity
import com.anenha.weather.app.ui.HomeActivity

import java.util.ArrayList
import com.anenha.weather.app.entity.TodayEntity
import kotlinx.android.synthetic.main.item_favorite.view.*

/**
 * Created by ajnen on 16/10/2017.
 *
 */

class FavoritesCityAdapter(private val context: Context, private val fe: FavoritesEntity, private var editMode: Boolean) : RecyclerView.Adapter<FavoritesCityAdapter.ViewHolder>() {
    private val removePositions: MutableList<String> = ArrayList()

    val removes: List<String>
        get() = removePositions

    fun setEditMode(editMode: Boolean) {
        this.editMode = editMode
        notifyDataSetChanged()
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesCityAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_favorite, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.favoriteCheckBox.isChecked = false
        holder.itemView.favoriteCheckBox.visibility = if (editMode) View.VISIBLE else View.INVISIBLE
        val city = fe.cities[position]

        holder.itemView.favoriteItemTitle.text = city
        holder.itemView.favoriteItemContainer.setOnClickListener {
            if (editMode) {
                holder.itemView.favoriteCheckBox.isChecked = !holder.itemView.favoriteCheckBox.isChecked
            } else {
                val i = Intent(context, HomeActivity::class.java)
                i.putExtra("REFRESH_CITY", city)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                context.startActivity(i)
            }
        }

        holder.itemView.favoriteCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                removePositions.add(city)
            } else if (!removePositions.isEmpty() && removePositions.contains(city)) {
                removePositions.remove(city)
            }
        }

        val te : TodayEntity? = fe.favorite!![position].todayEntity
        holder.itemView.favoriteItemSubtitle.text = te!!.getLocal(true)
        holder.itemView.favoriteItemImage.setImageDrawable(te.image)
        holder.itemView.favoriteItemTempNow.text = te.tempNow
        holder.itemView.favoriteItemTempHighLow.text = te.tempHigh + "\n" + te.tempLow
        holder.itemView.favoriteItemCondition.text = te.condition

    }

    override fun getItemCount(): Int {
        return fe.cities.size
    }
}

