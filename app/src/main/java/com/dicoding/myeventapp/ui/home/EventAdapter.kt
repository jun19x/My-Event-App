import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.myeventapp.data.response.ListEventsItem
import com.dicoding.myeventapp.databinding.ItemEventBinding
import com.dicoding.myeventapp.databinding.ItemEventFinishBinding

class EventAdapter(private val events: List<ListEventsItem>, private val isFinished: Boolean = false) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Define ViewType
    companion object {
        private const val TYPE_EVENT = 1
        private const val TYPE_FINISHED_EVENT = 2
    }

    private var onItemClickCallback: OnItemClickCallback? = null

    interface OnItemClickCallback {
        fun onItemClicked(data: ListEventsItem)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun getItemViewType(position: Int): Int {
        return if (isFinished) {
            TYPE_FINISHED_EVENT
        } else {
            TYPE_EVENT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_FINISHED_EVENT -> {
                val binding = ItemEventFinishBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                FinishedEventViewHolder(binding)
            }
            else -> {
                val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                EventViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val event = events[position]
        when (holder) {
            is EventViewHolder -> holder.bind(event)
            is FinishedEventViewHolder -> holder.bind(event)
        }
    }



    override fun getItemCount(): Int {
        return minOf(events.size, 5)
    }

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

    inner class FinishedEventViewHolder(private val binding: ItemEventFinishBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            binding.apply {
                tvTitle.text = event.name
                tvDate.text = event.endTime


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
