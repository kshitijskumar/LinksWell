package com.example.linkswell.home.domain

import com.example.linkswell.home.data.models.appmodel.LinkAppModel
import com.example.linkswell.home.data.models.appmodel.LinkGroupCollectionAppModel
import com.example.linkswell.home.data.repository.ILinksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetLinksGroupedInSameTypeListUseCase @Inject constructor(
    private val linksRepository: ILinksRepository
) {

    operator fun invoke(): Flow<List<LinkGroupCollectionAppModel>> {
        return linksRepository.getAllLinks().map { links ->
            val groupAndLinksMap = hashMapOf<String, List<LinkAppModel>>()

            links.forEach { linkAppModel ->
                val existingOrEmptyList = groupAndLinksMap[linkAppModel.groupName]?.toMutableList() ?: mutableListOf()
                existingOrEmptyList.add(linkAppModel)

                groupAndLinksMap[linkAppModel.groupName] = existingOrEmptyList
            }

            val resultList = groupAndLinksMap.entries.map {
                LinkGroupCollectionAppModel(
                    groupName = it.key,
                    links = it.value
                )
            }
            resultList
        }
    }
}