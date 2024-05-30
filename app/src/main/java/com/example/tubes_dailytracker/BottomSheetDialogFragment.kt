package com.example.tubes_dailytracker

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class LoginBottomSheetFragment : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Replace 'fragment_login_bottom_sheet' with your actual layout resource
        return inflater.inflate(R.layout.fragment_login_bottom_sheet, container, false)
    }
}