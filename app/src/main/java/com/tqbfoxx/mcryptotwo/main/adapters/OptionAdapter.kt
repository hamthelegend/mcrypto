package com.tqbfoxx.mcryptotwo.main.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tqbfoxx.mcryptotwo.R
import org.jetbrains.anko.find
import org.jetbrains.anko.imageResource

data class Option(@DrawableRes val iconResId: Int,
                  val name: String,
                  var value: String,
                  val blockOnClick: () -> Unit) {
	
}

class OptionRecyclerAdapter(val context: Context, val options: ArrayList<Option>):
		RecyclerView.Adapter<OptionRecyclerAdapter.ViewHolder>() {
	
	class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		val iconVw: AppCompatImageView = itemView.find(R.id.ic_vw)
		val nameVw: AppCompatTextView = itemView.find(R.id.name_vw)
		val valueVw: AppCompatTextView = itemView.find(R.id.value_vw)
		val optionParentVw: ConstraintLayout = itemView.find(R.id.option_parent_vw)
	}
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(parent.context)
			.inflate(R.layout.layout_option_item, parent, false)
		return ViewHolder(view)
	}
	
	override fun getItemCount(): Int = options.size
	
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		
		val currentOption = options[position]
		
		holder.apply {
			
			currentOption.apply {
				
				iconVw.imageResource = iconResId
				nameVw.text = name
				valueVw.text = value
				
				optionParentVw.setOnClickListener{
					blockOnClick()
					
					// TODO: Separate SettingsAdapter and CiphersAdapter
					
					
				}
				
			}
			
		}
	
	}
	
}