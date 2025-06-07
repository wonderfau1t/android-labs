package ru.denisepritskiy.lab17

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.denisepritskiy.lab17.databinding.FragmentMusicGenreBinding

class MusicGenreFragment : Fragment() {

    private var _binding: FragmentMusicGenreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMusicGenreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val genre = arguments?.getString(ARG_GENRE) ?: ""
        binding.genreText.text = genre
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_GENRE = "genre"

        fun newInstance(genre: String): MusicGenreFragment {
            val fragment = MusicGenreFragment()
            val args = Bundle()
            args.putString(ARG_GENRE, genre)
            fragment.arguments = args
            return fragment
        }
    }
}