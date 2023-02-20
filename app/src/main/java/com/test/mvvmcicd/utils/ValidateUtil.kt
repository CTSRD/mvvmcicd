package com.test.mvvmcicd.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

class ValidateUtil {
    private fun ValidateUtil() {
    }

    private fun isMatches(text: String, format: String): Boolean {
        val pattern: Pattern = Pattern.compile(format)
        val m: Matcher = pattern.matcher(text)
        return m.matches()
    }

    fun isAccount(str: String): Boolean {
        val format = "[a-zA-Z0-9]{0,20}"
        return isMatches(str, format)
    }

    fun isMoney(money: String): Boolean {
        val regex = "(^[1-9][0-9]{0,7}(\\.[0-9]{0,2})?)|(^0(\\.[0-9]{0,2})?)"
        return isMatches(money, regex)
    }


}