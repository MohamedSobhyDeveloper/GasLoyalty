package com.quekitapp.gasloyalty.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.quekitapp.gasloyalty.R
import com.quekitapp.gasloyalty.databinding.LayoutUpdatedDialogBinding
import com.quekitapp.gasloyalty.models.ScanModel
import com.quekitapp.gasloyalty.models.UpdatePlateBody
import com.quekitapp.gasloyalty.utlitites.HelpMe
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.activity_update_plate.*

class BottomSheetUpdatedDialog(
    val eventInt: String,
    val sacnplatenumber: ScanModel,
    val itemSelectedAction: (data: UpdatePlateBody) -> Unit
) : BottomSheetDialogFragment() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        LayoutUpdatedDialogBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //your code here
        binding.apply {

            if (!sacnplatenumber.plate_no.equals("-1")) {
                plate_tv.text = sacnplatenumber.plate_no
                updateplatebtn.visibility = View.VISIBLE
                layoutPlate.visibility=View.VISIBLE

            } else {
                plate_tv.visibility = View.VISIBLE
                plate_tv.text = requireActivity().getString(R.string.cnt_verify_plate_num)

            }
            updateplatebtn.setOnClickListener {

                if (tvNum1.text.toString()
                        .isNotEmpty() && tvNum2.text.toString()
                        .isNotEmpty() && tvNum3.text.toString()
                        .isNotEmpty() &&
                    tvCh1.text.toString().isNotEmpty() && tvCh2.text.toString()
                        .isNotEmpty() && tvCh3.text.toString().isNotEmpty()
                ) {
                    val updatePlateBody = UpdatePlateBody(
                        eventInt,
                        tvCh1.text.toString(),
                        tvCh2.text.toString(),
                        tvCh3.text.toString(),
                        tvNum1.text.toString(),
                        tvNum2.text.toString(),
                        tvNum3.text.toString(),
                        tvNum4.text.toString()
                    )
                    itemSelectedAction.invoke(updatePlateBody)
                } else {
                    TastyToast.makeText(
                        requireActivity(),
                        requireActivity().getString(R.string.enter_valid_plate),
                        TastyToast.LENGTH_SHORT,
                        TastyToast.ERROR
                    )

                }

            }

        }
    }


    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }

}