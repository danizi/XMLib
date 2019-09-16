package com.xm.lib.test.ui.act

import android.annotation.SuppressLint
import android.app.IntentService
import android.content.Intent
import android.os.*
import android.support.v7.app.AppCompatActivity
import com.xm.lib.common.log.BKLog
import com.xm.lib.downloader.utils.CommonUtil
import com.xm.lib.test.R
import java.net.URL
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.FutureTask

class PrimaryThread : AppCompatActivity() {
    companion object {
        const val TAG = "PrimaryThread"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_primary_thread)
    }

    /**
     * 《底层实现》
     * ThreadPoolExecutor + handler + Callable、Future和FutureTask 实现，轻量级异步框架。
     *
     * 《源码解析》
     *  AsyncTask 初始化
     *  1 初始化handler实例化InternalHandler，用于通知更新和下载结果。
     *  2 初始化WorkerRunnable它继承Callable接口，过程中调用了doInBackground()进行耗时操作，并且结果发送到UI线程上，耗时进度通过在doInBackground()调用publishProgress()进行更新。
     *  3 初始化FutureTask 获取WorkerRunnable返回的结果，并且结果发送到UI线程上。
     *
     *  SerialExecutor
     *  串行执行任务，底层使用了ThreadPoolExecutor线程池corePoolSize 线程池数量最少二个最多四个，maximumPoolSize (corePoolSize * 2+1)。
     *
     *  InternalHandler
     *  下载结果和下载进度处理Handler
     *
     *  WorkerRunnable
     *  继承Callable接口，调用call接口返回下载结果，并将结果通过InternalHandler 分发到UI线程上的。
     *
     *  FutureTask
     *  获取Callable接口返回的结果
     *
     *
     *
     * 《函数说明》
     * onPreExecute()
     * 任务真正执行前，通知用户任务开始了。
     *
     * doInBackground()
     * 耗时操作，进度可以通过调用publishProgress()，触发onProgressUpdate进行更新。
     *
     * onPostExecute()
     * 耗时操作完成的结果回调
     *
     * publishProgress()
     * 更新耗时进度
     *
     * 《AsynaTask相关提问》
     *  生命周期
     *  调用了cancel才算真正的销毁，并不是伴随Activity的销毁而销毁。另外，即使我们正确地调用了cancle() 也未必能真正地取消任务。因为如果在doInBackgroud里有一个不可中断的操作，比如BitmapFactory.decodeStream()，那么这个操作会继续下去。
     *
     *  内存泄露
     *  WeakRefrences来弱引用外部Context。
     *
     *  结果丢失
     *  窗口因为某种情况重新创建了，AsyncTask持有销毁Activity的引用，这时调用onPostExecute()再去更新界面将不再生效。
     *
     * 《参考》
     * https://juejin.im/post/5bd3fa0ff265da0a8a6af0bc
     * https://www.cnblogs.com/dolphin0520/p/3949310.html
     * https://www.jianshu.com/p/8f4a6c9414b0
     */
    private fun asyncTaskTest() {
        val task = @SuppressLint("StaticFieldLeak")
        object : AsyncTask<URL, Any, Boolean>() {

            /**
             * 运行在工作线程，下载任务
             */
            override fun doInBackground(vararg params: URL?): Boolean {
                publishProgress(12, 34, "DS", "dfw")
                BKLog.d(TAG, "isMainThread: ${CommonUtil.isMainThread()} doInBackground() ")
                return true
            }

            /**
             * 运行在UI线程，下载之前准备。
             */
            override fun onPreExecute() {
                super.onPreExecute()
                BKLog.d(TAG, "isMainThread: ${CommonUtil.isMainThread()} onPreExecute()")
                Looper.getMainLooper()
            }

            /**
             * 运行在UI线程，更新进度
             */
            override fun onProgressUpdate(vararg values: Any?) {
                super.onProgressUpdate(*values)
                BKLog.d(TAG, "isMainThread: ${CommonUtil.isMainThread()} onProgressUpdate()")
            }

            /**
             * 任务下载结果
             */
            override fun onPostExecute(result: Boolean?) {
                super.onPostExecute(result)
                BKLog.d(TAG, "isMainThread: ${CommonUtil.isMainThread()} onPostExecute()")
            }
        }
        //调用多次会报错的
        task.execute(URL("下载任务"))

        HandlerThread("")
    }

    /**
     *
     * 《Handler、Thread、HandlerThread有什么区别？》
     * Handler        负责发送和处理消息切换线程之间的消息。
     * Thread         执行运算的最小单位调度的基本单位
     * HandlerThread  结合上面两个类特性
     * 直接使用Handler + Thread方式处理就行了，为什么还要使用HandlerThread，我们知道Handler在子线程中发送消息，必须实例化一个Looper，系统并不建议手动实例化Looper所以封装了HandlerThread并且Looper在子线程上的。
     *
     * 《怎么切换到UI线程上来？》
     * Handler.post()
     * Activity.runOnUiThread()
     * Handler在主线程上，Handler.sendxxxx()
     *
     * 《内存泄露问题》
     * 非静态内部类、匿名内部类，都默认持有外部应用。如果Handler的生命周期 > 外部类的生命周期时就无法GC从而造成内存泄露
     *
     * 《内存泄露解决办法》
     * 静态内部类 + 弱引用的方式
     * 在onDestroy中调用HandLer.removeCallbacksAndMessages(null)
     */
    private fun handlerThreadTest() {
        val uiHandLer = Handler(Looper.getMainLooper())

        workHandler = HandlerThread("workProcess")
        workHandler?.start()

        val realWorkHandler = object : Handler(workHandler?.looper) {
            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
                uiHandLer.post {
                    //这里一直是在主线程上面的。
                }
            }
        }

        realWorkHandler.sendEmptyMessage(0)
    }

    private var workHandler: HandlerThread? = null
    override fun onDestroy() {
        super.onDestroy()
        workHandler?.quit()
    }

    /**
     * 《IntentService》
     *  与Servoce不同，IntentService进行耗时操作不需要创建工作线程，因为底层实现就用到了HandlerThread，耗时操作在onHandleIntent(...)操作即可。
     *
     *  详细看下源码就行了
     *
     */
    private fun intentServiceTest() {
        val handler = object : Handler() {
            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
            }
        }
        handler.sendMessage(Message())

        //startActivity(Intent(this))
    }

    private fun threadPoolExecutorTest() {

    }

    /**
     * run()和start()方法的区别
     * start为开启线程做一些准备，然后再调用run。如果直接调用run方法其实就是调用一个方法而已根本不会开启一个线程。通过在run方法里面打印当前线程就会知道了。
     */
    private fun threadTest() {
        val thread = ThreadTest()
        thread.start()
    }

    private fun runnableTest() {
        val thread = Thread(RunnableTest())
        thread.start()
    }

    private fun futureTest() {
        val executor = Executors.newCachedThreadPool()
        val result = executor.submit(Task())
        result.get()
    }

    private fun futureTaskTest() {
        val executor = Executors.newCachedThreadPool()
        val futureTask = FutureTask(Task())
        executor.submit(futureTask)
        futureTask.get()
    }

    internal inner class Task : Callable<Int> {
        @Throws(Exception::class)
        override fun call(): Int? {
            println("子线程在进行计算")
            Thread.sleep(3000)
            var sum = 0
            for (i in 0..99)
                sum += i
            return sum
        }
    }

    internal inner class RunnableTest : Runnable {
        override fun run() {

        }
    }

    internal inner class ThreadTest : Thread() {
        override fun run() {
            super.run()
        }
    }

    internal inner class IntentServiceTest : IntentService("") {

        override fun onHandleIntent(intent: Intent?) {
            BKLog.d(TAG, "onHandleIntent()")
        }

        override fun onBind(intent: Intent?): IBinder? {
            BKLog.d(TAG, "")
            return null
        }

        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            BKLog.d(TAG, "onStartCommand(...)")
            return super.onStartCommand(intent, flags, startId)
        }

        override fun onUnbind(intent: Intent?): Boolean {
            BKLog.d(TAG, "onUnbind(...)")
            return super.onUnbind(intent)
        }

        override fun onDestroy() {
            super.onDestroy()
            BKLog.d(TAG, "onDestroy(...)")
        }
    }
}


