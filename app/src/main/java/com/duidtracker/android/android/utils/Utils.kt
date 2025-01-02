package com.duidtracker.android.android.utils

import com.duidtracker.android.android.R
import com.duidtracker.android.android.data.model.ExpenseEntity
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {

    /**
     * Converts a timestamp (in milliseconds) to a human-readable date string (dd/MM/yyyy).
     */
    fun formatDateToHumanReadableForm(dateInMillis: Long): String {
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormatter.format(Date(dateInMillis))
    }

    /**
     * Formats a timestamp (in milliseconds) for use in charts (dd-MMM).
     */
    fun formatDateForChart(dateInMillis: Long): String {
        val dateFormatter = SimpleDateFormat("dd-MMM", Locale.getDefault())
        return dateFormatter.format(Date(dateInMillis))
    }

    /**
     * Formats a currency amount based on the given locale.
     */
    fun formatCurrency(amount: Double, locale: Locale = Locale("in", "ID")): String {
        val currencyFormatter = NumberFormat.getCurrencyInstance(locale)
        return currencyFormatter.format(amount)
    }

    /**
     * Converts a timestamp (in milliseconds) to a formatted string (MMM dd, yyyy).
     */
    fun formatDayMonthYear(dateInMillis: Long): String {
        val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return dateFormatter.format(Date(dateInMillis))
    }

    /**
     * Converts a timestamp (in milliseconds) to a day-month formatted string (dd/MM/yyyy).
     */
    fun formatDayMonth(dateInMillis: Long): String {
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormatter.format(Date(dateInMillis))
    }

    /**
     * Formats a double value to two decimal places.
     */
    fun formatToDecimalValue(d: Double): String {
        return String.format("%.2f", d)
    }

    /**
     * Converts a date string (dd/MM/yyyy) to a formatted string (MMM dd, yyyy).
     */
    fun formatStringDateToMonthDayYear(date: String): String {
        val millis = getMillisFromDate(date)
        return formatDayMonthYear(millis)
    }

    /**
     * Converts a date string (dd/MM/yyyy) to a timestamp (in milliseconds).
     */
    fun getMillisFromDate(date: String): Long {
        return getMilliFromDate(date)
    }

    /**
     * Safely converts a date string (dd/MM/yyyy) to a timestamp (in milliseconds).
     */
    fun getMilliFromDate(dateFormat: String?): Long {
        if (dateFormat.isNullOrBlank()) return 0L
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return try {
            val date = formatter.parse(dateFormat)
            date?.time ?: 0L
        } catch (e: ParseException) {
            e.printStackTrace()
            0L
        }
    }

    /**
     * Returns the appropriate drawable resource ID based on the title of the expense item.
     */
    fun getItemIcon(item: ExpenseEntity): Int {
        return when (item.title) {
            "BCA" -> R.drawable.bca
            "BRI" -> R.drawable.bri
            "BSI" -> R.drawable.bsi
            "Cash" -> R.drawable.cash
            "DANA" -> R.drawable.dana
            "OVO" -> R.drawable.ovo
            "Shopeepay" -> R.drawable.shopeepay
            "Gopay" -> R.drawable.gopay
            "Mandiri" -> R.drawable.mandiri
            else -> R.drawable.ic_upwork
        }
    }
}
