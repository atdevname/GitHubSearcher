package com.atdev.githubproject.model

import kotlinx.coroutines.flow.Flow

data class ReposResult(var items: Flow<List<RepositoryJsonObject>>) {

}