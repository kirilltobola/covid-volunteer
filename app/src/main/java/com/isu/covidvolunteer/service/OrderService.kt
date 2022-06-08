package com.isu.covidvolunteer.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.isu.covidvolunteer.MainActivity
import com.isu.covidvolunteer.R
import com.isu.covidvolunteer.api.GenericOrderResponse
import com.isu.covidvolunteer.models.order.OrderDto
import com.isu.covidvolunteer.models.order.Status
import com.isu.covidvolunteer.repository.OrderRepository
import com.isu.covidvolunteer.repository.UserRepository
import com.isu.covidvolunteer.retrofit.CustomResponse
import com.isu.covidvolunteer.util.UserDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class OrderService : Service() {
    private val handler = Handler(Looper.getMainLooper())

    var notificationId = 0
    private var userRepository = UserRepository()
    private var orderRepository = OrderRepository()
    private var orderIdAndPerformer: MutableMap<OrderDto, Boolean> = HashMap()

    override fun onCreate() {
        trackOrders()
    }

    private fun trackOrders() {
        GlobalScope.launch {
            val orders = userRepository.getUserOrders(
                "Bearer ${UserDetails.token?.token!!}",
                UserDetails.id!!
            )
            when (orders) {
                is CustomResponse.Success -> {
                    for (order in orders.body) {
                        if (!orderIdAndPerformer.containsKey(order)) {
                            orderIdAndPerformer[order] = order.hasPerformer()
                        }
                    }
                }
                is CustomResponse.NetworkError -> {
                    Log.d(LOG_TAG , orders.error.message.toString())
                    stopSelf()
                }
                is CustomResponse.ApiError -> {
                    Log.d(LOG_TAG , orders.body.message)
                    Log.d(LOG_TAG , orders.body.details.toString())
                    stopSelf()
                }
                is CustomResponse.UnexpectedError -> {
                    Log.d(LOG_TAG , orders.error?.message.toString())
                    Log.d(LOG_TAG , "Unexpected error")
                    stopSelf()
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handler.post(checkOrdersRunnable)
        createNotificationChannel()
        return START_STICKY
    }

    private var checkOrdersRunnable = Runnable {
        requestAndCheckPerformer()
        trackOrders()
    }

    private fun requestAndCheckPerformer() {
        if (!UserDetails.token?.token.isNullOrEmpty()) {
            GlobalScope.launch(Dispatchers.Main) {
                val trackedOrders = HashMap(orderIdAndPerformer)
                for ((order, hasPerformer) in trackedOrders) {
                    if (order.status == Status.ACTIVE) {
                        when (val orderHasPerformer = makeRequest(order.id)) {
                            is CustomResponse.Success -> {
                                if (!hasPerformer && orderHasPerformer.body) {
                                    createNotification(order, "Волонтер откликнулся на заявку")
                                    orderIdAndPerformer[order] = true
                                } else if (hasPerformer && !orderHasPerformer.body) {
                                    createNotification(order, "Волонтер отказался от заявки")
                                    orderIdAndPerformer[order] = false
                                }
                            }
                            is CustomResponse.NetworkError -> {
                                Log.d(LOG_TAG, orderHasPerformer.error.message.toString())
                            }
                            is CustomResponse.ApiError -> {
                                Log.d(LOG_TAG, orderHasPerformer.body.message)
                                Log.d(LOG_TAG, orderHasPerformer.body.details.toString())
                            }
                            is CustomResponse.UnexpectedError -> {
                                Log.d(LOG_TAG, orderHasPerformer.error?.message.toString()
                                )
                                Log.d(LOG_TAG, "Unexpected error")
                            }
                        }
                    }
                }
            }
        } else stopSelf()
        handler.postDelayed(checkOrdersRunnable, POST_DELAYED_IN_MILLIS)
    }

    private fun createNotification(order: OrderDto, msg: String) {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(
            applicationContext,
            CHANNEL_ID
        ).setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(order.headline)
            .setContentText(msg)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(notificationId++, notificationBuilder.build())
        }
        orderIdAndPerformer[order] = true
    }

    private suspend fun makeRequest(orderId: Long): GenericOrderResponse<Boolean> {
        return with(Dispatchers.IO) {
            orderRepository.hasPreformer(
                "Bearer ${UserDetails.token?.token}",
                orderId
            )
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(checkOrdersRunnable)
    }

    override fun onBind(p0: Intent?): IBinder? = null

    companion object {
        const val CHANNEL_ID = "ORDER_SERVICE_CHANNEL_ID"
        const val POST_DELAYED_IN_MILLIS = 60000L
        const val LOG_TAG = "ORDER_SERVICE"
    }
}