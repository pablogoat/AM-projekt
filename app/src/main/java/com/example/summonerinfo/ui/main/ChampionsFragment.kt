package com.example.summonerinfo.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.summonerinfo.databinding.FragmentMainBinding
import com.example.summonerinfo.dto.ChampionDTO
import com.example.summonerinfo.dto.RotationDTO
import com.example.summonerinfo.dto.SummonerDTO
import com.example.summonerinfo.tabData.ChampionsDataHolder
import com.example.summonerinfo.listeners.DataUpdateListener
import com.example.summonerinfo.models.ChampionDatabaseHelper
import com.example.summonerinfo.tabData.RotationDataHolder
import com.example.summonerinfo.tabData.SingleChampionDataHolder
import java.util.Date

/**
 * A placeholder fragment containing a simple view.
 */
class ChampionsFragment(private val dataUpdateListener: DataUpdateListener, private val context: Context, dbHelper: ChampionDatabaseHelper) : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private var _binding: FragmentMainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var rotationLabel: TextView

    private fun getBinding(view: View): FragmentMainBinding {
        return FragmentMainBinding.bind(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val root = binding.root

        rotationLabel = binding.sectionLabel

        /*val textView: TextView = binding.sectionLabel
        pageViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/

        val sectionNumber = arguments?.getInt(ARG_SECTION_NUMBER) ?: 2
        when (sectionNumber) {
            1 -> {
                dataUpdateListener.onRotationDataUpdated()
            }
            2 -> {
                dataUpdateListener.onChampionDataUpdated()
            }
            3 -> {
                dataUpdateListener.onRandomChampUpdated()
            }
        }


        return root
    }

    @SuppressLint("SetTextI18n")
    fun updateChampions(champions: List<ChampionDTO>) {
        val rotationText = champions.joinToString("\n\n") { champion ->
            "Champion: ${champion.championId}\nLevel: ${champion.championLevel}\nPoints: ${champion.championPoints}\nLast Play Time: ${Date(champion.lastPlayTime)}\nChest Granted: ${champion.chestGranted}"
        }
        rotationLabel.text = rotationText
    }

    fun updateChampions() {
        val champions: List<ChampionDTO>? = ChampionsDataHolder.champions

        if (champions != null) {
            val rotationText = champions.joinToString("\n\n") { champion ->
                "Champion: ${champion.championId}\nLevel: ${champion.championLevel}\nPoints: ${champion.championPoints}\nLast Play Time: ${Date(champion.lastPlayTime)}\nChest Granted: ${champion.chestGranted}"
            }
            rotationLabel.text = rotationText
        } else {
                rotationLabel.text = ""
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
        fun newInstance(sectionNumber: Int, dataUpdateListener: DataUpdateListener, context: Context, dbHelper: ChampionDatabaseHelper): ChampionsFragment {
            return ChampionsFragment(dataUpdateListener, context, dbHelper).apply {
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