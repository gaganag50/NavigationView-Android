package com.gaganag50.myapplication


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import java.io.PrintWriter
import java.io.StringWriter

class ErrorActivity : AppCompatActivity() {

    companion object {
        val ERROR_INFO = "error_info"
        val ERROR_LIST = "error_list"
        fun reportUiError(activity: AppCompatActivity, el: Throwable) {
            reportError(
                activity, listOf(el), activity.javaClass, null,
                ErrorInfo.make(UserAction.UI_ERROR, "none", "", R.string.app_ui_crash)
            )
        }

        private fun reportError(
            context: Context,
            el: List<Throwable>,
            returnActivity: Class<AppCompatActivity>,
            rootView: View?,
            errorInfo: ErrorInfo
        ) {

            if (rootView != null) {
                Snackbar.make(rootView, R.string.error_snackbar_message, 3 * 1000)
                    .setActionTextColor(Color.YELLOW)
                    .setAction(
                        R.string.error_snackbar_action
                    ) { v -> startErrorActivity(returnActivity, context, errorInfo, el) }.show()
            } else {
                startErrorActivity(returnActivity, context, errorInfo, el)
            }

        }

        private fun startErrorActivity(
            returnActivity: Class<AppCompatActivity>,
            context: Context,
            errorInfo: ErrorInfo,
            el: List<Throwable>
        ) {
            val ac = ActivityCommunicator.communicator
            ac.returnActivity = returnActivity
            val intent = Intent(context, ErrorActivity::class.java)
            intent.putExtra(ERROR_INFO, errorInfo)
            intent.putExtra(ERROR_LIST, elToSl(el))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }


        private fun elToSl(stackTraces: List<Throwable>): Array<String?> {
            val out = arrayOfNulls<String>(stackTraces.size)
            for (i in stackTraces.indices) {
                out[i] = getStackTrace(stackTraces[i])
            }
            return out
        }

        private fun getStackTrace(throwable: Throwable): String {
            val sw = StringWriter()
            val pw = PrintWriter(sw, true)
            throwable.printStackTrace(pw)
            return sw.buffer.toString()
        }

        class ErrorInfo : Parcelable {
            val userAction: UserAction
            val request: String?
            val serviceName: String?
            @StringRes
            val message: Int

            private constructor(userAction: UserAction, serviceName: String, request: String, @StringRes message: Int) {
                this.userAction = userAction
                this.serviceName = serviceName
                this.request = request
                this.message = message
            }

            protected constructor(`in`: Parcel) {
                this.userAction = UserAction.valueOf(`in`.readString())
                this.request = `in`.readString()
                this.serviceName = `in`.readString()
                this.message = `in`.readInt()
            }

            override fun describeContents(): Int {
                return 0
            }

            override fun writeToParcel(dest: Parcel, flags: Int) {
                dest.writeString(this.userAction.name)
                dest.writeString(this.request)
                dest.writeString(this.serviceName)
                dest.writeInt(this.message)
            }

            companion object {
                @JvmField
                val CREATOR: Parcelable.Creator<ErrorInfo> = object : Parcelable.Creator<ErrorInfo> {
                    override fun createFromParcel(source: Parcel): ErrorInfo {
                        return ErrorInfo(source)
                    }

                    override fun newArray(size: Int): Array<ErrorInfo?> {
                        return arrayOfNulls<ErrorInfo>(size)
                    }
                }

                fun make(
                    userAction: UserAction,
                    serviceName: String,
                    request: String, @StringRes message: Int
                ): ErrorInfo {
                    return ErrorInfo(userAction, serviceName, request, message)
                }
            }
        }

    }
}
