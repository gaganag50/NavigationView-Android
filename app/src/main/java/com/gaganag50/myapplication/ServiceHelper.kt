package com.gaganag50.myapplication

import android.content.Context

public class ServiceHelper {

    companion object {

        fun getTranslatedFilterString(filter: String, c: Context): String {
            when (filter) {
                "all" -> return c.getString(R.string.all)
                "videos" -> return c.getString(R.string.videos)
                "channels" -> return c.getString(R.string.channels)
                "playlists" -> return c.getString(R.string.playlists)
                "tracks" -> return c.getString(R.string.tracks)
                "users" -> return c.getString(R.string.users)
                else -> return filter
            }
        }


    }

}