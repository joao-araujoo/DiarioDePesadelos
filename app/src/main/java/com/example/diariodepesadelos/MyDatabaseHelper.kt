import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

  companion object {
    private const val DATABASE_NAME = "DiarioPesadelos.db"
    private const val DATABASE_VERSION = 1
    private const val TABLE_NAME = "pesadelos"
    private const val COLUMN_ID = "id"
    private const val COLUMN_TITLE = "titulo"
    private const val COLUMN_DESCRIPTION = "descricao"
    private const val COLUMN_TERROR_LEVEL = "nivel_terror"
    private const val COLUMN_TAGS = "tags"
    private const val COLUMN_DATE = "data"

    private const val SQL_CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
            "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$COLUMN_TITLE TEXT, " +
            "$COLUMN_DESCRIPTION TEXT, " +
            "$COLUMN_TERROR_LEVEL INTEGER, " +
            "$COLUMN_TAGS TEXT, " +
            "$COLUMN_DATE TEXT)"
  }

  override fun onCreate(db: SQLiteDatabase) {
    db.execSQL(SQL_CREATE_TABLE)
  }

  override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
    onCreate(db)
  }

  fun insertPesadelo(titulo: String, descricao: String, nivelTerror: Int, tags: String, data: String): Long {
    val db = writableDatabase
    val values = ContentValues().apply {
      put(COLUMN_TITLE, titulo)
      put(COLUMN_DESCRIPTION, descricao)
      put(COLUMN_TERROR_LEVEL, nivelTerror)
      put(COLUMN_TAGS, tags)
      put(COLUMN_DATE, data)
    }
    return db.insert(TABLE_NAME, null, values)
  }

  fun readPesadelos(): List<Nightmare> {
    val db = readableDatabase
    val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
    val pesadelosList = mutableListOf<Nightmare>()

    with(cursor) {
      while (moveToNext()) {
        val id = getLong(getColumnIndexOrThrow(COLUMN_ID))
        val titulo = getString(getColumnIndexOrThrow(COLUMN_TITLE))
        val descricao = getString(getColumnIndexOrThrow(COLUMN_DESCRIPTION))
        val nivelTerror = getInt(getColumnIndexOrThrow(COLUMN_TERROR_LEVEL))
        val tags = getString(getColumnIndexOrThrow(COLUMN_TAGS))
        val data = getString(getColumnIndexOrThrow(COLUMN_DATE))
        pesadelosList.add(Nightmare(id, titulo, descricao, nivelTerror, tags, data))
      }
    }
    cursor.close()
    return pesadelosList
  }

  fun deletePesadelo(id: Long): Int {
    val db = writableDatabase
    return db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
  }

  // Obter tags distintas
  @SuppressLint("Range")
  fun getDistinctTags(): List<String> {
    val tags = mutableListOf<String>()
    val db = readableDatabase
    val query = "SELECT DISTINCT tags FROM pesadelos"
    val cursor: Cursor = db.rawQuery(query, null)

    while (cursor.moveToNext()) {
      val tag = cursor.getString(cursor.getColumnIndex("tags"))
      tags.addAll(tag.split(", ")) // Separar as tags individuais
    }
    cursor.close()
    db.close()

    return tags.distinct()
  }
}
