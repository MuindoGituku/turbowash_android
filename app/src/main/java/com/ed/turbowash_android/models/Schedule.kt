package com.ed.turbowash_android.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class Schedule(
    var id:String = "",
    @get:PropertyName("available_for_hire") @set:PropertyName("available_for_hire") var availableForHire: Boolean = true,
    @get:PropertyName("schedule_day") @set:PropertyName("schedule_day") var scheduleDay:Timestamp = Timestamp.now(),
    @get:PropertyName("service_provider") @set:PropertyName("service_provider") var serviceProviderID: String = "",
    @get:PropertyName("schedule_period") @set:PropertyName("schedule_period") var schedulePeriod: SchedulePeriod = SchedulePeriod(
        Timestamp.now(), Timestamp.now()),
    @get:PropertyName("services_offered") @set:PropertyName("services_offered") var servicesOffered:MutableList<String> = mutableListOf(),
    @get:PropertyName("schedule_map_region") @set:PropertyName("schedule_map_region") var scheduleMapRegion: MutableList<City> = mutableListOf()
)

data class SchedulePeriod(
    @get:PropertyName("start_time") @set:PropertyName("start_time") var startTime:Timestamp = Timestamp.now(),
    @get:PropertyName("end_time") @set:PropertyName("end_time") var endTime:Timestamp = Timestamp.now(),
)

data class ScheduleLocal (
    var id: String = "",
    var scheduleDay: Timestamp = Timestamp.now(),
    var schedulePeriod: SchedulePeriod = SchedulePeriod(Timestamp.now(), Timestamp.now())
)