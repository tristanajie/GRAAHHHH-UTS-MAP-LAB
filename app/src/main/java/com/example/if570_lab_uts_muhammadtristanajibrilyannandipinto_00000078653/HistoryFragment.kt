package com.example.if570_lab_uts_muhammadtristanajibrilyannandipinto_00000078653

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.if570_lab_uts_muhammadtristanajibrilyannandipinto_00000078653.model.Absensi
import com.example.if570_lab_uts_muhammadtristanajibrilyannandipinto_00000078653.model.AbsensiAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HistoryFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var attendanceAdapter: AbsensiAdapter
    private lateinit var attendanceList: MutableList<Absensi>
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewHistory)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        attendanceList = mutableListOf()
        attendanceAdapter = AbsensiAdapter(attendanceList)
        recyclerView.adapter = attendanceAdapter

        firestore = Firebase.firestore
        getAttendanceRecords()
    }

    private fun getAttendanceRecords() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            firestore.collection("attendance")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { result ->
                    attendanceList.clear()
                    if (!result.isEmpty) {
                        for (document in result) {
                            val attendance = document.toObject(Absensi::class.java)
                            if (attendance.date != null && attendance.time != null) {
                                attendanceList.add(attendance)
                            }
                        }
                        attendanceList.sortByDescending { it.date }
                        attendanceAdapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(requireContext(), "No attendance records found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("HistoryFragment", "Failed to load data: ${exception.message}")
                    Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }
}
