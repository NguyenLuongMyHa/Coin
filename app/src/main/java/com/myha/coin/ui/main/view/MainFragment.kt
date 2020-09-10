package com.myha.coin.ui.main.view

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.myha.coin.R
import com.myha.coin.data.api.ApiHelper
import com.myha.coin.data.api.RetrofitBuilder
import com.myha.coin.data.db.AnimalDatabase
import com.myha.coin.data.model.Animal
import com.myha.coin.ui.base.AnimalVMFactory
import com.myha.coin.ui.main.adapter.AnimalAdapter
import com.myha.coin.ui.main.viewmodel.AnimalViewModel
import com.myha.coin.utils.Status
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {
    private lateinit var viewModel: AnimalViewModel
    private lateinit var adapter: AnimalAdapter
    var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        setHasOptionsMenu(true)
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setupUI()
        getCoinsFromLocal()
        setupAdapterClickListener()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_add -> {
                navController!!.navigate(R.id.action_mainFragment_to_newCoinFragment)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return false
    }
    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            AnimalVMFactory(
                ApiHelper(RetrofitBuilder.apiService), AnimalDatabase.getInstance(
                    requireContext()
                )
            )
        )[AnimalViewModel::class.java]
    }

    private fun setupUI() {
        toolbar.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = AnimalAdapter(arrayListOf())
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter
    }

    private fun setupAdapterClickListener() {
        adapter.setUpListener(object : AnimalAdapter.ItemCLickedListener {
            override fun onItemClicked(animal: Animal) {
                val bundle = bundleOf(
                    "animal" to animal
                )
                navController!!.navigate(
                    R.id.action_mainFragment_to_coinDetailFragment,
                    bundle
                )
            }

        })
    }
    private fun getCoinsFromLocal() {
        viewModel.getAnimalLocal().observe(requireActivity(), {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Get Data From Room Success",
                            Toast.LENGTH_LONG
                        ).show()
                        resource.data?.let { res -> retrieveList(res) }
                    }
                    Status.ERROR -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Get Data From Room Fail",
                            Toast.LENGTH_LONG
                        ).show()
                        Toast.makeText(requireContext(), "Get Data From NetWork", Toast.LENGTH_LONG)
                            .show()

                        getCoinsFromNetwork()

                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    }
                }
            }
        })

    }

    private fun getCoinsFromNetwork() {
        viewModel.getAnimalsNetwork().observe(requireActivity(), {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Get Data From Network Success",
                            Toast.LENGTH_LONG
                        ).show()
                        resource.data?.let { res -> retrieveListFromNetwork(res.animals) }
                    }
                    Status.ERROR -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                        Log.e("MYHA", it.message.toString())
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun retrieveList(animals: List<Animal>) {
        if(animals.isEmpty())
            getCoinsFromNetwork()
        else
        {
            adapter.apply {
                addAnimals(animals)
                notifyDataSetChanged()
            }
        }
    }

    private fun retrieveListFromNetwork(animals: List<Animal>) {

        viewModel.insertAllLocal(animals).observe(requireActivity(), {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(
                            requireContext(),
                            "Get Animals Data From Network Success, Save Room",
                            Toast.LENGTH_LONG
                        ).show()
                        getCoinsFromLocal()
                    }
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                    else -> Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

}

