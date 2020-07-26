package com.bael.kirin.lib.arch.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.bael.kirin.lib.logger.contract.Logger
import com.bael.kirin.lib.threading.contract.Threading
import com.bael.kirin.lib.threading.util.Util.MainThread
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by ErickSumargo on 01/06/20.
 */

abstract class BaseActivity<VB : ViewBinding, DF : Any, VM : ViewModel> :
    AppCompatActivity(),
    Threading {
    override val coroutineContext: CoroutineContext get() = MainThread

    @Inject
    protected lateinit var viewBinder: VB

    @Inject
    protected lateinit var dispatcherFactory: DF

    @Inject
    internal lateinit var viewModelDeferred: @JvmSuppressWildcards Lazy<VM>

    @Inject
    protected lateinit var logger: Logger

    protected val viewModel: VM by lazy { viewModelDeferred.value }

    open val hideDefaultLayout: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!hideDefaultLayout) {
            setContentView(viewBinder.root)
        }
    }

    override fun execute(
        thread: CoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ) {
        try {
            lifecycleScope.launchWhenCreated(block)
        } catch (cause: Exception) {
            logger.log(cause)
        }
    }
}
