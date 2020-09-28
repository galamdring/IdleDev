package com.galamdring.idledev

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.galamdring.idledev.database.WidgetRepository
import com.galamdring.idledev.database.WorkerRepository
import com.galamdring.idledev.databinding.FragmentWidgetsBinding

/**
 * A simple [Fragment] subclass.
 * Use the [WidgetsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WidgetsFragment : Fragment() {

    lateinit var stateSaver: StateSaver
    lateinit var widgetRepo: WidgetRepository
    lateinit var workerRepo: WorkerRepository
    lateinit var workerViewModel: WidgetViewModel
    private lateinit var widgetsMan: WidgetsManager

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        workerViewModel.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        setupView(savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        workerViewModel = ViewModelProvider(this).get(WidgetViewModel::class.java)
        workerViewModel.initDB()

        val binding: FragmentWidgetsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_widgets, container, false)
        binding.viewModel = workerViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        workerViewModel.amateursLive.observe(viewLifecycleOwner, {})
        workerViewModel.novicesLive.observe(viewLifecycleOwner, {})
        workerViewModel.apprenticesLive.observe(viewLifecycleOwner, {})
        workerViewModel.mastersLive.observe(viewLifecycleOwner, {})
        workerViewModel.widgetsLive.observe(viewLifecycleOwner, {})

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        widgetsMan = WidgetsManager(workerViewModel)
        stateSaver = StateSaver(workerViewModel)
        workerRepo = WorkerRepository(requireActivity().application)
        widgetRepo = WidgetRepository(requireActivity().application)
        setupView(savedInstanceState)
    }

    fun setupView(savedInstanceState: Bundle?) {

        workerViewModel.loadState(savedInstanceState)
        // Initialize text.

        postDelayed(stateSaver, 10000)
        postDelayed(widgetsMan, 500)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Widgets.
         */

        private val mainHandler = Handler(Looper.getMainLooper())

        @JvmStatic
        fun newInstance() =
            WidgetsFragment().apply {
                arguments = Bundle().apply {
                }
            }

        fun postDelayed(runner: Runnable, delay: Long) {
            mainHandler.postDelayed(runner, delay)
        }
    }
}
