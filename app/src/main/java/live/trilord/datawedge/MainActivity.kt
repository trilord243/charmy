package live.trilord.datawedge

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val intentFilter: IntentFilter = IntentFilter().apply {
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
                        jsonData = jsonData
                    )
                }
            }
        }
    }

    private val broadCastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            val bundle: Bundle? = intent.extras
            jsonData = bundle?.getString("com.symbol.datawedge.data_string")





        }
    }
}

@Composable
fun OCRButton(modifier: Modifier = Modifier, context: Context, jsonData: String?) {

    fun changeProfile(){

        val i =Intent()
        i.setAction("com.symbol.datawedge.api.ACTION")
        i.putExtra("com.symbol.datawedge.api.SWITCH_TO_PROFILE", "Profile0")
        i.putExtra("SEND_RESULT", "LAST_RESULT")
        context.sendBroadcast(i)
    }



    val intent = Intent("com.symbol.datawedge.api.ACTION")
    Column(modifier = modifier) {
        Button(onClick = {

            intent.putExtra("com.symbol.datawedge.api.SOFT_SCAN_TRIGGER", "START_SCANNING")
            context.sendBroadcast(intent)
        }) {
            Text(text = "Start OCR")
        }







        jsonData?.let {
            Text(text = it)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OCRButtonPreview() {
    DatawedgeTheme {
        OCRButton(context = LocalContext.current, jsonData = "Sample JSON Data")
    }
}