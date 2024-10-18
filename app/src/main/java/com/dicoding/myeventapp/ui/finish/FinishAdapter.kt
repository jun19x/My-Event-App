import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.myeventapp.data.response.ListEventsItem
import com.dicoding.myeventapp.databinding.ItemEventBinding

class FinishAdapter(private val events: List<ListEventsItem>, b: Boolean) : RecyclerView.Adapter<FinishAdapter.EventViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    interface OnItemClickCallback {
        fun onItemClicked(data: ListEventsItem)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.bind(event)
    }

    override fun getItemCount(): Int = events.size

    inner class EventViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            binding.apply {
                tvTitle.text = event.name
                tvDate.text = event.beginTime


                Glide.with(itemView.context)
                    .load(event.mediaCover)
                    .into(ivImage)


                itemView.setOnClickListener {
                    onItemClickCallback?.onItemClicked(event)
                }
            }
        }
    }
}
