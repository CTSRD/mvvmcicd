package com.test.mvvmcicd.utils

import android.text.TextUtils
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.math.BigInteger
import java.util.*

/**
 * Created by Shunte.Lin on 2022/09/16.
 */
object HexUtil {
    private val TAG: String = HexUtil::class.java.name
    private val hexArray = "0123456789ABCDEF".toCharArray()
    fun bytesToHex(bytes: ByteArray): String {
        val hexChars = CharArray(2)
        val sb = StringBuffer()
        for (j in bytes.indices) {
            val v = (bytes[j].toInt() and 0xFF)
            hexChars[0] = hexArray[v ushr 4]
            hexChars[1] = hexArray[v and 0x0F]
            if (sb.isNotEmpty()) {
                sb.append(" ")
            }
            sb.append(String(hexChars))
        }
        return sb.toString()
    }

    fun bytesToHex(bytes: ByteArray, append: String?): String {
        val hexChars = CharArray(2)
        val sb = StringBuffer()
        for (j in bytes.indices) {
            val v: Int = (bytes[j].toInt() and 0xFF)
            hexChars[0] = hexArray[v ushr 4]
            hexChars[1] = hexArray[v and 0x0F]
            if (sb.isNotEmpty()) {
                if (!TextUtils.isEmpty(append)) {
                    sb.append(append)
                }
            }
            sb.append(String(hexChars))
        }
        return sb.toString()
    }

    fun bytesToInt(bytes: ByteArray?): Int {
        if (null == bytes) {
            return 0
        }
        var value = 0
        val len = bytes.size
        for (i in 0 until len) {
            val shift = (len - 1 - i) * 8
            value += (bytes[i].toInt() and 0x000000FF) shl shift
        }
        return value
    }

    fun convertMacFormat(mac: String): String {
        if (mac.contains(":")) {
            return mac
        }
        val sbMac = StringBuffer(mac)
        var position = sbMac.length
        while (0 < position) {
            position -= 2
            if (0 >= position) {
                break
            }
            sbMac.insert(position, ":")
        }
        return sbMac.toString()
    }

    fun isSameMac(mac1: String, mac2: String): Boolean {
        return TextUtils.equals(
                mac1.replace(":", "").lowercase(Locale.ROOT),
                mac2.replace(":", "").lowercase(Locale.ROOT))
    }

    fun byteToString(bytes: ByteArray): String {
        return try {
            JSONObject(String(bytes)).toString()
        } catch (e: JSONException) {
            bytesToHex(bytes)
        }
    }

    fun parseHexToDec(hex: String): Int {
        val value = parseHexToIntDec(hex)
        if (value > Int.MAX_VALUE) {
            Log.e(TAG, "[Fail] Convert Hex to Dec fail: Convert $hex out of \"int\" range!")
            return 0
        }
        return value
    }

    fun parseHexToIntDec(hex: String): Int {
        if (TextUtils.isEmpty(hex) || !hex.matches(Regex("^[0-9a-fA-F]+$"))) {
            return 0
        }
        val value = BigInteger(hex, 16)
        return value.toInt()
    }

    fun parseHexToBitString(hex: String): String {
        if (hex.length %2 != 0) {
            Log.d(TAG, "Input data is error type")
            return "Wrong data input."
        }
        var result = ""
        for (element in hex.indices step 2){
            var data = parseHexToDec("${hex[element]}${hex[element+1]}").toString(2)
            data = when(data.length) {
                1 -> "0000000$data"
                2 -> "000000$data"
                3 -> "00000$data"
                4 -> "0000$data"
                5 -> "000$data"
                6 -> "00$data"
                7 -> "0$data"
                else -> data
            }
            result +=data
        }
        return result
    }
}