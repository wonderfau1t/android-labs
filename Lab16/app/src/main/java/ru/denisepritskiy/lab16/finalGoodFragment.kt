package ru.denisepritskiy.lab16


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class FinalGoodFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_final_good, container, false)

        val nextButton: Button = view.findViewById(R.id.GoodAgainButton)

        nextButton.setOnClickListener {
            findNavController().navigate(R.id.action_finalGoodFragment_to_introFragment)
        }
        return view
    }
}