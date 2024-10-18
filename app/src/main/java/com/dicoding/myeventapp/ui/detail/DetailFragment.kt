package com.dicoding.myeventapp.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.dicoding.myeventapp.data.response.DetailEventResponse
import com.dicoding.myeventapp.data.response.Event
import com.dicoding.myeventapp.data.retrofit.ApiConfig
import com.dicoding.myeventapp.databinding.FragmentDetailBinding
import kotlinx.coroutines.launch
import retrofit2.Response

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val eventId = arguments?.getInt("EVENT_ID") ?: -1
        if (eventId != -1) {
            getEventDetail(eventId)
        }
    }

    private fun getEventDetail(eventId: Int) {
        binding.progressBar.visibility = View.VISIBLE // Tampilkan ProgressBar

        lifecycleScope.launch {
            try {
                val response: Response<DetailEventResponse> = ApiConfig.getApiService().getDetailEvent(eventId.toString())
                if (response.isSuccessful) {
                    response.body()?.event?.let { event ->
                        displayEventDetails(event)
                    } ?: run {
                        // Handle jika event tidak ditemukan
                    }
                } else {
                    // Handle error respons
                }
            } catch (e: Exception) {
                // Handle exception
            } finally {
                binding.progressBar.visibility = View.GONE // Sembunyikan ProgressBar
            }
        }
    }


    private fun displayEventDetails(event: Event) {
        binding.apply {
            eventName.text = event.name
            ownerName.text = event.ownerName
            beginTime.text = event.beginTime
            remainingQuota.text = "${event.quota?.minus(event.registrants ?: 0)} left" // Sisa kuota


            description.text = HtmlCompat.fromHtml(event.description ?: "", HtmlCompat.FROM_HTML_MODE_LEGACY)


            Glide.with(this@DetailFragment)
                .load(event.imageLogo ?: event.mediaCover) // Gunakan imageLogo jika ada
                .into(imageLogo)

            openLinkButton.setOnClickListener {

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
                startActivity(intent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
