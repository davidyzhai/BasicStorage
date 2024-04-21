package com.example.basicstorage
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// At the top level of your kotlin file:
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val COUNTER_KEY = intPreferencesKey("counter")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlayAudio(this)
        }
    }
}

@Composable
fun PlayAudio(context: Context){

    val mp: MediaPlayer = MediaPlayer.create(context, R.raw.audio)
    val scope = rememberCoroutineScope()
    val counter = context.dataStore.data.map { preferences ->
        preferences[COUNTER_KEY] ?: 0
    }.collectAsState(initial = 0)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_music),
            contentDescription = "",
            modifier = Modifier
                .height(160.dp)
                .width(160.dp)
                .padding(32.dp)
                .background(Color.Black)
        )

        Text("Counter: ${counter.value}", style = MaterialTheme.typography.titleLarge)

        Row {

            IconButton(onClick = { mp.start() }, modifier = Modifier.size(35.dp)) {
                Icon(painter = painterResource(id = R.drawable.ic_play), contentDescription = "")
            }

            IconButton(onClick = { mp.pause() }, modifier = Modifier.size(35.dp)) {
                Icon(painter = painterResource(id = R.drawable.ic_pause), contentDescription = "")
            }

            IconButton(onClick = {
                scope.launch {
                    context.dataStore.edit { preferences ->
                        val currentCounter = preferences[COUNTER_KEY] ?: 0
                        preferences[COUNTER_KEY] = currentCounter + 1
                    }
                }
            }, modifier = Modifier.size(35.dp)) {
                Icon(painter = painterResource(id = R.drawable.ic_add), contentDescription = "")
            }
        }
    }
}
