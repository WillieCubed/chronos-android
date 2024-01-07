package com.craft.apps.countdowns.data.local.widget

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.craft.apps.countdowns.data.local.LocalCountdownModel

@Entity(
    tableName = "widget_data",
)
data class LocalWidgetDataModel(
    @PrimaryKey val widgetId: String,
    val countdownId: Int,
)

/**
 * A database-layer data model for widget data.
 */
data class WidgetDataAndCountdownModel(
    @Embedded val localWidgetDataModel: LocalWidgetDataModel,

    @Relation(
        parentColumn = "countdownId",
        entityColumn = "id",
    ) val countdown: LocalCountdownModel,
)