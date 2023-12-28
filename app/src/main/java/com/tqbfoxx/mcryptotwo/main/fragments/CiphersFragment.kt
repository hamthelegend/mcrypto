package com.tqbfoxx.mcryptotwo.main.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.tqbfoxx.mcryptotwo.R
import com.tqbfoxx.mcryptotwo.databinding.FragmentAddCipherBinding
import com.tqbfoxx.mcryptotwo.databinding.FragmentCiphersBinding
import com.tqbfoxx.mcryptotwo.databinding.FragmentEditCipherBinding
import com.tqbfoxx.mcryptotwo.encryption.*
import com.tqbfoxx.mcryptotwo.extensions.textString
import com.tqbfoxx.mcryptotwo.main.MainActivity
import com.tqbfoxx.mcryptotwo.main.adapters.CipherRecyclerAdapter
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

/**
 * A simple [Fragment] subclass.
 */
class CiphersFragment : Fragment() {

    // Binding Object
    lateinit var binding: FragmentCiphersBinding

    // The parent activity
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // inflates the layout binding if this fragment
        binding = FragmentCiphersBinding.inflate(inflater, container, false)

        // initializes the objects of this fragment
        initObjects()

        // hides the bottomBar as it's really for the MainFragment
        mainActivity.hideMessageAppBar()

        // shows the addFAB to enable addition of new ciphers
        mainActivity.showFAB()

        // shows AddCipherFragment
        // on FAB click
        mainActivity.apply {
            binding.fab.setOnClickListener {

                // goes to AddCipherFragment
                this.findNavController(R.id.fragment_host)
                        .navigate(CiphersFragmentDirections
                                .actionCiphersFragmentToAddCipherFragment())

            }
        }

        // returns the layout
        return binding.root

    }

    /**
     * Initializes the objects of this fragment.
     */
    private fun initObjects() {

        // initializes the parent activity instance for future references
        mainActivity = activity as MainActivity

        // sets the adapter of the RecyclerView that shows all the ciphers
        binding.cipherRecycler.adapter =
                CipherRecyclerAdapter(context!!, context!!.ciphers)

    }


}


class EditCipherFragment : Fragment() {

    // Binding Object
    lateinit var binding: FragmentEditCipherBinding

    // The parent activity
    private lateinit var mainActivity: MainActivity

    lateinit var cipher: Cipher

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // inflates the binding layout of this fragment
        binding = FragmentEditCipherBinding.inflate(inflater, container, false)

        // initializes a copy of the parent activity for future references
        mainActivity = activity as MainActivity

        // gets the cipher to edit from the previous fragment
        cipher = EditCipherFragmentArgs.fromBundle(arguments!!).cipher

        // hides the FAB
        mainActivity.hideFAB()

        // pre-inputs the name and keyValue of the cipher to edit
        binding.apply {
            nameField.textString = cipher.name
            keyValueField.textString = cipher.keyValue
        }

        // sets the OnClickListeners
        setOnClickListeners()

        // returns the layout
        return binding.root

    }

    /**
     * Sets the OnClickListeners of this fragment
     */
    private fun setOnClickListeners() {
        binding.apply {

            context?.apply {

                deleteButton.setOnClickListener {view ->
                    // removes the cipher being edited
                    removeCipher(cipher)

                    // closes the fragment
                    view.findNavController().navigateUp()
                }

                saveButton.setOnClickListener {view ->
                    // saves the changes to the cipher being edited
                    updateCipher(newCipher)

                    // closes the fragment
                    view.findNavController().navigateUp()
                }

                saveAndSetButton.setOnClickListener {view ->
                    // saves the changes to the cipher being edited
                    updateCipher(newCipher)
                    // sets it as the active cipher
                    activeCipher = newCipher

                    // closes the fragment
                    view.findNavController().navigateUp()
                }

                // TODO: move SharedPreferences from "ciphers" to "appData" or "data" or something

            }

        }
    }

    private val newCipher: Cipher
        get() {
            val name = binding.nameField.textString
            val keyValue = binding.keyValueField.textString
            val index = cipher.index

            return Cipher(name, keyValue, index)
        }
}



class AddCipherFragment : Fragment() {

    lateinit var binding: FragmentAddCipherBinding

    lateinit var mainActivity: MainActivity

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddCipherBinding.inflate(inflater, container, false)

        mainActivity = activity as MainActivity

        mainActivity.hideMessageAppBar()

        mainActivity.hideFAB()

        context?.apply {

            binding.apply {

                saveAndSetButton.setOnClickListener { view ->
                    if (nameField.textString.isNotBlank()) {
                        if (keyValueField.textString.length == 16) {
                            val name = nameField.textString
                            val keyValue = keyValueField.textString
                            val index = ciphers.size
                            val newCipher = Cipher(name, keyValue, index)
                            ciphers = (ciphers + newCipher) as ArrayList<Cipher>

                            activeCipher = newCipher

                            view.findNavController().navigateUp()
                        } else {
                            longToast("Key value must be exactly 16 characters long.")
                        }
                    } else {
                        toast("Name shouldn't be blank.")
                    }
                }

            }

        }

        return binding.root
    }

}