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
import com.isu.covidvolunteer.api.GenericChatResponse
import com.isu.covidvolunteer.models.chat.ChatDto
import com.isu.covidvolunteer.repository.ChatRepository
import com.isu.covidvolunteer.retrofit.CustomResponse
import com.isu.covidvolunteer.util.UserDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ChatService : Service() {
    var notificationId = 0
    private val handler = Handler(Looper.getMainLooper())
    private var chatRepository: ChatRepository = ChatRepository()
    private var chatIdAndMessagesCount: MutableMap<ChatDto, Long> = HashMap()

    override fun onCreate() {
        trackChats()
    }

    private fun trackChats() {
        GlobalScope.launch(Dispatchers.IO) {
            when (val chats = chatRepository.getAllChats("Bearer ${UserDetails.token?.token!!}")) {
                is CustomResponse.Success -> {
                    for (chat in chats.body) {
                        if (!chatIdAndMessagesCount.containsKey(chat)) {
                            val countMessages =
                                chatRepository.countMessages("Bearer ${UserDetails.token?.token!!}", chat.id)
                            when (countMessages) {
                                is CustomResponse.Success -> {
                                    chatIdAndMessagesCount[chat] = countMessages.body
                                } else -> {
                                    Log.d(LOG_TAG, "Count messages failed")
                                }
                            }
                        }
                    }
                }
                is CustomResponse.ApiError -> {
                    Log.d(LOG_TAG, chats.body.message)
                    Log.d(LOG_TAG, chats.body.details.toString())
                }
                is CustomResponse.NetworkError -> {
                    Log.d(LOG_TAG, chats.error.message.toString())
                }
                is CustomResponse.UnexpectedError -> {
                    Log.d(LOG_TAG, chats.error?.message.toString())
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handler.post(checkMessagesRunnable)
        createNotificationChannel()
        return START_STICKY
    }

    private var checkMessagesRunnable = Runnable {
        requestAndCheckMessagesCount()
        trackChats()
    }

    private fun requestAndCheckMessagesCount() {
        if (!UserDetails.token?.token.isNullOrEmpty()) {
            GlobalScope.launch(Dispatchers.Main) {
                val trackedChats = HashMap(chatIdAndMessagesCount)
                for ((chat, messages) in trackedChats) {
                    when (val currentMessagesCount = makeRequest(chat.id)) {
                        is CustomResponse.Success -> {
                            if (currentMessagesCount.body > messages) {
                                createNotification(chat, "У вас новое сообщение")
                                chatIdAndMessagesCount[chat] = currentMessagesCount.body
                            }
                        }
                        is CustomResponse.ApiError -> {
                            Log.d(LOG_TAG, currentMessagesCount.body.message)
                            Log.d(LOG_TAG, currentMessagesCount.body.details.toString())
                        }
                        is CustomResponse.NetworkError -> {
                            Log.d(LOG_TAG, currentMessagesCount.error.message.toString())
                        }
                        is CustomResponse.UnexpectedError -> {
                            Log.d(LOG_TAG, currentMessagesCount.error?.message.toString())
                        }
                    }
                }
            }
        } else stopSelf()
        handler.postDelayed(checkMessagesRunnable, POST_DELAYED_IN_MILLIS)
    }

    private fun createNotification(chat: ChatDto, msg: String) {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(chat.user.toString())
            .setContentText(msg)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(notificationId, notificationBuilder.build())
            notificationId++
        }
    }

    private suspend fun makeRequest(chatId: Long): GenericChatResponse<Long> {
        return with(Dispatchers.IO) {
            chatRepository.countMessages(
                "Bearer ${UserDetails.token?.token!!}",
                chatId
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
        handler.removeCallbacks(checkMessagesRunnable)
    }

    override fun onBind(intent: Intent): IBinder? = null

    companion object {
        const val CHANNEL_ID = "CHAT_SERVICE_CHANNEL_ID"
        const val POST_DELAYED_IN_MILLIS = 60000L
        const val LOG_TAG = "CHAT_SERVICE"
    }
}