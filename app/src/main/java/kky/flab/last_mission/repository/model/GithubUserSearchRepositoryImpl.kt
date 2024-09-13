package kky.flab.last_mission.repository.model

import android.util.Log
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

    private var page = 1

    private val perPage = 40

    private var cachedList: List<GithubUser> = emptyList()

    private var cachedKeyword: String = ""

    private val removedStateFlow: MutableStateFlow<Set<Long>> = MutableStateFlow(setOf())

    private val memoStateFlow: MutableStateFlow<Map<Long, String>> = MutableStateFlow(mapOf())

    override fun flowSearchUser(keyword: String): Flow<List<GithubUser>> = flow {
        if (keyword == cachedKeyword) {
            page++
        } else {
            page = 1
            cachedList = listOf()
            cachedKeyword = keyword
        }

        val list = if (keyword.isEmpty()) {
            emptyList()
        } else {
            val response = githubSearchApi.search(keyword, page, perPage)
            response.items.sortedBy { it.name }
        }

        emit(list)
    }.flatMapLatest { items ->
        removedStateFlow.map { removed ->
            items.filter { item -> removed.contains(item.id).not() }
        }
    }.flatMapLatest { items ->
        memoStateFlow.map { memoMap ->
            items.map { item ->
                val memo = memoMap[item.id]
                item.toDomain(memo ?: "")
            }
        }
    }.map { users ->
        cachedList = cachedList.toMutableList().apply {
            addAll(users)
        }
        cachedList
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