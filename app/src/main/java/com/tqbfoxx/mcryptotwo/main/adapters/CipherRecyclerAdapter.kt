package com.tqbfoxx.mcryptotwo.main.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.tqbfoxx.mcryptotwo.R
import com.tqbfoxx.mcryptotwo.encryption.Cipher
import com.tqbfoxx.mcryptotwo.encryption.activeCipher
import com.tqbfoxx.mcryptotwo.extensions.getAttributeColor
import com.tqbfoxx.mcryptotwo.main.fragments.CiphersFragmentDirections
import org.jetbrains.anko.find
import org.jetbrains.anko.textColor
import org.jetbrains.anko.textColorResource

class CipherRecyclerAdapter(val context: Context, val ciphers: ArrayList<Cipher>):
		RecyclerView.Adapter<CipherRecyclerAdapter.ViewHolder>() {
	
	class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		val nameVw: AppCompatTextView = itemView.find(R.id.name_vw)
		val valueVw: AppCompatTextView = itemView.find(R.id.value_vw)
		val parentVw: ConstraintLayout = itemView.find(R.id.option_parent_vw)
	}
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(parent.context)
			.inflate(R.layout.layout_cipher_item, parent, false)
		return ViewHolder(view)
	}
	
	override fun getItemCount(): Int = ciphers.size
	
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		
		val currentCipher = ciphers[position]
		
		holder.apply {
			
			currentCipher.apply {
				
				nameVw.text = name
				valueVw.text = keyValue
				
				parentVw.setOnClickListener{ view ->
					view.findNavController().navigate(
						CiphersFragmentDirections
						.actionCiphersFragmentToEditCipherFragment(ciphers[position]))
				}

				if (currentCipher == context.activeCipher) {
					nameVw.textColor = context.getAttributeColor(R.attr.colorPrimary)
					valueVw.textColor = context.getAttributeColor(R.attr.colorPrimary)
				}
				
			}
			
		}
	
	}
	
}