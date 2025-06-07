package ru.denisepritskiy.lab16


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class FoxFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fox, container, false)

        val nextButton: Button = view.findViewById(R.id.nextButton)
        val stayButton: Button = view.findViewById(R.id.backButton)

        nextButton.setOnClickListener {
            findNavController().navigate(R.id.action_foxFragment_to_finalGoodFragment)
        }
        stayButton.setOnClickListener{
            findNavController().navigate(R.id.action_foxFragment_to_finalBadFragment)
        }

        return view
    }
}