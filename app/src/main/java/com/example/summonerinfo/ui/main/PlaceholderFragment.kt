package com.example.summonerinfo.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.summonerinfo.databinding.FragmentMainBinding
import com.example.summonerinfo.dto.ChampionDTO
import com.example.summonerinfo.dto.RotationDTO
import com.example.summonerinfo.dto.SummonerDTO
import com.example.summonerinfo.tabData.ChampionsDataHolder
import com.example.summonerinfo.listeners.DataUpdateListener
import com.example.summonerinfo.tabData.RotationDataHolder
import com.example.summonerinfo.tabData.SingleChampionDataHolder
import java.util.Date

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment(private val dataUpdateListener: DataUpdateListener) : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private var _binding: FragmentMainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val root = binding.root

        val textView: TextView = binding.sectionLabel
        pageViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        val sectionNumber = arguments?.getInt(ARG_SECTION_NUMBER) ?: 1
        when (sectionNumber) {
            1 -> {
                dataUpdateListener.onRotationDataUpdated()
                dataUpdateListener.onChampionDataUpdated()
            }
            2 -> {
                //dataUpdateListener.onChampionDataUpdated()
            }
            3 -> {
                dataUpdateListener.onRandomChampUpdated()
            }
        }



        return root
    }

    @SuppressLint("SetTextI18n")
    fun updateSummonerData(summonerDTO: SummonerDTO) {
        val rotationLabel: TextView = binding.sectionLabel
        rotationLabel.text = "puuid: ${summonerDTO.puuid}\nrevisionDate: ${Date(summonerDTO.revisionDate)}\nsummonerLevel: ${summonerDTO.summonerLevel}"
    }

    @SuppressLint("SetTextI18n")
    fun updateRotationData(rotationDTO: RotationDTO) {
        val rotationLabel: TextView = binding.sectionLabel
        val rotationText = rotationDTO.freeChampionIds.joinToString("\n")
        rotationLabel.text = rotationText
    }

    @SuppressLint("SetTextI18n")
    fun updateRotationData() {
        _binding?.let { binding ->
            val rotationLabel: TextView = binding.sectionLabel
            val rotationDTO: RotationDTO? = RotationDataHolder.rotationDTO

            if (rotationDTO != null) {
                val rotationText = rotationDTO.freeChampionIds.joinToString("\n")
                rotationLabel.text = rotationText
                //dataUpdateListener.onRotationDataUpdated()
            } else {
                rotationLabel.text = "Rotation data unavailable"
            }
        }



    }

    @SuppressLint("SetTextI18n")
    fun updateChampions(champions: List<ChampionDTO>) {
        val rotationLabel: TextView = binding.sectionLabel
        val rotationText = champions.joinToString("\n\n") { champion ->
            "Champion: ${champion.championId}\nLevel: ${champion.championLevel}\nPoints: ${champion.championPoints}\nLast Play Time: ${Date(champion.lastPlayTime)}\nChest Granted: ${champion.chestGranted}"
        }
        rotationLabel.text = rotationText
    }

    fun updateChampions() {
        _binding?.let { binding ->
            val rotationLabel: TextView = binding.sectionLabel
            val champions: List<ChampionDTO>? = ChampionsDataHolder.champions

            if (champions != null) {
                val rotationText = champions.joinToString("\n\n") { champion ->
                    "Champion: ${champion.championId}\nLevel: ${champion.championLevel}\nPoints: ${champion.championPoints}\nLast Play Time: ${Date(champion.lastPlayTime)}\nChest Granted: ${champion.chestGranted}"
                }
                rotationLabel.text = rotationText
                //dataUpdateListener.onChampionDataUpdated()
            } else {
                rotationLabel.text = ""
            }
        }


    }

    @SuppressLint("SetTextI18n")
    fun updateRandomChamp(championDTO: ChampionDTO) {
        val rotationLabel: TextView = binding.sectionLabel
        val rotationText = "Champion: ${championDTO.championId}\nLevel: ${championDTO.championLevel}\nPoints: ${championDTO.championPoints}\nLast Play Time: ${Date(championDTO.lastPlayTime)}\nChest Granted: ${championDTO.chestGranted}"
        rotationLabel.text = rotationText
    }

    fun updateRandomChamp() {
        _binding?.let { binding ->
            val rotationLabel: TextView = binding.sectionLabel
            val championDTO: ChampionDTO? = SingleChampionDataHolder.champion

            if (championDTO != null) {
                val rotationText = "Champion: ${championDTO.championId}\nLevel: ${championDTO.championLevel}\nPoints: ${championDTO.championPoints}\nLast Play Time: ${Date(championDTO.lastPlayTime)}\nChest Granted: ${championDTO.chestGranted}"
                rotationLabel.text = rotationText
                //dataUpdateListener.onRandomChampUpdated()
            } else {
                rotationLabel.text = ""
            }
        }



    }


    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int, dataUpdateListener: DataUpdateListener): PlaceholderFragment {
            return PlaceholderFragment(dataUpdateListener).apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}