package ru.denisepritskiy.lab17

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.denisepritskiy.lab17.databinding.FragmentNewsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private var timer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.newsText.text = getString(R.string.news)

        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNav)
        val badge = bottomNav?.getOrCreateBadge(R.id.menu_news)

        binding.clearNewsButton.setOnClickListener {
            badge?.number = 0
            badge?.isVisible = false
        }
    }

    override fun onResume() {
        super.onResume()
        timer?.cancel()
    }

    override fun onPause() {
        super.onPause()
        startNewsTimer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer?.cancel()
        _binding = null
    }

    private fun startNewsTimer() {
        timer?.cancel()
        timer = object : CountDownTimer(Long.MAX_VALUE, 3000) {
            override fun onTick(millisUntilFinished: Long) {
                val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNav)
                val badge = bottomNav?.getOrCreateBadge(R.id.menu_news)
                badge?.let {
                    it.number++
                    it.isVisible = it.number > 0
                }
            }

            override fun onFinish() {}
        }.start()
    }
}