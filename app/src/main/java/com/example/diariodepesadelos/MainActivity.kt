package com.example.diariodepesadelos

import MyDatabaseHelper
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
  private lateinit var dbHelper: MyDatabaseHelper

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    dbHelper = MyDatabaseHelper(this)

    val editTextTitulo = findViewById<EditText>(R.id.editTextTitle)
    val editTextDescricao = findViewById<EditText>(R.id.editTextDescription)
    val editTextNivelTerror = findViewById<EditText>(R.id.editTextNivelTerror)
    val editTextTags = findViewById<EditText>(R.id.editTextTags)
    val buttonAdd = findViewById<Button>(R.id.buttonInsert)
    val buttonViewAll = findViewById<Button>(R.id.buttonView)

    buttonAdd.setOnClickListener {
      val titulo = editTextTitulo.text.toString()
      val descricao = editTextDescricao.text.toString()
      val nivelTerror = editTextNivelTerror.text.toString().toIntOrNull() ?: 0
      val tags = editTextTags.text.toString()
      val data = "2024-10-31"

      if (titulo.isNotEmpty() && descricao.isNotEmpty()) {
        dbHelper.insertPesadelo(titulo, descricao, nivelTerror, tags, data)
        editTextTitulo.text.clear()
        editTextDescricao.text.clear()
        editTextNivelTerror.text.clear()
        editTextTags.text.clear()
      }
    }

    buttonViewAll.setOnClickListener {
      val intent = Intent(this, ViewNightmaresActivity::class.java)
      startActivity(intent)
    }
  }
}
