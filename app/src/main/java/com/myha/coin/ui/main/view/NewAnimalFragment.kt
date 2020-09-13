package com.myha.coin.ui.main.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.myha.coin.R
import com.myha.coin.data.api.ApiHelper
import com.myha.coin.data.api.RetrofitBuilder
import com.myha.coin.data.db.AnimalDatabase
import com.myha.coin.data.model.Animal
import com.myha.coin.data.model.NewAnimal
import com.myha.coin.ui.base.AnimalVMFactory
import com.myha.coin.ui.main.viewmodel.AnimalViewModel
import com.myha.coin.utils.Status
import kotlinx.android.synthetic.main.fragment_new_coin.*


class NewAnimalFragment : Fragment() {
    private lateinit var viewModel: AnimalViewModel
    private var navController: NavController? = null
    private var animal : Animal? = null
    private var action : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        animal = arguments?.getSerializable("animal") as Animal?
        action = arguments?.getString("action")
        setupViewModel()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_new_coin, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setupUI()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_new_coin, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_save -> {
                if (action != "EDIT") {
                    val animal = NewAnimal()
                    animal.description = tv_description.text.toString()
                    animal.name = tv_name.text.toString()
                    animal.size = tv_size.text.toString()
                    animal.gender = tv_gender.text.toString()
                    animal.coat = tv_coat.text.toString()
                    animal.type = tv_type.text.toString()
                    animal.age = tv_age.text.toString()
                    saveNewAnimal(animal)
                } else {
                    animal?.id?.let {
                        val animal = Animal(it)
                        animal.description = tv_description.text.toString()
                        animal.name = tv_name.text.toString()
                        animal.size = tv_size.text.toString()
                        animal.gender = tv_gender.text.toString()
                        animal.coat = tv_coat.text.toString()
                        animal.type = tv_type.text.toString()
                        animal.age = tv_age.text.toString()
                        updateAnimal(animal)
                    }
                }
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
            navController!!.navigate(R.id.action_newCoinFragment_to_mainFragment)
        }
        toolbar.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        if (action == "EDIT") {
            tv_description.setText(animal?.description)
            tv_name.setText(animal?.name)
            tv_size.setText(animal?.size)
            tv_gender.setText(animal?.gender)
            tv_coat.setText(animal?.coat)
            tv_age.setText(animal?.age)
            tv_type.setText(animal?.type)
        }
    }

    private fun saveNewAnimal(animal: NewAnimal) {
         val tilList = listOf<TextInputLayout>(til_name, til_age, til_description, til_gender, til_size, til_type)
         val tieList = listOf<TextInputEditText>(tv_name, tv_age, tv_description, tv_gender, tv_size, tv_type)

        if(validateEditTextRequireField(tilList, tieList)) {
            viewModel.insertLocal(animal).observe(requireActivity(), {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            progressBar.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                "Save Animal to Room Success",
                                Toast.LENGTH_LONG
                            ).show()
                            navController!!.navigate(R.id.action_newCoinFragment_to_mainFragment)
                        }
                        Status.ERROR -> {
                            progressBar.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                "Save Animal to Room Fail" + resource.message,
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

    private fun validateEditTextRequireField(tilList: List<TextInputLayout>, tieList: List<TextInputEditText>): Boolean {
        var flag = true
        for (index in tilList.indices) {
            val editText: TextInputEditText = tieList[index]
            if(editText.text.toString().isEmpty())
            {
                tilList[index].error = "This field is required."
                flag = false
            }
        }
        return flag
    }

    private fun updateAnimal(animal: Animal) {
        val tilList = listOf<TextInputLayout>(til_name, til_age, til_description, til_gender, til_size, til_type)
        val tieList = listOf<TextInputEditText>(tv_name, tv_age, tv_description, tv_gender, tv_size, tv_type)

        if(validateEditTextRequireField(tilList, tieList)) {
            viewModel.updateLocal(animal).observe(requireActivity(), {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            progressBar.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                "Edit Animal to Room Success",
                                Toast.LENGTH_LONG
                            ).show()
                            navController!!.navigate(R.id.action_newCoinFragment_to_mainFragment)
                        }
                        Status.ERROR -> {
                            progressBar.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                "Edit Animal to Room Fail" + resource.message,
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

}