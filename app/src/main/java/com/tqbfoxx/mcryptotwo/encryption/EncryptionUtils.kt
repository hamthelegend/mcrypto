package com.tqbfoxx.mcryptotwo.encryption

import android.content.Context
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.tqbfoxx.mcryptotwo.main.adapters.Message

const val START_ID = "//"
const val END_ID = "√√"

fun Context.encrypt(string: String): String {
    val simpleEncryptedText = AESUtils.encrypt(string, this.activeCipher.keyValue.toByteArray())
    return "$START_ID$simpleEncryptedText$END_ID"
}

fun Context.decrypt(string: String): String {
    val simpleEncryptedText = string.removePrefix(START_ID).removeSuffix(
            END_ID
    )
    return AESUtils.decrypt(simpleEncryptedText, this.activeCipher.keyValue.toByteArray())
}

fun String.isEncrypted(): Boolean {
    return this.startsWith(START_ID) || this.endsWith(END_ID)
}

var Context.conversation: ArrayList<Message>
    get() {
        try {
            val conversationJson = ciphersPref.getString("conversation", ERROR_STRING)
            return gson.fromJson(conversationJson, object : TypeToken<ArrayList<Message>>() {}.type)
        } catch (e: JsonSyntaxException) {
            conversation = ArrayList()
            val conversationJson = ciphersPref.getString("conversation", ERROR_STRING)
            return gson.fromJson(conversationJson, object : TypeToken<ArrayList<Message>>() {}.type)
        }
    }
    set(ciphers) = ciphersPref.edit()
            .putString("conversation", gson.toJson(ciphers)).apply()