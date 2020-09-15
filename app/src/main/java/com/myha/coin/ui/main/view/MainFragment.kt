package com.myha.coin.ui.main.view

import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.myha.coin.R
import com.myha.coin.data.api.ApiHelper
import com.myha.coin.data.api.RetrofitBuilder
import com.myha.coin.data.db.AnimalDatabase
import com.myha.coin.data.model.Animal
import com.myha.coin.ui.base.AnimalVMFactory
import com.myha.coin.ui.main.adapter.AnimalLoadStateAdapter
import com.myha.coin.ui.main.adapter.AnimalPagerAdapter
import com.myha.coin.ui.main.viewmodel.AnimalViewModel
import com.myha.coin.utils.Status
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
class MainFragment : Fragment() {
    private lateinit var viewModel: AnimalViewModel
    private val adapterPaging = AnimalPagerAdapter()
    private var searchJob: Job? = null
    var navController: NavController? = null
    private val rotateAnimation = RotateAnimation(
        0F,
        359F,
        Animation.RELATIVE_TO_SELF,
        0.5f,
        Animation.RELATIVE_TO_SELF,
        0.5f
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        setHasOptionsMenu(true)
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setupViewModel()
        setupUI()
        initAdapter()
        setupAdapterClickListener()
        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        search(query)
        initSearch(query)
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
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putString(LAST_SEARCH_QUERY, searchView.query.trim().toString())
//    }

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
        rotateAnimation.duration = 1000

        toolbar.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )

        btn_refresh.setOnClickListener {
            //search("")
            updateAnimalListFromInput()
            it.startAnimation(rotateAnimation)
        }
    }

    private fun initAdapter() {
        recyclerView.adapter = adapterPaging.withLoadStateHeaderAndFooter(
            header = AnimalLoadStateAdapter { adapterPaging.retry() },
            footer = AnimalLoadStateAdapter { adapterPaging.retry() }
        )
//        adapterPaging.addLoadStateListener { loadState ->
//            // Only show the list if refresh succeeds.
//            recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
//            // Show loading spinner during initial load or refresh.
//            progressBar.isVisible = loadState.source.refresh is LoadState.Loading
//
//            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
//            val errorState = loadState.source.append as? LoadState.Error
//                ?: loadState.source.prepend as? LoadState.Error
//                ?: loadState.append as? LoadState.Error
//                ?: loadState.prepend as? LoadState.Error
//            errorState?.let {
//                Toast.makeText(
//                    requireContext(),
//                    "\uD83D\uDE28 Wooops ${it.error}",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//        }
    }

    private fun search(query: String) {
        // cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchAnimal2(query).collectLatest {
                adapterPaging.submitData(it)
            }
        }
    }

    private fun initSearch(query: String) {
        searchView.setQuery(query, false)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                updateAnimalListFromInput()
                return false
            }
        })

        // Scroll to top when the list is refreshed from network.
        lifecycleScope.launch {
            adapterPaging.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect { recyclerView.scrollToPosition(0) }
        }
    }

    private fun updateAnimalListFromInput() {
        searchView.query.trim().let {
            if (it.isNotEmpty()) {
                search(it.toString())
            }
        }
    }

    private fun setupAdapterClickListener() {
        adapterPaging.setUpListener(object : AnimalPagerAdapter.ItemCLickedListener {
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

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = ""
    }
}

/*

    private fun searchAnimalNetwork(queryString: String) {
        viewModel.findAnimalsByTypeNetwork(queryString).observe(requireActivity(), {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Find Pet From Network Success",
                            Toast.LENGTH_LONG
                        ).show()
                        resource.data?.let { res -> retrieveList(res.animals) }
                    }
                    Status.ERROR -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Find Pet From Network Fail",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    }
                }
            }
        })

    }

    private fun getPetsFromLocal() {
        viewModel.getAnimalLocal().observe(requireActivity(), {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Get Pet From Room Success",
                            Toast.LENGTH_LONG
                        ).show()
                        resource.data?.let { res -> retrieveList(res) }
                    }
                    Status.ERROR -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Get Pet From Room Fail",
                            Toast.LENGTH_LONG
                        ).show()
                        getPetsFromNetwork()

                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    }
                }
            }
        })

    }

    private fun getPetsFromNetwork() {
        viewModel.getAnimalsNetwork().observe(requireActivity(), {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Get Pet From Network Success",
                            Toast.LENGTH_LONG
                        ).show()
                        resource.data?.let { res -> retrieveListFromNetwork(res.animals) }
                    }
                    Status.ERROR -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                        Toast.makeText(
                            requireContext(),
                            "Get Pet From Network Fail",
                            Toast.LENGTH_LONG
                        ).show()
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
                getPetsFromNetwork()
            else
            {
//            adapter.apply {
//                addAnimals(animals)
//                notifyDataSetChanged()
//            }
            }
    }

    private fun retrieveListFromNetwork(animals: List<Animal>) {
        viewModel.insertAllLocal(animals).observe(requireActivity(), {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(
                            requireContext(),
                            "Save Room Success",
                            Toast.LENGTH_LONG
                        ).show()
                        getPetsFromLocal()
                    }
                    Status.ERROR -> {
                        Toast.makeText(
                            requireContext(),
                            "Save Room Fail",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else -> {
                    }
                }
            }
        })
    }


    private fun search2(query: String) {
        btn_refresh.startAnimation(rotateAnimation)
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchAnimal(query).observe(requireActivity(), {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            recyclerView.visibility = View.VISIBLE
                            progressBar.visibility = View.GONE
                            lifecycleScope.launch {
                                resource.data?.collectLatest {
                                    adapterPaging.submitData(it)
                                }
                            }
                        }
                        Status.ERROR -> {
                            recyclerView.visibility = View.VISIBLE
                            progressBar.visibility = View.GONE
                        }
                        Status.LOADING -> {
                            progressBar.visibility = View.VISIBLE
                            recyclerView.visibility = View.GONE
                        }
                    }
                }
            })

        }


    }


     */