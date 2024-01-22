package com.craft.apps.countdowns.core.data

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


class CountdownRepositoryTest {

    private lateinit var repository: GenericCountdownRepository

    @Before
    fun createRepository() {
        repository = MockCountdownRepository()
    }

    @Test
    fun `ensure item is present`() = runTest {
        val items = repository.data.first()
//        assert(repository.observeAllModels.first().none { item -> item.isBookmarked })
//        val firstItemId = items.first().id
//        repository.bookmark(firstItemId, true)
//        val firstItem = repository.observeModelById(firstItemId).first()
//        assert(firstItem.isBookmarked)
    }
}
