package ru.denisepritskiy.lab17

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import ru.denisepritskiy.lab17.databinding.FragmentMusicBinding

class MusicFragment : Fragment() {

    private var _binding: FragmentMusicBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMusicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val genres = listOf(
            getString(R.string.tab_music_pop),
            getString(R.string.tab_music_disco),
            getString(R.string.tab_music_rock),
            getString(R.string.tab_music_classic),
            getString(R.string.tab_music_score),
            getString(R.string.tab_music_instrument),
            getString(R.string.tab_music_jazz),
            getString(R.string.tab_music_hiphop)
        )

        val fragments = genres.map { MusicGenreFragment.newInstance(it) }
        val adapter = MusicViewPagerAdapter(fragments, this)
        binding.pager.adapter = adapter

        TabLayoutMediator(binding.tabs, binding.pager) { tab, position ->
            tab.text = genres[position]
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}