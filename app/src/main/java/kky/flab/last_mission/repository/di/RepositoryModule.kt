package kky.flab.last_mission.repository.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kky.flab.last_mission.repository.GithubUserSearchRepository
import kky.flab.last_mission.repository.model.GithubUserSearchRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindsGithubUserSearchRepository(
        githubUserSearchRepositoryImpl: GithubUserSearchRepositoryImpl
    ): GithubUserSearchRepository
}