package com.ed.turbowash_android.models

import com.google.firebase.Timestamp

data class Schedule(
    var id:String = "",
    var scheduleDay:Timestamp,
    var serviceProviderID: String,
    var schedulePeriod: SchedulePeriod,
    var servicesOffered:MutableList<String>,
)

data class SchedulePeriod(
    var startTime:Timestamp,
    var endTime:Timestamp,
)
