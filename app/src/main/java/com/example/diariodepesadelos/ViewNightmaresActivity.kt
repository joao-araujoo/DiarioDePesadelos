// ViewNightmaresActivity.kt
package com.example.diariodepesadelos

import MyDatabaseHelper
import Nightmare
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ViewNightmaresActivity : AppCompatActivity() {

  private lateinit var nightmareRecyclerView: RecyclerView
  private lateinit var tagSpinner: Spinner
  private lateinit var nightmareAdapter: NightmareAdapter
  private lateinit var dbHelper: MyDatabaseHelper
  private var nightmaresList = listOf<Nightmare>()
  private var filteredNightmaresList = listOf<Nightmare>()

  @SuppressLint("MissingInflatedId")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_view_nightmares)

    dbHelper = MyDatabaseHelper(this)

    nightmareRecyclerView = findViewById(R.id.nightmareRecyclerView)
    tagSpinner = findViewById(R.id.tagSpinner)

    // Configurar o RecyclerView com o adapter
    nightmareAdapter = NightmareAdapter(filteredNightmaresList) { nightmare ->
      // Função lambda para deletar o pesadelo quando o botão for pressionado
      deleteNightmare(nightmare)
    }
    nightmareRecyclerView.layoutManager = LinearLayoutManager(this)
    nightmareRecyclerView.adapter = nightmareAdapter

    // Carregar os pesadelos do banco de dados
    nightmaresList = dbHelper.readPesadelos()
    filteredNightmaresList = nightmaresList
    nightmareAdapter.updateData(filteredNightmaresList)

    // Carregar as tags distintas do banco de dados
    val tags = dbHelper.getDistinctTags().toMutableList()
    tags.add(0, "Todos") // Adicionar a opção "Todos"
    val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tags)
    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    tagSpinner.adapter = spinnerAdapter

    // Listener para filtrar pesadelos ao selecionar uma tag
    tagSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
      override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val selectedTag = tags[position]
        filteredNightmaresList = if (selectedTag == "Todos") {
          nightmaresList
        } else {
          nightmaresList.filter { it.tags.contains(selectedTag) }
        }
        nightmareAdapter.updateData(filteredNightmaresList)
      }

      override fun onNothingSelected(parent: AdapterView<*>) {}
    }

    // Botão de voltar para a página inicial
    val backButton: Button = findViewById(R.id.backButton)
    backButton.setOnClickListener {
      val intent = Intent(this, MainActivity::class.java)
      startActivity(intent)
    }
  }

  private fun deleteNightmare(nightmare: Nightmare) {
    // Deletar o pesadelo do banco
    val rowsDeleted = dbHelper.deletePesadelo(nightmare.id)
    if (rowsDeleted > 0) {
      Toast.makeText(this, "Pesadelo excluído com sucesso!", Toast.LENGTH_SHORT).show()
      // Atualizar a lista após a exclusão
      nightmaresList = dbHelper.readPesadelos() // Recarregar os pesadelos
      filteredNightmaresList = nightmaresList
      nightmareAdapter.updateData(filteredNightmaresList) // Atualizar o RecyclerView
    } else {
      Toast.makeText(this, "Erro ao excluir o pesadelo.", Toast.LENGTH_SHORT).show()
    }
  }
}
