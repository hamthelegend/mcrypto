package com.tqbfoxx.mcryptotwo.encryption

import android.content.Context
import android.content.SharedPreferences
import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlinx.android.parcel.Parcelize

const val ERROR_STRING = "ERROR"

val gson = Gson()

@Parcelize
data class Cipher(var name: String, var keyValue: String, var index: Int): Parcelable

val Context.ciphersPref: SharedPreferences
	get() = getSharedPreferences("ciphersPref", Context.MODE_PRIVATE)

var Context.ciphers: ArrayList<Cipher>
	get() {
		try {
			val ciphersJson = ciphersPref.getString("ciphers", ERROR_STRING)
			return gson.fromJson(ciphersJson, object : TypeToken<ArrayList<Cipher>>() {}.type)
		} catch (e: JsonSyntaxException) {
			ciphers = ArrayList()
			val ciphersJson = ciphersPref.getString("ciphers", ERROR_STRING)
			return gson.fromJson(ciphersJson, object : TypeToken<ArrayList<Cipher>>() {}.type)
		}
	}
	set(ciphers) = ciphersPref.edit()
		.putString("ciphers", gson.toJson(ciphers)).apply()

var Context.activeCipher: Cipher
	get() {
		val activeCipherJson = ciphersPref.getString("activeCipher", /*defValue*/ERROR_STRING)
		return gson.fromJson(activeCipherJson, Cipher::class.java)
	}
	set(activeCipher) = ciphersPref.edit()
		.putString("activeCipher", gson.toJson(activeCipher)).apply()

infix fun Context.addCipher(cipher: Cipher) {
	ciphers = (ciphers + cipher) as ArrayList
}

fun Context.removeCipher(cipher: Cipher) {
	ciphers = ciphers.apply { remove(cipher) }
	updateCipherIndices()
}

fun Context.updateCipher(cipher: Cipher) {
	val newCiphers = ciphers
	newCiphers[cipher.index] = cipher
	ciphers = newCiphers
}

fun Context.updateCipherIndices() {
	for ((i, cipher) in ciphers.withIndex()) cipher.index = i
}