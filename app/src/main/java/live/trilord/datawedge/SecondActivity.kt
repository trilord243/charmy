package live.trilord.datawedge

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


class SecondActivity : ComponentActivity() {
    private var jsonData by mutableStateOf<String?>(null)
    private var activeProfile by mutableStateOf<String?>(null)
    private var profilesList by mutableStateOf<List<String>?>(null)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intentFilter: IntentFilter = IntentFilter().apply {
            addAction("com.symbol.datawedge.api.RESULT_ACTION")
            addAction("com.symbol.datawedge.api.RESULT_GET_PROFILES_LIST")
            addAction("com.zebra.id_scanning.ACTION")
            addCategory(Intent.CATEGORY_DEFAULT)
        }
        registerReceiver(broadCastReceiver, intentFilter)
        setContent {

                SecondScreen(jsonData)

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
                    println("Hson data: $jsonData")
                }
            }
        }
    }
}

@Composable
fun SecondScreen(jsonData:String?) {
    Text(text = "   Esto es la segunda pantolla")
    jsonData?.let{
        Text("This is the Second Activity with data: $it")
    }

}