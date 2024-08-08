package live.trilord.datawedge


import android.content.Context
import androidx.activity.ComponentActivity

fun Context.isActivity(activityClass: Class<out ComponentActivity>): Boolean {
    return this is ComponentActivity && this::class.java == activityClass
}