package com.galamdring.idledev

class StateSaver(var viewModel: WidgetViewModel) : Runnable {
    override fun run() {
        viewModel.saveAll()
        WidgetsFragment.postDelayed(this, 10000)
    }
}