package com.tqbfoxx.mcryptotwo.main.adapters

import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.tqbfoxx.mcryptotwo.R
import com.tqbfoxx.mcryptotwo.extensions.copy
import com.tqbfoxx.mcryptotwo.extensions.getColorCompat
import com.tqbfoxx.mcryptotwo.extensions.setTintCompat
import org.jetbrains.anko.find
import org.jetbrains.anko.textColor
import org.jetbrains.anko.toast

data class Message(val type: Type, val encryptedMessage: String, val decryptedMessage: String) {
	enum class Type {
		RECEIVED, SENT
	}
}

class MessageRecyclerAdapter
	(private val context: Context,
	 private val messages: ArrayList<Message>,
	 private val clipboardManager: ClipboardManager):
	RecyclerView.Adapter<MessageRecyclerAdapter.ViewHolder>() {
	
	class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
		// the text views that holds the encrypted message and is ellipsized
		val encryptedMessageTxtVw: TextView = itemView.find(R.id.encrypted_message_txt_vw)
		
		// the button that represents the ability of the card view to be pressed to be copied
		val copyButton: ImageView = itemView.find(R.id.copy_button)
		
		// the decrypted message
		// shown as whole
		val decryptedMessageTxtVw: TextView = itemView.find(R.id.decrypted_message_txt_vw)
		
		// the cardView that holds everything in place
		val cardView: CardView = itemView.find(R.id.card_view)
		
	}
	
	// saves the last position index for animation
	var lastPosition: Int = 0
	
	// the layout parameters of the cardView of the received message to position it to left
	private val receivedParams =
		RelativeLayout.LayoutParams(
			/*width*/ RelativeLayout.LayoutParams.WRAP_CONTENT,
			/*height*/ RelativeLayout.LayoutParams.WRAP_CONTENT
		).apply {
			context.resources.apply {
				marginStart = getDimension(R.dimen.margin).toInt()
				marginEnd = getDimension(R.dimen.message_indention).toInt()
				topMargin = getDimension(R.dimen.margin_half).toInt()
				bottomMargin = getDimension(R.dimen.margin_half).toInt()
				addRule(RelativeLayout.ALIGN_PARENT_START)
			}
		}
	
	// the layout parameters of the cardView of the sent message to position it to right
	private val sentParams =
		RelativeLayout.LayoutParams(
			/*width*/ RelativeLayout.LayoutParams.WRAP_CONTENT,
			/*height*/ RelativeLayout.LayoutParams.WRAP_CONTENT
		).apply {
			context.resources.apply {
				marginStart = getDimension(R.dimen.message_indention).toInt()
				marginEnd = getDimension(R.dimen.margin).toInt()
				topMargin = getDimension(R.dimen.margin_half).toInt()
				bottomMargin = getDimension(R.dimen.margin_half).toInt()
				addRule(RelativeLayout.ALIGN_PARENT_END)
			}
		}
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		
		// inflates the view that will contain all the message content
		val view = LayoutInflater.from(parent.context)
			.inflate(R.layout.layout_message_bubble, parent, false)
		return ViewHolder(view)
		
	}
	
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		
		// the current message being set up
		val currentMessage: Message = messages[position]
		
		// styles the message card depending if it's "SENT" or "RECEIVED"
		holder.apply {
			when (currentMessage.type) {
				Message.Type.RECEIVED -> {
					encryptedMessageTxtVw.textColor =
						context.getColorCompat(R.color.received_text_color)
					copyButton.setTintCompat(context.getColorCompat(R.color.received_text_color))
					decryptedMessageTxtVw.textColor =
						context.getColorCompat(R.color.received_text_color)
					cardView.setCardBackgroundColor(
						context.getColorCompat(R.color.received_card_color))
					cardView.layoutParams = receivedParams }
				
				Message.Type.SENT -> {
					encryptedMessageTxtVw.textColor =
						context.getColorCompat(R.color.sent_text_color)
					copyButton.setTintCompat(context.getColorCompat(R.color.sent_text_color))
					decryptedMessageTxtVw.textColor =
						context.getColorCompat(R.color.sent_text_color)
					cardView.setCardBackgroundColor(
						context.getColorCompat(R.color.sent_card_color))
					cardView.layoutParams = sentParams }
			}
			
			// sets the encrypted and decrypted text
			encryptedMessageTxtVw.text = currentMessage.encryptedMessage
			decryptedMessageTxtVw.text = currentMessage.decryptedMessage
			
			// copies the text (encrypted or decrypted per Message.Type)
			cardView.setOnClickListener {
				when (currentMessage.type) {
					Message.Type.RECEIVED -> currentMessage.copyMessage()
					Message.Type.SENT -> currentMessage.copyMessage()
				}
			}
			
			// animates the entrance of the new message
			itemView.setAnimation(position)
			
		}
		
		
	}
	
	override fun getItemCount() = messages.size
	
	/**
	 * Copies the text (encrypted or decrypted per type) of the message
	 */
	private fun Message.copyMessage() {
		
		clipboardManager.copy(
			when (type) {
				Message.Type.RECEIVED -> decryptedMessage
				Message.Type.SENT -> encryptedMessage
			}
		)
		
		context.toast(
			when (type) {
				Message.Type.RECEIVED -> context.getString(
					R.string.copied_decrypted_text
				)
				Message.Type.SENT -> context.getString(
					R.string.confirmation_copied_encrypted_text
				)
			})
		
	}
	
	/**
	 * Animates the entrance of new messages
	 */
	private fun View.setAnimation(position: Int) {
		// If the bound view wasn't previously displayed on screen, it's animated
		if (position > lastPosition) {
			val animation = AnimationUtils.loadAnimation(context,
				R.anim.slide_in_bottom
			)
			this.startAnimation(animation)
			lastPosition = position
		}
	}
	
}
