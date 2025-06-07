import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.denisepritskiy.lab14.CityAdapter
import ru.denisepritskiy.lab14.Common
import ru.denisepritskiy.lab14.R

class CityListFragment : Fragment() {
    interface OnCitySelectedListener {
        fun onCitySelected(index: Int)
    }

    private var listener: OnCitySelectedListener? = null
    private var layoutState: Parcelable? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCitySelectedListener) {
            listener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_city_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = CityAdapter(Common.cities) { index ->
            listener?.onCitySelected(index)
        }

        if (layoutState != null) {
            recyclerView.layoutManager?.onRestoreInstanceState(layoutState)
        }

        return view
    }

    override fun onPause() {
        super.onPause()
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerView)
        layoutState = recyclerView?.layoutManager?.onSaveInstanceState()
    }
}
