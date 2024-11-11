import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.diariodepesadelos.R

class NightmareAdapter(
  private var nightmaresList: List<Nightmare>,
  private val onDeleteClick: (Nightmare) -> Unit // Adicionando um listener de exclusão
) : RecyclerView.Adapter<NightmareAdapter.NightmareViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NightmareViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_nightmare, parent, false)
    return NightmareViewHolder(view)
  }

  override fun onBindViewHolder(holder: NightmareViewHolder, position: Int) {
    val nightmare = nightmaresList[position]
    holder.bind(nightmare)
  }

  override fun getItemCount(): Int {
    return nightmaresList.size
  }

  fun updateData(newList: List<Nightmare>) {
    nightmaresList = newList
    notifyDataSetChanged() // Notificar o adapter sobre a mudança de dados
  }

  inner class NightmareViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val titleTextView: TextView = itemView.findViewById(R.id.textViewTitleCard)
    private val descriptionTextView: TextView = itemView.findViewById(R.id.textViewDescriptionCard)
    private var nivelTerrorTextView: TextView = itemView.findViewById(R.id.textViewNivelTerrorCard)
    private val tagsTextView: TextView = itemView.findViewById(R.id.textViewTagsCard)
    private val deleteButton: Button = itemView.findViewById(R.id.buttonDelete)

    @SuppressLint("SetTextI18n")
    fun bind(nightmare: Nightmare) {
      titleTextView.text = nightmare.title
      descriptionTextView.text = nightmare.description
      nivelTerrorTextView.text = "Nivel de Terror: " + nightmare.nivelTerror.toString()
      tagsTextView.text = "Tag: " + nightmare.tags
      deleteButton.setOnClickListener {
        // Chama a função de exclusão passando o pesadelo
        onDeleteClick(nightmare)
      }
    }
  }
}
