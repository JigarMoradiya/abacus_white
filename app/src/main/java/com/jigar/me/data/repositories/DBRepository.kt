package com.jigar.me.data.repositories

import com.jigar.me.data.api.connections.SafeApiCall
import com.jigar.me.data.local.db.abacus_all_data.AbacusAllDataDB
import com.jigar.me.data.model.dbtable.abacus_all_data.Level
import com.jigar.me.data.model.dbtable.abacus_all_data.SetProgress
import javax.inject.Inject

class DBRepository @Inject constructor(
    private val abacusAllDataDB: AbacusAllDataDB,
) : SafeApiCall {

    // abacus all data
    suspend fun insertLevel(data : List<Level>) = abacusAllDataDB.insertLevel(data)
    suspend fun insertSetProgress(data : List<SetProgress>) = abacusAllDataDB.insertSetProgress(data)
}