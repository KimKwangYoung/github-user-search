package kky.flab.last_mission.repository.model

import kky.flab.last_mission.network.api.GithubSearchApi
import kky.flab.last_mission.repository.GithubUserSearchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class GithubUserSearchRepositoryImpl @Inject constructor(
    private val githubSearchApi: GithubSearchApi,
) : GithubUserSearchRepository {

    private val removedStateFlow: MutableStateFlow<Set<Long>> = MutableStateFlow(setOf())

    private val memoStateFlow: MutableStateFlow<Map<Long, String>> = MutableStateFlow(mapOf())

    override fun flowSearchUser(keyword: String): Flow<List<GithubUser>> = flow {
        val list = if (keyword.isEmpty()) {
            emptyList()
        } else {
            val response = githubSearchApi.search(keyword)
            response.items
        }

        emit(list)
    }.flatMapLatest { items ->
        removedStateFlow.map { removed ->
            items.filter { item -> !removed.contains(item.id) }
        }
    }.flatMapLatest { items ->
        memoStateFlow.map { memoMap ->
            items.map { item ->
                val memo = memoMap[item.id]
                item.toDomain(memo ?: "")
            }
        }
    }

    override fun remove(id: Long) {
        removedStateFlow.update { it.toMutableSet().apply { add(id) } }
    }

    override fun memo(id: Long, memo: String) {
        memoStateFlow.update {
            it.toMutableMap().apply { put(id, memo) }
        }
    }
}