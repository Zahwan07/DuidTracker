package com.duidtracker.android.android.feature.stats

import com.duidtracker.android.android.base.BaseViewModel
import com.duidtracker.android.android.base.UiEvent
import com.duidtracker.android.android.utils.Utils
import com.duidtracker.android.android.data.dao.ExpenseDao
import com.duidtracker.android.android.data.model.ExpenseSummary
import com.github.mikephil.charting.data.Entry
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(val dao: ExpenseDao) : BaseViewModel() {
    // Data yang diambil dari DAO
    val entries = dao.getAllExpenseByDate()
    val topEntries = dao.getTopExpenses()

    fun getEntriesForChart(entries: List<ExpenseSummary>): List<Entry> {
        val sortedEntries = entries.sortedBy { Utils.getMillisFromDate(it.date) }
        val chartEntries = mutableListOf<Entry>()
        var cumulativeSum = 0f

        for (entry in sortedEntries) {
            val formattedDate = Utils.getMillisFromDate(entry.date)
            // Hitung kumulasi dengan mempertimbangkan pemasukan atau pengeluaran
            cumulativeSum += entry.total_amount.toFloat()
            chartEntries.add(Entry(formattedDate.toFloat(), cumulativeSum))
        }
        return chartEntries
    }

    override fun onEvent(event: UiEvent) {
        // Handle UI events jika diperlukan
    }
}
