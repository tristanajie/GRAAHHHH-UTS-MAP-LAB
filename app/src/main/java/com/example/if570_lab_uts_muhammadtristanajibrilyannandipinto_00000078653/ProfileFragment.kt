package com.example.if570_lab_uts_muhammadtristanajibrilyannandipinto_00000078653

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var etName: EditText
    private lateinit var etNim: EditText
    private lateinit var btnSave: Button
    private lateinit var tvEmail: TextView
    private lateinit var btnLogout: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Inisialisasi Firebase Firestore dan Firebase Auth
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Hubungkan UI
        etName = view.findViewById(R.id.et_name)
        etNim = view.findViewById(R.id.et_nim)
        btnSave = view.findViewById(R.id.btn_save)
        tvEmail = view.findViewById(R.id.tv_email) // TextView untuk menampilkan email login
        btnLogout = view.findViewById(R.id.logout) // Button untuk log out

        // Tombol Save untuk menyimpan data ke Firebase Firestore
        btnSave.setOnClickListener {
            saveProfileData()
        }

        // Tombol Logout untuk keluar dari aplikasi
        btnLogout.setOnClickListener {
            auth.signOut() // Proses sign out
            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
            // Arahkan kembali ke halaman login
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish() // Tutup halaman profil agar tidak bisa diakses kembali setelah logout
        }

        // Menampilkan email pengguna yang sedang login
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val email = currentUser.email

            // Buat SpannableString dengan teks "Logged in as: "
            val loggedInText = "Logged in as: "
            val spannableString = SpannableString("$loggedInText$email")

            // Set bagian email menjadi bold
            val boldSpan = StyleSpan(Typeface.BOLD)
            spannableString.setSpan(boldSpan, loggedInText.length, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            // Set SpannableString ke TextView
            tvEmail.text = spannableString
        }

        return view
    }

    private fun saveProfileData() {
        val name = etName.text.toString().trim()
        val nim = etNim.text.toString().trim()

        if (name.isEmpty() || nim.isEmpty()) {
            Toast.makeText(requireContext(), "Name and NIM cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        // Data yang akan disimpan
        val userData = hashMapOf(
            "name" to name,
            "nim" to nim
        )

        // Menyimpan data ke Firebase Firestore dalam koleksi "profiles"
        db.collection("profiles")
            .add(userData)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Profile saved successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Failed to save profile: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
