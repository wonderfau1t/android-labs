package ru.denisepritskiy.lab15

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ShoppingDialog : DialogFragment() {

    private var item: ShoppingItem? = null
    private var listener: ((ShoppingItem) -> Unit)? = null

    companion object {
        fun newInstance(item: ShoppingItem? = null): ShoppingDialog {
            return ShoppingDialog().apply {
                this.item = item
            }
        }
    }

    fun setOnSaveListener(listener: (ShoppingItem) -> Unit) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_shopping, null)

        // Инициализация EditText через findViewById
        val etName: EditText = view.findViewById(R.id.et_name)
        val etQuantity: EditText = view.findViewById(R.id.et_quantity)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(if (item == null) "Добавить покупку" else "Редактировать покупку")
            .setView(view)
            .setPositiveButton("OK") { _, _ ->
                val name = etName.text.toString()
                val quantity = etQuantity.text.toString()
                if (name.isNotEmpty() && quantity.isNotEmpty()) {
                    listener?.invoke(ShoppingItem(name, quantity))
                }
            }
            .setNegativeButton("ОТМЕНА", null)
            .create()

        item?.let {
            etName.setText(it.name)
            etQuantity.setText(it.quantity)
        }

        return dialog
    }
}