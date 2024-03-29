package com.galamdring.idledev

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.galamdring.idledev.databinding.ActivityMainBinding
import com.google.android.gms.ads.MobileAds
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * The number of pages (wizard steps) to show in this demo.
 */
private const val NUM_PAGES = 2
private const val QUEUE_SIZE = 5
private const val REPEAT_LISTENER_INTERVAL = 100

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2

    private var titles = arrayOf("Widgets", "Dodads")

    private val purchaserQueueSize = QUEUE_SIZE

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Start the purchaser loop
        GlobalScope.launch { Purchaser.listenForPurchases(purchaserQueueSize) }

        binding = ActivityMainBinding.inflate(layoutInflater)
        // The pager adapter, which provides the pages to the view pager widget.
        val pagerAdapter = ScreenSlidePagerAdapter(this)
        binding.pager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = titles[position]
            binding.pager.setCurrentItem(tab.position, true)
        }.attach()

        /**
         * The pager widget, which handles animation and allows swiping horizontally to access previous
         * and next wizard steps.
         */
        MobileAds.initialize(this) {}
        binding.fab.setOnTouchListener(
            RepeatListener(
                REPEAT_LISTENER_INTERVAL,
                REPEAT_LISTENER_INTERVAL,
                View.OnClickListener {
                    maxButtonOnClick()
                }
            )
        )
    }

    fun maxButtonOnClick() {
        widgetsFragment.buyAll()
    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed()
        } else {
            // Otherwise, select the previous step.
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }

    val widgetsFragment = WidgetsFragment.newInstance()
    val dodadsFragment = DodadsFragment.newInstance()

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> widgetsFragment
                1 -> dodadsFragment
                else -> Fragment()
            }
        }
    }
}
