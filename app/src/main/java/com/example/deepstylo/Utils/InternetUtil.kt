package com.example.deepstylo.Utils

import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress

object InternetUtil {

    fun isOnline(): Boolean {
        return try {
            val timeoutMs = 1500
            val sock = Socket()
            val sockaddr: SocketAddress = InetSocketAddress("8.8.8.8", 53)
            sock.connect(sockaddr, timeoutMs)
            sock.close()
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

}