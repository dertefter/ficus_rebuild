package com.dertefter.ficus.repositoty.local

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit

object AppPreferences {
    private var sharedPreferences: SharedPreferences? = null

    fun setup(context: Context) {

        sharedPreferences = context.getSharedPreferences("ficus.sharedprefs", MODE_PRIVATE)
    }

    var review: Boolean?
        get() = Key.REVIEW.getBoolean()
        set(value) = Key.REVIEW.setBoolean(value)

    var login: String?
        get() = Key.LOGIN.getString()
        set(value) = Key.LOGIN.setString(value)

    var theme: String?
        get() = Key.THEME.getString()
        set(value) = Key.THEME.setString(value)

    var monet: Boolean?
        get() = Key.MONET.getBoolean()
        set(value) = Key.MONET.setBoolean(value)

    var nstu_icon: Boolean?
        get() = Key.ICON.getBoolean()
        set(value) = Key.ICON.setBoolean(value)

    var guest: Boolean?
        get() = Key.GUEST.getBoolean()
        set(value) = Key.GUEST.setBoolean(value)

    var password: String?
        get() = Key.PASSWORD.getString()
        set(value) = Key.PASSWORD.setString(value)

    var group: String?
        get() = Key.GROUP.getString()
        set(value) = Key.GROUP.setString(value)

    var token: String?
        get() = Key.TOKEN.getString()
        set(value) = Key.TOKEN.setString(value)

    var di: Boolean?
        get() = Key.DI.getBoolean()
        set(value) = Key.DI.setBoolean(value)

    var faculty: String?
        get() = Key.FACULTY.getString()
        set(value) = Key.FACULTY.setString(value)

    var faculty_image: String?
        get() = Key.FACULTY_IMAGE.getString()
        set(value) = Key.FACULTY_IMAGE.setString(value)

    var name: String?
        get() = Key.NAME.getString()
        set(value) = Key.NAME.setString(value)

    var fullName: String?
        get() = Key.FULLNAME.getString()
        set(value) = Key.FULLNAME.setString(value)

    var dispace_token: String?
        get() = Key.DISPACE_TOKENS.getString()
        set(value) = Key.DISPACE_TOKENS.setString(value)

    private enum class Key {
        LOGIN, PASSWORD, GROUP, TOKEN, NAME, FULLNAME, MONET, THEME, GUEST, ICON, DI, FACULTY_IMAGE, FACULTY, DISPACE_TOKENS, TG, REVIEW;

        fun getBoolean(): Boolean? =
            if (sharedPreferences!!.contains(name)) sharedPreferences!!.getBoolean(
                name,
                false
            ) else null

        fun getFloat(): Float? =
            if (sharedPreferences!!.contains(name)) sharedPreferences!!.getFloat(name, 0f) else null

        fun getInt(): Int? =
            if (sharedPreferences!!.contains(name)) sharedPreferences!!.getInt(name, 0) else null

        fun getLong(): Long? =
            if (sharedPreferences!!.contains(name)) sharedPreferences!!.getLong(name, 0) else null

        fun getString(): String? =
            if (sharedPreferences!!.contains(name)) sharedPreferences!!.getString(
                name,
                ""
            ) else null

        fun setBoolean(value: Boolean?) =
            value?.let { sharedPreferences!!.edit { putBoolean(name, value) } } ?: remove()

        fun setFloat(value: Float?) =
            value?.let { sharedPreferences!!.edit { putFloat(name, value) } } ?: remove()

        fun setInt(value: Int?) =
            value?.let { sharedPreferences!!.edit { putInt(name, value) } } ?: remove()

        fun setLong(value: Long?) =
            value?.let { sharedPreferences!!.edit { putLong(name, value) } } ?: remove()

        fun setString(value: String?) =
            value?.let { sharedPreferences!!.edit { putString(name, value) } } ?: remove()

        fun remove() = sharedPreferences!!.edit { remove(name) }
    }
}