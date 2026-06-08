package com.jigar.me.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jigar.me.data.local.data.ExamPaper
import java.util.*

class DataTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun beginnerExamDetailToList(data: String): List<ExamPaper> {
        val listType = object : TypeToken<List<ExamPaper>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun listToBeginnerExamDetail(someObjects: List<ExamPaper>): String {
        return gson.toJson(someObjects)
    }

    @TypeConverter
    fun stringToList(data: String?): List<String>? {
        if (data.isNullOrEmpty()){
            return arrayListOf()
        }
        val listType = object : TypeToken<List<String>?>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun listToString(someObjects: List<String>?): String {
        return gson.toJson(someObjects)
    }
}