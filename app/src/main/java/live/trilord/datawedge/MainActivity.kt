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
    fun Context.isActivity(activityClass: Class<out ComponentActivity>): Boolean {
        return this is ComponentActivity && this::class.java == activityClass
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
                    if (context.isActivity(MainActivity::class.java)) {
                        println("MainActivity")

                    jsonData = bundle?.getString("com.symbol.datawedge.data_string")
                    }
                }
            }
        }
    }


}

@Composable
fun OCRButton(modifier: Modifier = Modifier, jsonData: String?) {
    val context = LocalContext.current

    if(jsonData?.contains("chc", ignoreCase = true)==true){
        val intent = Intent(context, SecondActivity::class.java).apply {
            putExtra("nombreProducto", "cachapa")
            putExtra("jsonData", jsonData)
        }
        context.startActivity(intent)
        return
    }



    DataFixed.getAll().keys.forEach { key ->
        if (jsonData?.contains(key, ignoreCase = true) == true) {
            val intent = Intent(context, SecondActivity::class.java).apply {
                putExtra("nombreProducto", key)
                putExtra("jsonData", jsonData)
            }
            context.startActivity(intent)
            return@forEach
        }
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