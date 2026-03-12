package com.byeboo.app.data.validatorimpl

import android.content.Context
import com.byeboo.app.domain.model.auth.BadWordValidator
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

class BadWordValidatorImpl
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) : BadWordValidator {
        private val forbiddenData: JSONObject by lazy {
            try {
                context.assets
                    .open("forbidden_words.json")
                    .bufferedReader()
                    .use { it.readText() }
                    .let { JSONObject(it) }
            } catch (e: Exception) {
                Timber.e(e)
                JSONObject()
            }
        }

        private val badWordsSet: Set<String> by lazy {
            forbiddenData.optJSONArray("words")?.toSet() ?: emptySet()
        }

        private val restrictedNamesSet: Set<String> by lazy {
            forbiddenData.optJSONArray("restrictedNames")?.toSet() ?: emptySet()
        }

        private fun org.json.JSONArray.toSet(): Set<String> {
            val set = mutableSetOf<String>()
            for (i in 0 until this.length()) {
                val word = this.optString(i)?.lowercase()?.trim()
                if (!word.isNullOrEmpty()) set.add(word)
            }
            return set
        }

        override fun contains(input: String): Boolean {
            if (input.isBlank()) return false

            val trimmedInput = input.trim().lowercase()

            if (restrictedNamesSet.contains(trimmedInput)) return true

            return badWordsSet.any { badWord ->
                trimmedInput.contains(badWord)
            }
        }
    }
