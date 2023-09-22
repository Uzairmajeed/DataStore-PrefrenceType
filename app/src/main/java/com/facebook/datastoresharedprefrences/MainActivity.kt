package com.facebook.datastoresharedprefrences

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var dataStore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnRead = findViewById<Button>(R.id.btnRead)
        val etSaveKey = findViewById<EditText>(R.id.etSaveKey)
        val etSaveValue = findViewById<EditText>(R.id.etSaveValue)
        val etReadkey = findViewById<EditText>(R.id.etReadkey)
        val tvReadValue = findViewById<TextView>(R.id.tvReadValue)
        dataStore = createDataStore(name = "settings")
        btnSave.setOnClickListener {
            lifecycleScope.launch {
                save(
                    etSaveKey.text.toString(),
                    etSaveValue.text.toString()
                )
            }
        }

        btnRead.setOnClickListener {
            lifecycleScope.launch {
                val value = read(etReadkey.text.toString())
                tvReadValue.text = value ?: "No value found"
            }
        }
    }

    private suspend fun save(key: String, value: String) {
        val dataStoreKey = preferencesKey<String>(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    private suspend fun read(key: String): String? {
        val dataStoreKey = preferencesKey<String>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }
}
