package com.AndersonHsieh.wmma_wheremymoneyat.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.AndersonHsieh.wmma_wheremymoneyat.data.TransactionRepository
import com.AndersonHsieh.wmma_wheremymoneyat.databinding.FragmentHomeBinding
import com.AndersonHsieh.wmma_wheremymoneyat.model.TransactionDAO
import com.AndersonHsieh.wmma_wheremymoneyat.util.MyViewModelFactory

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TransactionDAO is here for temperary use
        //TODO(Figure out where TransactionDAO should be initialized)
        val factory = MyViewModelFactory(TransactionRepository.getInstance(TransactionDAO()))
        val viewModel: HomeViewModel by lazy {
            //only initialized on first time use of the variable "viewModel"
            ViewModelProvider(this, factory).get(HomeViewModel::class.java)
        }

        //update the actual data in view model
        viewModel.getTransactions();
        //register observer of HomeViewModel, which belongs to HomeFragment
        viewModel.transactions.observe(viewLifecycleOwner, Observer {
            //here the response would be List<Transaction>
            binding.textDashboard.text = it.toString();
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}