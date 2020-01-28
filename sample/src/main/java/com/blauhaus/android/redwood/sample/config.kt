package com.blauhaus.android.redwood.sample


class Config: IConfig {
    override fun TOTAL_DAYS_ACHIEVEMENT_THRESHOLD() = 14
    override fun GOLD_MEDAL_THRESHOLD_IN_MINUTES() = 25
    override fun SILVER_MEDAL_THRESHOLD_IN_MINUTES() = 15
    override fun BRONZE_MEDAL_THRESHOLD_IN_MINUTES() = 10
}

interface IConfig {
    fun TOTAL_DAYS_ACHIEVEMENT_THRESHOLD():Int
    fun GOLD_MEDAL_THRESHOLD_IN_MINUTES() :Int
    fun SILVER_MEDAL_THRESHOLD_IN_MINUTES():Int
    fun BRONZE_MEDAL_THRESHOLD_IN_MINUTES() :Int
}
