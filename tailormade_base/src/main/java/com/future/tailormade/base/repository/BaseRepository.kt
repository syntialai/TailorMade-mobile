package com.future.tailormade.base.repository

import com.future.tailormade.util.logger.AppLogger

abstract class BaseRepository {

    protected var appLogger = AppLogger.create(this.getLogName())

    protected abstract fun getLogName(): String
}