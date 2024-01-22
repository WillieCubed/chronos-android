package com.craft.apps.countdowns.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.craft.apps.countdowns.core.model.Countdown
import kotlinx.datetime.Instant

/**
 * A database-layer data model for a Countdown.
 */
@Entity(tableName = "countdowns")
data class LocalCountdownModel(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val label: String,
    /**
     * The instant this countdown expires, measured in milliseconds since the UNIX epoch.
     */
    val expiration: Long,
    val creationTimestamp: Long,
    val notes: String,
    val featured: Boolean,
)

/**
 * Utility function to convert a database-level countdown model to a  one.
 */
fun LocalCountdownModel.toDataModel() = Countdown(
    id = this.id,
    label = this.label,
    expiration = Instant.fromEpochMilliseconds(this.expiration),
    creationTimestamp = Instant.fromEpochMilliseconds(this.creationTimestamp),
    notes = this.notes,
    featured = this.featured,
)

/**
 * Utility function to convert a repository-level countdown model to a database-level one.
 */
fun Countdown.toDatabaseModel() = LocalCountdownModel(
    id = this.id,
    label = this.label,
    expiration = this.expiration.toEpochMilliseconds(),
    creationTimestamp = this.creationTimestamp.toEpochMilliseconds(),
    notes = this.notes,
    featured = this.featured,
)