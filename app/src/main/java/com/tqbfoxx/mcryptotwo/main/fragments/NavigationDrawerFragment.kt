package com.tqbfoxx.mcryptotwo.main.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tqbfoxx.mcryptotwo.R
import com.tqbfoxx.mcryptotwo.databinding.FragmentNavDrawerBinding
import com.tqbfoxx.mcryptotwo.main.fragments.MainFragmentDirections

class NavigationDrawerFragment: BottomSheetDialogFragment() {

	lateinit var binding: FragmentNavDrawerBinding

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		binding = FragmentNavDrawerBinding.inflate(inflater, container, false)

		binding.navigationView.setupWithNavController(this.findNavController())

//		binding.navigationView.setNavigationItemSelectedListener { menuItem ->
//
//			when (menuItem.itemId) {
//					R.id.ciphersFragment -> this.findNavController()
//						.navigate(MainFragmentDirections.actionMainFragmentToCiphersFragment())
//					R.id.settingsFragment -> this.findNavController()
//						.navigate(MainFragmentDirections.actionMainFragmentToSettingsFragment())
//			}
//
//			// closes the nav drawer
//			dismiss()
//
//			true
//		}

		return binding.root
	}
	
	override fun getTheme(): Int = R.style.BottomSheetDialogTheme
	
	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)
	
}