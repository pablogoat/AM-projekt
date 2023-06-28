package com.example.summonerinfo.ui.main

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.summonerinfo.databinding.FragmentMainBinding
import com.example.summonerinfo.dto.ChampionContract
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
class RotationFragment(private val dataUpdateListener: DataUpdateListener, private val context: Context) : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private var _binding: FragmentMainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var rotationLabel: TextView

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

        rotationLabel = binding.sectionLabel

        val sectionNumber = arguments?.getInt(ARG_SECTION_NUMBER) ?: 1
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
    fun updateRotationData(rotationDTO: RotationDTO) {

        val rotationText = rotationDTO.freeChampionIds.joinToString("\n")
        rotationLabel.text = rotationText

    }

    @SuppressLint("SetTextI18n")
    fun updateRotationData() {
        val rotationDTO: RotationDTO? = RotationDataHolder.rotationDTO
         if (rotationDTO != null) {
             val championNames = rotationDTO.freeChampionIds.map { getChampionNameById(it) }
             val rotationText = championNames.joinToString("\n")
             rotationLabel.text = rotationText
         } else {
             rotationLabel.text = "Rotation data unavailable"
         }

    }

    @SuppressLint("Range")
    private fun getChampionNameById(championId: Int): String {
        val databaseHelper = ChampionDatabaseHelper(context)
        val db = databaseHelper.readableDatabase
        val COLUMN_NAME = ChampionContract.ChampionEntry.COLUMN_NAME_CHAMPION_NAME

        val columns = arrayOf(COLUMN_NAME)
        val selection = "${DownloadManager.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(championId.toString())

        val cursor = db.query(ChampionContract.ChampionEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, null)

        var championName = ""

        if (cursor.moveToFirst()) {
            championName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
        }

        cursor.close()
        db.close()

        return championName
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
        fun newInstance(sectionNumber: Int, dataUpdateListener: DataUpdateListener, context: Context): RotationFragment {
            return RotationFragment(dataUpdateListener, context).apply {
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