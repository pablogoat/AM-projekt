package com.example.summonerinfo

import android.content.Context
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.summonerinfo.databinding.ActivityMainBinding
import com.example.summonerinfo.service.RiotApiFetcher
import com.example.summonerinfo.tabData.ChampionsDataHolder
import com.example.summonerinfo.listeners.DataUpdateListener
import com.example.summonerinfo.listeners.ShakeDetector
import com.example.summonerinfo.models.ChampionDatabaseHelper
import com.example.summonerinfo.tabData.RotationDataHolder
import com.example.summonerinfo.tabData.SingleChampionDataHolder
import com.example.summonerinfo.ui.main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), DataUpdateListener, ShakeDetector.OnShakeListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var riotApiFetcher: RiotApiFetcher

    private lateinit var shakeDetector: ShakeDetector
    private lateinit var dbHelper: ChampionDatabaseHelper

    private lateinit var rotationFragment: RotationFragment
    private lateinit var championFragment: ChampionsFragment
    private lateinit var randomChampionFragment: RandomChampionFragment

    override fun onRotationDataUpdated() {
        //val rotationFragment = supportFragmentManager.fragments[0] as PlaceholderFragment
        rotationFragment.updateRotationData()
    }

    override fun onChampionDataUpdated() {
        //val championFragment = supportFragmentManager.fragments[1] as PlaceholderFragment
        championFragment.updateChampions()
    }

    override fun onRandomChampUpdated() {
        //val championFragment = supportFragmentManager.fragments[2] as PlaceholderFragment
        randomChampionFragment.updateRandomChamp()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        shakeDetector = ShakeDetector(this)
        shakeDetector.setOnShakeListener(this)

        dbHelper = ChampionDatabaseHelper(this)

        rotationFragment = RotationFragment(this, this)
        championFragment = ChampionsFragment(this, this, dbHelper)
        randomChampionFragment = RandomChampionFragment(this, this, dbHelper)

        /*val rotationFragment = RotationFragment.newInstance(1, this)
        val championFragment = ChampionsFragment.newInstance(2, this)
        val randomChampionFragment = RandomChampionFragment.newInstance(3, this)*/

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        riotApiFetcher = RiotApiFetcher(this)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.addFragment(rotationFragment)
        sectionsPagerAdapter.addFragment(championFragment)
        sectionsPagerAdapter.addFragment(randomChampionFragment)

        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = binding.fab

        val editText: EditText = binding.editText
        val button: Button = binding.button
        button.setOnClickListener {
            val name = editText.text.toString()
            val summonerPuuid: String? = fetchSummonerData(name)
        }

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        fetchRotationData()
    }

    private fun fetchRotationData() {
        GlobalScope.launch(Dispatchers.Main) {
            val rotationDTO = riotApiFetcher.fetchRotationData()

            if(rotationDTO != null) {
                RotationDataHolder.rotationDTO = rotationDTO
                //onRotationDataUpdated()

                //val rotationFragment = supportFragmentManager.fragments[0] as PlaceholderFragment
                rotationFragment.updateRotationData(rotationDTO)
            } else {
                Toast.makeText(this@MainActivity, "Wystąpił błąd podczas pobierania danych", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchChampionData(championId: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            val sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
            val summonerPuuid = sharedPreferences.getString("puuid", null)

            if(summonerPuuid != null) {
                val championDTO = riotApiFetcher.fetchChampion(championId, summonerPuuid)

                if(championDTO != null) {
                    SingleChampionDataHolder.champion = championDTO
                    //onRandomChampUpdated()
                    println(championDTO)

                    //val rotationFragment = supportFragmentManager.fragments[1] as PlaceholderFragment
                    randomChampionFragment.updateRandomChamp(championDTO)
                } else {
                    Toast.makeText(this@MainActivity, "Wystąpił błąd podczas pobierania danych", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun fetchSummonerData(name: String): String? {
        var summonerPuuid: String? = null
        GlobalScope.launch(Dispatchers.Main) {
            val summonerDTO = riotApiFetcher.fetchSummonerData(name)

            if(summonerDTO != null) {
                summonerPuuid = summonerDTO.puuid
                val sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("puuid", summonerPuuid)
                editor.apply()
            } else {
                Toast.makeText(this@MainActivity, "Wystąpił błąd podczas pobierania danych", Toast.LENGTH_SHORT).show()
            }

            /*if(summonerDTO != null) {
                val rotationFragment = supportFragmentManager.fragments[1] as PlaceholderFragment
                rotationFragment.updateSummonerData(summonerDTO)
            } else {
                Toast.makeText(this@MainActivity, "Wystąpił błąd podczas pobierania danych", Toast.LENGTH_SHORT).show()
            }*/

            val champions = summonerDTO?.let { riotApiFetcher.fetchChampions(it.puuid) }

            if (champions != null) {
                ChampionsDataHolder.champions = champions
                //onChampionDataUpdated()

                //val rotationFragment = supportFragmentManager.fragments[1] as PlaceholderFragment
                championFragment.updateChampions(champions)
            } else {
                Toast.makeText(this@MainActivity, "Wystąpił błąd podczas pobierania danych", Toast.LENGTH_SHORT).show()
            }
        }
        return summonerPuuid
    }

    override fun onShake(championId: Int) {
        fetchChampionData(championId)
    }

    override fun onResume() {
        super.onResume()
        shakeDetector.startListening()
    }

    override fun onPause() {
        super.onPause()
        shakeDetector.stopListening()
    }
}