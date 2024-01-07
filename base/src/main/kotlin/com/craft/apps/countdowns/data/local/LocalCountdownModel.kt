package com.craft.apps.countdowns.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.craft.apps.countdowns.core.data.repository.Countdown
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
    val timestamp: Long,
)

/**
 * Utility function to convert a database-level countdown model to a  one.
 */
fun LocalCountdownModel.toRepositoryModel() = Countdown(
    id = this.id,
    label = this.label,
    timestamp = Instant.fromEpochMilliseconds(this.timestamp),
)

/**
 * Utility function to convert a repository-level countdown model to a database-level one.
 */
fun Countdown.toLocalModel() = LocalCountdownModel(
    id = this.id,
    label = this.label,
    timestamp = this.timestamp.toEpochMilliseconds(),
)