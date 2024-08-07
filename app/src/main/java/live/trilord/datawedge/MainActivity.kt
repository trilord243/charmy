package live.trilord.datawedge

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import live.trilord.datawedge.ui.theme.DatawedgeTheme

class MainActivity : ComponentActivity() {
    private var jsonData by mutableStateOf<String?>(null)
    private var activeProfile by mutableStateOf<String?>(null)
    private var profilesList by mutableStateOf<List<String>?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val intentFilter: IntentFilter = IntentFilter().apply {
            addAction("com.symbol.datawedge.api.RESULT_ACTION")
            addAction("com.symbol.datawedge.api.RESULT_GET_PROFILES_LIST")
            addAction("com.zebra.id_scanning.ACTION")
            addCategory(Intent.CATEGORY_DEFAULT)
        }
        registerReceiver(broadCastReceiver, intentFilter)

        setContent {
            DatawedgeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    OCRButton(
                        modifier = Modifier.padding(innerPadding),
                        context = this@MainActivity,
                        jsonData = jsonData,
                        activeProfile = activeProfile,
                        profilesList = profilesList
                    )
                }
            }
        }
    }

    private val broadCastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val bundle: Bundle? = intent.extras
            when {
                intent.hasExtra("com.symbol.datawedge.api.RESULT_GET_ACTIVE_PROFILE") -> {
                    val profileName = intent.getStringExtra("com.symbol.datawedge.api.RESULT_GET_ACTIVE_PROFILE")
                    activeProfile = profileName
                }
                intent.hasExtra("com.symbol.datawedge.api.RESULT_GET_PROFILES_LIST") -> {
                    val profiles = intent.getStringArrayExtra("com.symbol.datawedge.api.RESULT_GET_PROFILES_LIST")
                    profilesList = profiles?.toList()
                }
                else -> {
                    jsonData = bundle?.getString("com.symbol.datawedge.data_string")
                }
            }
        }
    }

    private fun getProfilesList() {
        val intent = Intent().apply {
            action = "com.symbol.datawedge.api.ACTION"
            putExtra("com.symbol.datawedge.api.GET_PROFILES_LIST", "")
        }
        sendBroadcast(intent)
    }

    private fun switchToProfile(profileName: String) {
        val intent = Intent().apply {
            action = "com.symbol.datawedge.api.ACTION"
            putExtra("com.symbol.datawedge.api.SWITCH_TO_PROFILE", profileName)
        }
        sendBroadcast(intent)
    }
}

@Composable
fun OCRButton(
    modifier: Modifier = Modifier,
    context: Context,
    jsonData: String?,
    activeProfile: String?,
    profilesList: List<String>?
) {
    val ACTION = "com.symbol.datawedge.api.ACTION"

    Column(modifier = modifier) {
        Button(onClick = {
            val intent = Intent(ACTION).apply {
                putExtra("com.symbol.datawedge.api.SOFT_SCAN_TRIGGER", "START_SCANNING")
            }
            context.sendBroadcast(intent)
        }) {
            Text(text = "Lectura de OCT")
        }

        Button(onClick = {
            val intent = Intent(ACTION).apply {
                putExtra("com.symbol.datawedge.api.GET_ACTIVE_PROFILE", true)
            }
            context.sendBroadcast(intent)
        }) {
            Text(text = "Obtener Perfil Activo")
        }

        Button(onClick = {
            val intent = Intent(ACTION).apply {
                putExtra("com.symbol.datawedge.api.GET_PROFILES_LIST", "")
            }
            context.sendBroadcast(intent)
        }) {
            Text(text = "Obtener Lista de Perfiles")
        }

        Button(onClick = {
            val intent = Intent(ACTION).apply {
                putExtra("com.symbol.datawedge.api.SWITCH_TO_PROFILE", "Profile0 (default)")
            }
            context.sendBroadcast(intent)
        }) {
            Text(text = "Cambiar a Perfil1")
        }

        activeProfile?.let {
            Text(text = "Perfil Activo: $it")
        }

        profilesList?.let {
            Column {
                Text(text = "Perfiles Disponibles:")
                it.forEach { profile ->
                    Text(text = profile)
                }
            }
        }

        jsonData?.let {
            when {
                it.contains("chocolate", ignoreCase = true) -> {
                    Text(text = it)
                }
                else -> {
                    Text(text = it)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OCRButtonPreview() {
    DatawedgeTheme {
        OCRButton(context = LocalContext.current, jsonData = "Sample JSON Data", activeProfile = "Sample Profile", profilesList = listOf("Profile1", "Profile2"))
    }
}
