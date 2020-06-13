package com.bael.kirin

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.os.RemoteException


/**
 * Created by ErickSumargo on 01/06/20.
 */

class DialogActivity : Activity() {

    private lateinit var broadcastReceiver: BroadcastReceiver

    private var mService: Messenger? = null
    private var bound: Boolean = false

    private val mConnection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            mService = Messenger(service)
            bound = true
        }

        override fun onServiceDisconnected(className: ComponentName) {
            mService = null
            bound = false
        }
    }

    fun sayHello(code: Int) {
        if (!bound) return
        // Create and send a message to the service, using a supported 'what' value
        val msg: Message = Message.obtain(null, code, 0, 0)
        try {
            mService?.send(msg)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    override fun onStart() {
        super.onStart()
        // Bind to the service
        Intent(this, TranslationService::class.java).also { intent ->
            bindService(intent, mConnection, BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        // Unbind from the service
        sayHello(SUBJECT_DISMISS_DIALOG)
        if (bound) {
            unbindService(mConnection)
            bound = false
        }
        closeDialog()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        broadcastReceiver = DialogBroadcastReceiver { onStop() }

        val intentFilter = IntentFilter(SUBJECT_DISMISS_DIALOG.hashCode().toString())
        registerReceiver(broadcastReceiver, intentFilter)
    }

    fun closeDialog() {
        finish()
        overridePendingTransition(0, 0)
    }

    override fun onDestroy() {
        unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }
}
