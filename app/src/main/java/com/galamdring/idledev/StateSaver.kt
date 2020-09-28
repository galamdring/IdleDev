package com.galamdring.idledev

class StateSaver(var viewModel: WidgetViewModel) : Runnable {
    var once: Boolean = false
    override fun run() {
        viewModel.saveAll()
        if (!once) {
            WidgetsFragment.postDelayed(this, 10000)
        }
    }
}