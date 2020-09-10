package com.galamdring.idledev

import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.galamdring.idledev.database.WorkerDatabase
import kotlinx.android.synthetic.main.fragment_widgets.*
import kotlin.math.ceil
import androidx.databinding.DataBindingUtil
import com.galamdring.idledev.databinding.FragmentWidgetsBinding




// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [WidgetsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WidgetsFragment : Fragment(), UiUpdater {

    fun Button.DisableIf(condition: Boolean){
        if (condition) {
            helpers.markButtonDisable(this, requireContext())
        } else {
            helpers.markButtonEnabled(this, requireContext())
        }

    }



    private val widgetCountSavedInstanceKey = "widgetCount"
    private val amateurCountSavedInstanceKey = "amateurCount"
    private val apprenticeCountSavedInstanceKey = "apprenticeCount"
    private val noviceCountSavedInstanceKey = "noviceCount"
    private val masterCountSavedInstanceKey = "masterCount"

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putDouble(widgetCountSavedInstanceKey, widgetsMan.widgetCount)
        outState.putDouble(amateurCountSavedInstanceKey, widgetsMan.amateurCount)
        outState.putDouble(apprenticeCountSavedInstanceKey, widgetsMan.apprenticeCount)
        outState.putDouble(noviceCountSavedInstanceKey, widgetsMan.noviceCount)
        outState.putDouble(masterCountSavedInstanceKey, widgetsMan.masterCount)

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        setupView(savedInstanceState)
    }


    private val mainHandler = Handler(Looper.getMainLooper())
    private lateinit var widgetsMan : WidgetsManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        widgetsMan = WidgetsManager(requireActivity().application,
            this,
            WorkerDatabase.getInstance(requireActivity().application).workerDao
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentWidgetsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_widgets, container, false)
        // Inflate the layout for this fragment
        val application = requireNotNull(this.activity).application
        val dataSource = WorkerDatabase.getInstance(application).workerDao
        val viewModelFactory = WorkerViewModelFactory(dataSource, application, this)
        val workerViewModel =
            ViewModelProvider(
                this, viewModelFactory).get(WidgetsManager::class.java)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(savedInstanceState)
    }



    fun setupView(savedInstanceState: Bundle?){

        widgetsMan.getDbWorkersOrCreateNew()


        if (savedInstanceState!=null){
            widgetsMan.widgetCount = savedInstanceState.getDouble(widgetCountSavedInstanceKey)
            widgetsMan.amateurCount = savedInstanceState.getDouble(amateurCountSavedInstanceKey)
            widgetsMan.noviceCount = savedInstanceState.getDouble(noviceCountSavedInstanceKey)
            widgetsMan.apprenticeCount = savedInstanceState.getDouble(apprenticeCountSavedInstanceKey)
            widgetsMan.masterCount = savedInstanceState.getDouble(masterCountSavedInstanceKey)
        }
        // Initialize text.
        updateWidgetCount(helpers.formatNumbers(widgetsMan.widgetCount))
        updateNoviceCount(helpers.formatNumbers(widgetsMan.noviceCount))
        updateApprenticeCount(helpers.formatNumbers(widgetsMan.apprenticeCount))
        updateMasterCount(helpers.formatNumbers(widgetsMan.masterCount))
        updateMasterSpeed(helpers.formatNumbers(widgetsMan.masters.totalSpeed()))
        updateApprenticeSpeed(helpers.formatNumbers(widgetsMan.apprentices.totalSpeed()))
        updateNoviceSpeed(helpers.formatNumbers(widgetsMan.novices.totalSpeed()))
        updateButtons()

        widgetProduceButton.setOnClickListener{
            widgetsMan.widgetCount+=1
            widgetsProducedCountTV.text = helpers.formatNumbers(widgetsMan.widgetCount);
        }

        amateurBuySingle.setOnClickListener{
            if (widgetsMan.amateurs.purchase(1)){
                updateAmateurCount(helpers.formatNumbers(widgetsMan.amateurs.count))
                updateAmateurSpeed(helpers.formatNumbers(widgetsMan.amateurs.totalSpeed()))
                updateButtons()
            }
        }

        amateurBuySet.setOnClickListener{
            if (widgetsMan.amateurs.purchase(widgetsMan.amateurs.countToSet())){
                updateAmateurCount(helpers.formatNumbers(widgetsMan.amateurs.count))
                updateAmateurSpeed(helpers.formatNumbers(widgetsMan.amateurs.totalSpeed()))
                updateButtons()
            }
        }

        noviceBuySingle.setOnClickListener{
            if (widgetsMan.novices.purchase(1)){
                updateNoviceCount(helpers.formatNumbers(widgetsMan.novices.count))
                updateNoviceSpeed(helpers.formatNumbers(widgetsMan.novices.totalSpeed()))
                updateButtons()
            }
        }

        noviceBuySet.setOnClickListener{
            if (widgetsMan.novices.purchase(widgetsMan.novices.countToSet())){
                updateNoviceCount(helpers.formatNumbers(widgetsMan.novices.count))
                updateNoviceSpeed(helpers.formatNumbers(widgetsMan.novices.totalSpeed()))
                updateButtons()
            }
        }

        apprenticeBuySingle.setOnClickListener{
            if (widgetsMan.apprentices.purchase(1)){
                updateApprenticeCount(helpers.formatNumbers(widgetsMan.apprentices.count))
                updateApprenticeSpeed(helpers.formatNumbers(widgetsMan.apprentices.totalSpeed()))
                updateButtons()
            }
        }
        apprenticeBuySet.setOnClickListener{
            if (widgetsMan.apprentices.purchase(widgetsMan.apprentices.countToSet())){
                updateApprenticeCount(helpers.formatNumbers(widgetsMan.apprentices.count))
                updateApprenticeSpeed(helpers.formatNumbers(widgetsMan.apprentices.totalSpeed()))
                updateButtons()
            }
        }

        masterBuySingle.setOnClickListener{
            if (widgetsMan.masters.purchase(1)){
                masterCountTV.text = widgetsMan.masters.count.toString()
                masterSpeedTV.text =
                    resources.getString(R.string.novice_speed).format(helpers.formatNumbers(widgetsMan.masters.totalSpeed()))
                masterBuySingle.text = resources.getString(R.string.novice_buy_single_button).format(helpers.formatNumbers(widgetsMan.masters.cost))
                masterBuySet.text = resources.getString(R.string.novice_buy_set_button).format(widgetsMan.masters.countToSet(), helpers.formatNumbers(widgetsMan.masters.priceToSet()))
            }
        }

        masterBuySet.setOnClickListener{
            if (widgetsMan.masters.purchase(widgetsMan.masters.countToSet())){
                masterCountTV.text = widgetsMan.masters.count.toString()
                masterSpeedTV.text =
                    resources.getString(R.string.novice_speed).format(helpers.formatNumbers(widgetsMan.masters.totalSpeed()))
                masterBuySingle.text = resources.getString(R.string.novice_buy_single_button).format(helpers.formatNumbers(widgetsMan.masters.cost))
                masterBuySet.text = resources.getString(R.string.novice_buy_set_button).format(widgetsMan.masters.countToSet(), helpers.formatNumbers(widgetsMan.masters.priceToSet()))
            }
        }

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
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            WidgetsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun updateButtons(){
        amateurBuySingle.text = resources.getString(R.string.novice_buy_single_button).
            format(helpers.formatNumbers(widgetsMan.amateurs.cost))
        amateurBuySingle.DisableIf(widgetsMan.amateurs.cost > widgetsMan.widgetCount)

        amateurBuySet.text = resources.getString(R.string.novice_buy_set_button).
            format(widgetsMan.amateurs.countToSet(),
                helpers.formatNumbers(ceil(widgetsMan.amateurs.priceToSet())))
        amateurBuySet.DisableIf(widgetsMan.amateurs.priceToSet() > widgetsMan.widgetCount)

        noviceBuySingle.text = resources.getString(R.string.novice_buy_single_button).
            format(helpers.formatNumbers(widgetsMan.novices.cost))
        noviceBuySingle.DisableIf(widgetsMan.novices.cost > widgetsMan.widgetCount)

        noviceBuySet.text = resources.getString(R.string.novice_buy_set_button).
            format(widgetsMan.novices.countToSet(),
                helpers.formatNumbers(ceil(widgetsMan.novices.priceToSet())))
        noviceBuySet.DisableIf(widgetsMan.novices.priceToSet() > widgetsMan.widgetCount)

        apprenticeBuySingle.text = resources.getString(R.string.novice_buy_single_button).
            format(helpers.formatNumbers(widgetsMan.apprentices.cost))
        apprenticeBuySet.text = resources.getString(R.string.novice_buy_set_button).
            format(widgetsMan.apprentices.countToSet(),
                helpers.formatNumbers(ceil(widgetsMan.apprentices.priceToSet())))
        masterBuySingle.text = resources.getString(R.string.novice_buy_single_button).
            format(helpers.formatNumbers(widgetsMan.masters.cost))
        masterBuySet.text = resources.getString(R.string.novice_buy_set_button).
            format(widgetsMan.masters.countToSet(),
                helpers.formatNumbers(ceil(widgetsMan.masters.priceToSet())))
    }

    override fun updateAmateurCount(count: String) {
        amateurCountTV.text = count
    }

    override fun updateAmateurSpeed(speed: String) {
        amateurSpeedTV.text = resources.getString(R.string.novice_speed).format(speed)
    }

    override fun updateNoviceCount(count: String) {
        noviceCountTV.text = count
    }

    override fun updateNoviceSpeed(speed: String) {
        noviceSpeedTV.text = resources.getString(R.string.novice_speed).format(speed)
    }

    override fun updateApprenticeCount(count: String) {
        apprenticeCountTV.text = count
    }

    override fun updateApprenticeSpeed(speed: String) {
        apprenticeSpeedTV.text = resources.getString(R.string.novice_speed).format(speed)
    }

    override fun updateWidgetCount(count: String) {
        widgetsProducedCountTV.text = count
    }

    override fun updateMasterCount(count: String) {
        masterCountTV.text = count
    }

    override fun updateMasterSpeed(speed: String) {
        masterSpeedTV.text = resources.getString(R.string.novice_speed).format(speed)
    }

    override fun postDelayed(runner: Runnable, delay: Long) {
        mainHandler.postDelayed(runner, delay)
    }

    override fun getApplication(): Application? {
        return requireNotNull(this.activity).application
    }
}