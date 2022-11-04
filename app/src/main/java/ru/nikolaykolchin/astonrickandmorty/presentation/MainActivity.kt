package ru.nikolaykolchin.astonrickandmorty.presentation

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.nikolaykolchin.astonrickandmorty.R
import ru.nikolaykolchin.astonrickandmorty.data.Singletons
import ru.nikolaykolchin.astonrickandmorty.data.Singletons.instanceCharacterFragment
import ru.nikolaykolchin.astonrickandmorty.data.Singletons.instanceEpisodeFragment
import ru.nikolaykolchin.astonrickandmorty.data.Singletons.instanceLocationFragment
import ru.nikolaykolchin.astonrickandmorty.data.characters.database.CharacterDatabase
import ru.nikolaykolchin.astonrickandmorty.databinding.ActivityMainBinding
import ru.nikolaykolchin.astonrickandmorty.data.episodes.database.EpisodeDatabase
import ru.nikolaykolchin.astonrickandmorty.data.locations.database.LocationDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDatabaseSingletons()

        if (isInContainer(null)) showFragment(instanceCharacterFragment, false)

        binding.buttonCharacters.setOnClickListener {
            instanceCharacterFragment.let { fragment ->
                if (!isInContainer(fragment)) showFragment(fragment)
            }
        }
        binding.buttonLocations.setOnClickListener {
            instanceLocationFragment.let { fragment ->
                if (!isInContainer(fragment)) showFragment(fragment)
            }
        }
        binding.buttonEpisodes.setOnClickListener {
            instanceEpisodeFragment.let { fragment ->
                if (!isInContainer(fragment)) showFragment(fragment)
            }
        }
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentCallbacks, false)
    }

    private fun setupDatabaseSingletons() {
        lifecycleScope.launch(Dispatchers.IO) {
            Singletons.characterDao =
                CharacterDatabase.getInstance(applicationContext).getCharacterDao()
            Singletons.episodeDao =
                EpisodeDatabase.getInstance(applicationContext).getEpisodeDao()
            Singletons.locationDao =
                LocationDatabase.getInstance(applicationContext).getLocationDao()
        }
    }

    private fun isInContainer(fragment: Fragment?) =
        binding.fragmentContainer.getFragment<Fragment?>() == fragment

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        if (addToBackStack) transaction.addToBackStack(null)
        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
            .commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentCallbacks)
    }

    private val fragmentCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            } else {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
            }
        }
    }
}