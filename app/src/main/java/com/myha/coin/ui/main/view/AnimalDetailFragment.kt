package com.myha.coin.ui.main.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.myha.coin.R
import com.myha.coin.data.api.ApiHelper
import com.myha.coin.data.api.RetrofitBuilder
import com.myha.coin.data.db.AnimalDatabase
import com.myha.coin.data.model.Animal
import com.myha.coin.ui.base.AnimalVMFactory
import com.myha.coin.ui.main.viewmodel.AnimalViewModel
import com.myha.coin.utils.Status
import kotlinx.android.synthetic.main.fragment_coin_detail.*

class AnimalDetailFragment : Fragment() {
    private lateinit var viewModel: AnimalViewModel
    private var navController: NavController? = null
    
    
    private var animal : Animal? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        animal = arguments?.getSerializable("animal") as Animal?
        setupViewModel()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_coin_detail, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setupUI()
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_edit -> {
                updateAnimal()
                return true
            }
            R.id.menu_item_delete -> {
                animal?.let { deleteCoin(it) }
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
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener{
            navController!!.navigate(R.id.action_coinDetailFragment_to_mainFragment)
        }
        toolbar.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
//        tv_address.text = animal?.contact.toString()
        tv_age.text = animal?.age
        tv_coat.text = animal?.coat
        tv_description.text = animal?.description
        tv_gender.text = animal?.gender
        tv_name.text = animal?.name
        tv_size.text = animal?.size
        tv_type.text = animal?.type
        tv_address.text = animal?.contact?.address.toString()
        tv_phone.text = animal?.contact?.phone
        tv_email.text = animal?.contact?.email
//        Glide.with(requireContext()).load(animal?.photos?.get(0)?.fullsize).into(img_photo)
    }

    private fun updateAnimal() {
        val bundle = bundleOf(
            "animal" to animal,
            "action" to "EDIT"
        )
        navController!!.navigate(
            R.id.action_coinDetailFragment_to_newCoinFragment,
            bundle
        )
    }


    private fun deleteCoin(animal: Animal) {
        viewModel.deleteLocal(animal).observe(requireActivity(), {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Delete Animal from Room Success",
                            Toast.LENGTH_LONG
                        ).show()
                        navController!!.navigate(R.id.action_coinDetailFragment_to_mainFragment)
                    }
                    Status.ERROR -> {
                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Delete Animal from Room Fail" + resource.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })

    }

}