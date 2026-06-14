package com.ngoctientnt.template.core.locale

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.ngoctientnt.template.R

enum class AppLanguage(
    val languageTag: String?,
    @StringRes val displayNameRes: Int,
) {
    SYSTEM(null, R.string.language_system),
    ENGLISH("en", R.string.language_english),
    VIETNAMESE("vi", R.string.language_vietnamese),
    ;

    fun toLocaleListCompat(): LocaleListCompat {
        return if (languageTag == null) {
            LocaleListCompat.getEmptyLocaleList()
        } else {
            LocaleListCompat.forLanguageTags(languageTag)
        }
    }

    companion object {
        val selectable: List<AppLanguage> = entries

        fun fromLanguageTag(tag: String?): AppLanguage {
            if (tag.isNullOrBlank()) return SYSTEM
            return entries.find { it.languageTag == tag } ?: SYSTEM
        }

        fun fromLocaleListCompat(locales: LocaleListCompat): AppLanguage {
            val tag = locales.toLanguageTags()
            return fromLanguageTag(tag.takeIf { it.isNotBlank() })
        }

        fun current(): AppLanguage {
            return fromLocaleListCompat(AppCompatDelegate.getApplicationLocales())
        }
    }
}
