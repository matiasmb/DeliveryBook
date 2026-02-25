package com.deliverybook.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [ContactEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class DeliveryBookDatabase : RoomDatabase() {
    abstract fun contactsDao(): ContactsDao
}
