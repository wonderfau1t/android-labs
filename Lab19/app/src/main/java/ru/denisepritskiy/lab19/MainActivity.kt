package ru.denisepritskiy.lab19

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import ru.denisepritskiy.lab19.databinding.ActivityMainBinding
import ru.denisepritskiy.lab19.databinding.DialogPermissionRationaleBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val contactsList = mutableListOf<String>()
    private lateinit var adapter: ContactAdapter

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            fetchContacts()
        } else {
            handlePermissionDenied()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ContactAdapter(contactsList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.fetchButton.setOnClickListener {
            checkAndRequestPermission()
        }
    }

    private fun checkAndRequestPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED -> {
                fetchContacts()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                showPermissionRationale()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
        }
    }

    private fun showPermissionRationale() {
        val dialogBinding = DialogPermissionRationaleBinding.inflate(LayoutInflater.from(this))
        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setPositiveButton("Понятно") { _, _ ->
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
            .setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss()
                handlePermissionDenied()
            }
            .show()
    }

    private fun handlePermissionDenied() {
        Toast.makeText(
            this,
            "Без разрешения на чтение контактов функционал ограничен",
            Toast.LENGTH_LONG
        ).show()
        binding.permissionMessage.visibility = View.VISIBLE
        binding.permissionMessage.setOnClickListener {
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:$packageName")
            )
            startActivity(intent)
        }
    }

    private fun fetchContacts() {
        contactsList.clear()
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        cursor?.use {
            while (it.moveToNext()) {
                val nameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                if (nameIndex != -1) {
                    val name = it.getString(nameIndex)
                    if (name != null) {
                        contactsList.add(name)
                    }
                }
            }
        }
        if (contactsList.isEmpty()) {
            Toast.makeText(this, "Контакты не найдены", Toast.LENGTH_SHORT).show()
        }
        binding.permissionMessage.visibility = View.GONE
        adapter.notifyDataSetChanged()
    }
}