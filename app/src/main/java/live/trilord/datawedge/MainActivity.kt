package live.trilord.datawedge


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
                    OCRButton(modifier = Modifier.padding(innerPadding), jsonData = jsonData)
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
                intent.hasExtra("com.symbol.datawedge.data_string") -> {
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

    private fun setConfig(profileName: String) {
        val intent = Intent("com.symbol.datawedge.api.ACTION")
        val configBundle = Bundle().apply {
            putString("PROFILE_NAME", profileName)
            putBundle("APP_LIST", Bundle().apply {
                putString("PACKAGE_NAME", packageName)
                putStringArray("ACTIVITY_LIST", arrayOf("*"))
            })
            putParcelableArray("PLUGIN_CONFIG", arrayOf(
                Bundle().apply {
                    putString("PLUGIN_NAME", "OCR")
                    putString("RESET_CONFIG", "true")
                    putBundle("PARAM_LIST", Bundle().apply {
                        putString("FREE_FORM_ENABLED", "false")
                    })
                },
                Bundle().apply {
                    putString("PLUGIN_NAME", "BARCODE")
                    putString("RESET_CONFIG", "true")
                    putBundle("PARAM_LIST", Bundle().apply {
                        putString("scanner_input_enabled", "true")
                    })
                },
                Bundle().apply {
                    putString("PLUGIN_NAME", "WORKFLOW")
                    putString("RESET_CONFIG", "true")
                    putBundle("PARAM_LIST", Bundle().apply {
                        putString("workflow_input_enabled", "false")
                    })
                }
            ))
            putBundle("OUTPUT_PLUGIN", Bundle().apply {
                putString("PLUGIN_NAME", "INTENT")
                putString("RESET_CONFIG", "true")
                putBundle("PARAM_LIST", Bundle().apply {
                    putString("intent_output_enabled", "true")
                    putString("intent_action", "com.zebra.id_scanning.ACTION")
                    putString("intent_category", Intent.CATEGORY_DEFAULT)
                    putString("intent_delivery", "2")
                })
            })
        }
        intent.putExtra("com.symbol.datawedge.api.SET_CONFIG", configBundle)
        sendBroadcast(intent)
    }
}

@Composable
fun OCRButton(modifier: Modifier = Modifier, jsonData: String?) {
    val context = LocalContext.current


    if (jsonData?.contains("chocolate", ignoreCase = true) == true) {

        val intent = Intent(context, SecondActivity::class.java).apply {
            putExtra("boxDesc", "chocolate")
            putExtra("jsonData", jsonData)
        }
        context.startActivity(intent)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Escanee la caja para extraer su información",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
    }
}
