package com.gaganag50.myapplication

class ActivityCommunicator {

    @Volatile
    var returnActivity: Class<*>? = null

    companion object {

        private var activityCommunicator: ActivityCommunicator? = null

        val communicator: ActivityCommunicator
            get() {
                if (activityCommunicator == null) {
                    activityCommunicator = ActivityCommunicator()
                }
                return activityCommunicator!!
            }
    }
}
