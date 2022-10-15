import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.deepstylo.R
import com.example.deepstylo.model.FilterItem
import kotlinx.android.synthetic.main.filter_item.view.*

class FilterAdapter() : ListAdapter<FilterItem, FilterAdapter.viewHolder>(Diff()) {

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.filter_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val current = getItem(position)

        holder.itemView.apply {
            filterImage.setImageResource(current.image)

            setOnClickListener {
                onItemClickListner?.let {
                    it(current)
                }
            }

        }
    }

    private var onItemClickListner: ((FilterItem) -> Unit)? = null

    fun setOnItemClickListner(listner: (FilterItem) -> Unit) {
        onItemClickListner = listner
    }


}


class Diff() : DiffUtil.ItemCallback<FilterItem>() {
    override fun areItemsTheSame(oldItem: FilterItem, newItem: FilterItem): Boolean {
        return oldItem.styleNum == newItem.styleNum
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: FilterItem, newItem: FilterItem): Boolean {
        return oldItem == newItem
    }

}