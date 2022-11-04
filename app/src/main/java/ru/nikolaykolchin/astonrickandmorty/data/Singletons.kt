package ru.nikolaykolchin.astonrickandmorty.data

import ru.nikolaykolchin.astonrickandmorty.data.characters.database.CharacterDao
import ru.nikolaykolchin.astonrickandmorty.data.characters.service.CharacterService
import ru.nikolaykolchin.astonrickandmorty.presentation.characters.CharacterFragment
import ru.nikolaykolchin.astonrickandmorty.data.episodes.database.EpisodeDao
import ru.nikolaykolchin.astonrickandmorty.data.episodes.service.EpisodeService
import ru.nikolaykolchin.astonrickandmorty.presentation.episodes.EpisodeFragment
import ru.nikolaykolchin.astonrickandmorty.data.locations.database.LocationDao
import ru.nikolaykolchin.astonrickandmorty.data.locations.service.LocationService
import ru.nikolaykolchin.astonrickandmorty.presentation.locations.LocationFragment
import kotlin.properties.Delegates

object Singletons {
    val instanceCharacterFragment = CharacterFragment.newInstance()
    val instanceLocationFragment = LocationFragment.newInstance()
    val instanceEpisodeFragment = EpisodeFragment.newInstance()

    val characterService: CharacterService =
        RetrofitModule.getInstance().create(CharacterService::class.java)
    val episodeService: EpisodeService =
        RetrofitModule.getInstance().create(EpisodeService::class.java)
    val locationService: LocationService =
        RetrofitModule.getInstance().create(LocationService::class.java)

    var characterDao by Delegates.notNull<CharacterDao>()
    var episodeDao by Delegates.notNull<EpisodeDao>()
    var locationDao by Delegates.notNull<LocationDao>()
}