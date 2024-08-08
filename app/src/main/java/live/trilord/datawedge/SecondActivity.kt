package live.trilord.datawedge

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.ContactsContract.Data
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import live.trilord.datawedge.ui.theme.DatawedgeTheme

class SecondActivity : ComponentActivity() {
    private var barocdes by mutableStateOf<String?>(null)
    private var activeProfile by mutableStateOf<String?>(null)
    private var profilesList by mutableStateOf<List<String>?>(null)
    private var showDialog by mutableStateOf<Boolean>(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intentFilter: IntentFilter = IntentFilter().apply {
            addAction("com.symbol.datawedge.api.RESULT_ACTION")
            addAction("com.symbol.datawedge.api.RESULT_GET_PROFILES_LIST")
            addAction("com.zebra.id_scanning.ACTION")
            addCategory(Intent.CATEGORY_DEFAULT)
        }
        registerReceiver(broadCastReceiver, intentFilter)

        val nameProduct = intent.getStringExtra("nombreProducto")

        setContent {
            DatawedgeTheme {
                SecondScreen(nameProduct=nameProduct,showDialog=showDialog,barocdes = barocdes)
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


                    barocdes = bundle?.getString("com.symbol.datawedge.data_string")



                    showDialog = true




                }
            }
        }
    }
}

@Composable
fun SecondScreen(nameProduct: String?, showDialog: Boolean,barocdes:String?) {
    val context= LocalContext.current


    var quantity by remember { mutableStateOf("") }
    val codigoBarra= nameProduct?.let { DataFixed.get(it) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        if (barocdes != null  && barocdes.contains(codigoBarra.toString()) ) {
            Text(text = "Caja de $nameProduct")
            Spacer(modifier = Modifier.height(16.dp))
            Text(text =  "Description: ${DataFixed.getDesc(codigoBarra.toString())}                         ")
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Codigo: $codigoBarra")
            TextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Cantidad de cajas") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Cantidad ingresada: $quantity")

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val intent = Intent(context, MainActivity::class.java).apply {
                    putExtra("boxDesc", "")
                    putExtra("jsonData", "")
                }
                context.startActivity(intent)
            }) {
                Text("Enviar Cantidad")
            }

        }else{

            Text("Lea los distintos codigos de barras", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }


    }
}

