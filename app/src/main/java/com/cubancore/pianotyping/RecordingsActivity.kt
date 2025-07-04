package com.cubancore.pianotyping

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.cubancore.pianotyping.api.GreetingsProvider
import com.cubancore.pianotyping.data.Greeting
import com.cubancore.pianotyping.data.cb.GreetingsResults
import com.cubancore.pianotyping.ui.RecordingsScreen
import com.cubancore.pianotyping.ui.theme.PianoTypingTheme
import kotlin.getValue

class RecordingsActivity : ComponentActivity(), GreetingsResults {
    private val recordingsManager: RecordingsManagerViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val provider = GreetingsProvider()
        provider.fetchGreeting(this)

        setContent {
            PianoTypingTheme {
                Scaffold (
                    topBar = {
                        TopAppBar(
                            navigationIcon = {
                                IconButton(
                                    onClick = { finish() }
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = stringResource(R.string.back)
                                    )
                                }
                            },
                            title = {
                                Text(stringResource(R.string.recordings_activity_title))
                            },
                            actions = {
                                Button (
                                  onClick = { throw RuntimeException("Gratitude")}
                                ) {
                                    Text(
                                        text = stringResource(R.string.send_gratitude)
                                    )
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    Box (
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        RecordingsScreen(recordingsManager)
                    }
                }
            }
        }
    }

    override fun onDataFetchedSuccess(message: Greeting) {
        Toast.makeText(this, message.message, Toast.LENGTH_SHORT).show()
    }

    override fun onDataFetchedFailed(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}