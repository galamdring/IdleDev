package com.galamdring.idledev

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.galamdring.idledev.database.WidgetRepository
import com.galamdring.idledev.database.WorkerRepository
import com.galamdring.idledev.databinding.FragmentWidgetsBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

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

    override fun onDestroy() {
        stateSaver.once = true
        stateSaver.run()
        stateSaver.once = false
        super.onDestroy()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        setupView(savedInstanceState)
    }

    private var _binding: FragmentWidgetsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        workerViewModel = ViewModelProvider(this).get(WidgetViewModel::class.java)
        workerViewModel.initDB()

        binding.viewModel = workerViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        workerViewModel.amateursLive.observe(viewLifecycleOwner) {}
        workerViewModel.novicesLive.observe(viewLifecycleOwner) {}
        workerViewModel.apprenticesLive.observe(viewLifecycleOwner) {}
        workerViewModel.mastersLive.observe(viewLifecycleOwner) {}
        workerViewModel.widgetsLive.observe(viewLifecycleOwner) {}

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // The floating action button was clicked, buy what we can.
    fun buyAll() {
        if (this::workerViewModel.isInitialized) {
            workerViewModel.buyAll()
        }
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

        postDelayed(stateSaver, stateSaverDelay)
        postDelayed(widgetsMan, widgetProductionDelay)

        if (AppConfig.PRODUCT_FLAVOR == "free") {
            val adView = AdView(requireContext())
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL
            adView.layoutParams = layoutParams
            adView.adSize = AdSize.BANNER
            adView.adUnitId = DebugConfig.adMobId
            binding.adLayout.addView(adView)
            adView.loadAd(AdRequest.Builder().build())
        }

        if (DebugConfig.DEBUG_VERSION) {
            binding.resetButton.setOnClickListener(workerViewModel.clickListener)
        } else {
            binding.resetButton.visibility = View.GONE
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment Widgets.
         */

        private val mainHandler = Handler(Looper.getMainLooper())
        const val stateSaverDelay = 1000000.toLong()
        const val widgetProductionDelay = 500.toLong()

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
