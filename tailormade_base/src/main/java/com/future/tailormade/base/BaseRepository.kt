package com.future.tailormade.base

import com.future.tailormade.util.AppLogger

abstract class BaseRepository {

    protected var appLogger = AppLogger.create(this.getLogName())

    protected abstract fun getLogName(): String
}