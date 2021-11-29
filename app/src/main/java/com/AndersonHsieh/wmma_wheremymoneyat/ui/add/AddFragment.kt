package com.AndersonHsieh.wmma_wheremymoneyat.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.AndersonHsieh.wmma_wheremymoneyat.data.TransactionRepository
import com.AndersonHsieh.wmma_wheremymoneyat.databinding.FragmentAddBinding
import com.AndersonHsieh.wmma_wheremymoneyat.data.TransactionDAO
import com.AndersonHsieh.wmma_wheremymoneyat.util.MyViewModelFactory

class AddFragment : Fragment() {

    private lateinit var addViewModel: AddViewModel
    private var _binding: FragmentAddBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addViewModel = ViewModelProvider(this, MyViewModelFactory(TransactionRepository.getInstance(
            TransactionDAO()
        )))[AddViewModel::class.java]
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}