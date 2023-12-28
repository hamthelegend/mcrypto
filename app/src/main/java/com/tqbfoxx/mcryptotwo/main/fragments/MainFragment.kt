package com.tqbfoxx.mcryptotwo.main.fragments


import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tqbfoxx.mcryptotwo.R
import com.tqbfoxx.mcryptotwo.databinding.FragmentMainBinding
import com.tqbfoxx.mcryptotwo.encryption.*
import com.tqbfoxx.mcryptotwo.extensions.copy
import com.tqbfoxx.mcryptotwo.extensions.setTooltipCompat
import com.tqbfoxx.mcryptotwo.extensions.textString
import com.tqbfoxx.mcryptotwo.main.MainActivity
import com.tqbfoxx.mcryptotwo.main.adapters.Message
import com.tqbfoxx.mcryptotwo.main.adapters.MessageRecyclerAdapter
import org.jetbrains.anko.support.v4.toast

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {

    // Binding Object
    private lateinit var binding: FragmentMainBinding

    // Other Objects
    private lateinit var clipboardManager: ClipboardManager
    private lateinit var messages: ArrayList<Message>

    // The parent activity
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // inflates the layout binding for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false)

        // sets the objects up
        initObjects()

        // sets the OnClickListeners
        setOnClickListeners()

        // sets the tooltips
        setTooltips()

        // returns the layout
        return binding.root

    }

    override fun onResume() {
        super.onResume()

        // checks if the cipher is available and opens the AddCipherFragment ig not
        // was put in onResume() to prevent user from accessing MainFragment without a cipher
        checkIfCipherIsAvailable()

        // shows the bottomAppBar as other fragments might've hid it
        mainActivity.showMessageAppBar()

        // hides the FAB as other fragments might've shown it
        mainActivity.hideFAB()

//        // refreshes the RecyclerView
//        refreshRecycler()

    }

    override fun onPause() {
        super.onPause()

        // saves the messages history to SharedPreferences
        context!!.conversation = messages

    }




    /**
     * Initializes the objects needed in the activity.
     */
    private fun initObjects() {
        // Other Objects
        clipboardManager = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        // initializes the message recycler view
        initMessagesRecycler()

        // gets the parent activity for references
        mainActivity = activity as MainActivity
    }

    /**
     * Sets up the OnClickListeners of clickable elements of this fragment.
     */
    private fun setOnClickListeners() {
        mainActivity.binding.apply {

            // show nav drawer
            // on hamburger menu icon click
            navDrawerButton.setOnClickListener {
                showNavDrawer()
            }

            // pastes from clipboardManager
            // on smartPaste icon click
            pasteButton.setOnClickListener {
                smartPaste()
            }

            // encrypts the message
            // on lock icon click
            encryptButton.setOnClickListener {
                encryptAndClear()
            }

        }
    }

    /**
     * Sets the tooltip texts of the clickable elements of this fragment.
     */
    private fun setTooltips() {
        mainActivity.binding.apply {

            navDrawerButton.setTooltipCompat(getString(R.string.tooltip_nav_drawer_button))
            pasteButton.setTooltipCompat(getString(R.string.tooltip_paste_button))
            encryptButton.setTooltipCompat(getString(R.string.tooltip_encrypt_button))

        }
    }



    /**
     * Sets up the messagesRecycler
     */
    private fun initMessagesRecycler() {
        messages = context!!.conversation
        binding.messagesRecycler.adapter =
                MessageRecyclerAdapter(
                        context!!,
                        messages,
                        clipboardManager
                )
    }



    /**
     * Opens the AddCipherFragment in case there are no active ciphers
     */
    private fun checkIfCipherIsAvailable() {
        context?.apply {
            if (ciphers.isEmpty()) {
                this@MainFragment.findNavController()
                        .navigate(MainFragmentDirections.actionMainFragmentToAddCipherFragment())
                toast(getString(R.string.guide_assert_cipher_need))
            }
        }
    }



    /**
     * Shows the Navigation Drawer.
     */
    private fun showNavDrawer() {
        mainActivity.showNavDrawer()
    }

    /**
     * Decrypts the text on clipboard if it's encrypted or pastes it to the inputField if it's not.
     */
    private fun smartPaste() {
        val pasteData = getPasteData()

        if (pasteData.isEncrypted()) {
            pasteData.decryptAndAdd()
        } else {
            pasteData.basicPaste()
        }

    }

    /**
     * Encrypts the text in the input field and clears the input field.
     */
    private fun encryptAndClear() {
        // encrypts and adds the new SENT Message if the input field isn't empty
        mainActivity.binding.inputField.textString.apply {
            if (this.isNotEmpty()) encryptAndAdd() else toast(getString(R.string.error_empty_input_field))
        }

        // clears the input field
        mainActivity.binding.inputField.textString = ""
    }



    /**
     * Gets the primary clip's text from the clipboard
     */
    private fun getPasteData(): String {
        // gets the primary clip from the clipboardManager
        val clipData = clipboardManager.primaryClip
        val clipCount = clipData?.itemCount ?: 0

        // if clipData is not empty
        if (clipCount > 0) {
            // gets source text.
            val item = clipData?.getItemAt(0)
            return item?.text.toString()
        } else {
            return ""
        }
    }

    /**
     * Decrypts the given String and adds the message as Message.Type.RECEIVED
     */
    private fun String.decryptAndAdd() {
        val decryptedMessage = context!!.decrypt(this)
        Message(
                Message.Type.RECEIVED,
                this,
                decryptedMessage
        ).addMessageToList()
    }

    /**
     * Paste the String directly to the input field
     */
    private fun String.basicPaste() {
        when (this) {
            "" -> toast(getString(R.string.error_empty_clipboard))
            else -> mainActivity.binding.inputField.textString = this
        }
    }



    /**
     * Adds the message to list
     */
    private fun Message.addMessageToList() {
        // adds the message
        messages.add(this)

        // refreshes the RecyclerView
        refreshRecycler()

        (activity as MainActivity).collapseAppBar()

    }

    /**
     * Refreshes the RecyclerView
     */
    private fun refreshRecycler() {
        binding.messagesRecycler.apply {
            // updates messagesRecycler
            adapter?.notifyDataSetChanged()
            // scrolls all the way to the bottom
            post { smoothScrollToPosition(adapter?.getItemCount()?.minus(1)!!) }
        }
    }

    /**
     * Encrypts the given String and adds the message as Message.Type.SENT
     */
    private fun String.encryptAndAdd() {
        val encryptedMessage = context!!.encrypt(this)
        Message(
                Message.Type.SENT,
                encryptedMessage,
                this
        ).addMessageToList()
        clipboardManager.copy(encryptedMessage)
        toast(getString(R.string.confirmation_copied_encrypted_text))
    }


}
