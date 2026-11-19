package com.shadowtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val bg = Color(0xFF090713)
private val panel = Color(0xFF191329)
private val violet = Color(0xFF9B72FF)
private val muted = Color(0xFFBDB2D2)

class MainActivity : ComponentActivity() {
 override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); setContent { ShadowScreen() } }
}

@Composable
fun ShadowScreen() {
 var xp by remember { mutableIntStateOf(120) }
 var note by remember { mutableStateOf("") }
 var notes by remember { mutableStateOf(listOf<String>()) }
 var question by remember { mutableStateOf("") }
 var reply by remember { mutableStateOf("System online. Offline starter assistant ready.") }
 var done by remember { mutableStateOf(setOf<Int>()) }
 val tasks = listOf("Review one school topic" to 25, "Practise a skill for 20 minutes" to 20, "Plan tomorrow" to 15)
 MaterialTheme(colorScheme = darkColorScheme(primary = violet, background = bg, surface = panel)) {
  Column(Modifier.fillMaxSize().background(bg).padding(18.dp)) {
   Text("SHADOW TRACKER", color = violet, fontWeight = FontWeight.Bold)
   Text("SYSTEM INTERFACE", color = muted, fontSize = 11.sp)
   Spacer(Modifier.height(16.dp))
   Card(colors = CardDefaults.cardColors(containerColor = panel), shape = RoundedCornerShape(18.dp)) {
    Column(Modifier.fillMaxWidth().padding(18.dp)) {
     Text("PLAYER STATUS", color = muted, fontSize = 11.sp)
     Text("LEVEL ${xp / 100 + 1}", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Black)
     Text("$xp XP  •  ${100 - xp % 100} XP TO NEXT LEVEL", color = violet)
    }
   }
   Spacer(Modifier.height(16.dp))
   Text("DAILY MISSIONS", color = violet, fontWeight = FontWeight.Bold)
   LazyColumn(Modifier.weight(1f)) {
    items(tasks.size) { i ->
     Card(Modifier.fillMaxWidth().padding(vertical = 5.dp), colors = CardDefaults.cardColors(containerColor = panel)) {
      Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
       Column(Modifier.weight(1f)) { Text(tasks[i].first); Text("+${tasks[i].second} XP", color = muted, fontSize = 12.sp) }
       Button(enabled = i !in done, onClick = { if (done.add(i)) { done = done + i; xp += tasks[i].second } }) { Text(if (i in done) "DONE" else "CLAIM") }
      }
     }
    }
    item {
     Spacer(Modifier.height(12.dp))
     Text("PRIVATE NOTES", color = violet, fontWeight = FontWeight.Bold)
     OutlinedTextField(note, { note = it }, Modifier.fillMaxWidth(), label = { Text("Write a note") })
     Button(onClick = { if (note.isNotBlank()) { notes = notes + note.trim(); note = "" } }) { Text("SAVE NOTE") }
     notes.forEach { Text("• $it", color = muted) }
     Spacer(Modifier.height(12.dp))
     Text("OFFLINE ASSISTANT", color = violet, fontWeight = FontWeight.Bold)
     OutlinedTextField(question, { question = it }, Modifier.fillMaxWidth(), label = { Text("Ask the System") })
     Button(onClick = { reply = offlineReply(question) }) { Text("ASK SYSTEM") }
     Text(reply, color = muted, modifier = Modifier.padding(top = 8.dp))
     Spacer(Modifier.height(12.dp))
     Text("SYNC: NOT CONNECTED", color = muted, fontSize = 11.sp)
     Text("Starter build stores this session locally; persistent encrypted storage is still to be added.", color = muted, fontSize = 11.sp)
    }
   }
  }
 }
}

private fun offlineReply(s: String): String = when {
 s.isBlank() -> "Enter a question first."
 s.contains("study", true) || s.contains("exam", true) -> "Choose one topic, focus for 20 minutes, then test yourself without notes."
 s.contains("goal", true) || s.contains("mission", true) -> "Break the goal into one small action you can complete today."
 s.contains("money", true) || s.contains("invest", true) -> "Learning mode: understand risk, fees, diversification and time horizon. This app does not trade."
 else -> "This is a basic offline assistant, not a full AI model yet. Try asking about study, goals or money learning."
}
