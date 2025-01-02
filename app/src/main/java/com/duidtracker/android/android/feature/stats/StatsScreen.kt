package com.duidtracker.android.android.feature.stats

import android.view.LayoutInflater
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.duidtracker.android.android.feature.home.TransactionList
import com.duidtracker.android.android.utils.Utils
import com.duidtracker.android.android.R
import com.duidtracker.android.android.widget.ExpenseTextView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet

@Composable
fun StatsScreen(navController: NavController, viewModel: StatsViewModel = hiltViewModel()) {
    Scaffold(topBar = {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clickable { navController.navigateUp() },
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.outline)
            )
            ExpenseTextView(
                text = "Statistics",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Center)
            )
        }
    }) {
        val expenseDataState = viewModel.entries.collectAsState(emptyList())
        val topExpenseState = viewModel.topEntries.collectAsState(emptyList())

        Column(modifier = Modifier.padding(it)) {
            val chartEntries = viewModel.getEntriesForChart(expenseDataState.value)

            if (chartEntries.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ExpenseTextView(text = "No data available", fontSize = 16.sp)
                }
            } else {
                LineChart(entries = chartEntries)
            }

            Spacer(modifier = Modifier.height(16.dp))
            TransactionList(
                Modifier,
                list = topExpenseState.value,
                title = "Top Spending",
                onSeeAllClicked = {}
            )
        }
    }
}

@Composable
fun LineChart(entries: List<Entry>) {
    val context = LocalContext.current
    AndroidView(
        factory = {
            val view = LayoutInflater.from(context).inflate(R.layout.stats_line_chart, null)
            view
        }, modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) { view ->
        val lineChart = view.findViewById<LineChart>(R.id.lineChart)

        val dataSet = LineDataSet(entries, "Expenses").apply {
            color = android.graphics.Color.parseColor("#FF2F7E79")
            valueTextColor = android.graphics.Color.BLACK
            lineWidth = 3f
            axisDependency = YAxis.AxisDependency.RIGHT
            setDrawFilled(true)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            valueTextSize = 12f
            valueTextColor = android.graphics.Color.parseColor("#FF2F7E79")
            val drawable = ContextCompat.getDrawable(context, R.drawable.char_gradient)
            drawable?.let {
                fillDrawable = it
            }
        }

        lineChart.xAxis.valueFormatter =
            object : com.github.mikephil.charting.formatter.ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return Utils.formatDateForChart(value.toLong())
                }
            }
        lineChart.data = com.github.mikephil.charting.data.LineData(dataSet)
        lineChart.axisLeft.isEnabled = false
        lineChart.axisRight.isEnabled = true
        lineChart.axisRight.setDrawGridLines(true)
        lineChart.axisRight.textColor = android.graphics.Color.GRAY
        lineChart.axisRight.textSize = 10f
        lineChart.xAxis.setDrawGridLines(true)
        lineChart.xAxis.gridColor = android.graphics.Color.LTGRAY
        lineChart.xAxis.setDrawAxisLine(false)
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.setTouchEnabled(true)
        lineChart.isDragEnabled = true
        lineChart.setScaleEnabled(true)
        lineChart.setPinchZoom(true)

        // Mengganti properti dengan metode untuk highlight
        lineChart.setHighlightPerTapEnabled(true)

        lineChart.invalidate()
    }
}
