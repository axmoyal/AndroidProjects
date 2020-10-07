package edu.stanford.axmoyal.tippy2

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG= "MainActivity"
private const val FirstTip= 15

/* I have made two extensions : splitting the billing for N people and changing the background color.*/
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBarTip.progress= FirstTip
        tvTipPercent.text="$FirstTip%"
        updateTipComment(FirstTip)
        seekBarTip.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                            seekBar: SeekBar?,
                            progress: Int,
                            fromUser: Boolean
                    ) {

                        tvTipPercent.text="$progress%"
                        computeTotalTip()
                        updateTipComment(progress)
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                }
        )
        etBase.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                computeTotalTip()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        edNumPersons.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                computeTotalTip()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun computeTotalTip(){
        if (etBase.text.isEmpty()){
            tvTipAmount.text=""
            tvTotalAmount.text=""
            return
        }
        val baseAm=etBase.text.toString().toDouble()
        val tipPercent=seekBarTip.progress

        val tipAm=tipPercent*baseAm /100.0
        val totalAm=tipAm+baseAm

        tvTipAmount.text="%.2f".format(tipAm)
        tvTotalAmount.text="%.2f".format(totalAm)

        if (edNumPersons.text.isEmpty()){
            tvTipPerson.text=""
            tvTotalPerPerson.text=""
            return
        }
        val numPerson=edNumPersons.text.toString().toDouble()
        val totalAmPerPerson=1.0*totalAm/numPerson
        val tipAmPerPerson=1.0*tipAm/numPerson
        tvTipPerson.text="%.2f".format(tipAmPerPerson)
        tvTotalPerPerson.text="%.2f".format(totalAmPerPerson)
    }

    private fun updateTipComment(TipPercent: Int){
        val TipComment : String
        when (TipPercent){
            in 0..9->TipComment="poor"
            in 10..14->TipComment="Acceptable"
            in 15..19->TipComment="Good"
            in 20..24->TipComment="Great"
            else->TipComment="Great"
        }
        tvTipComment.text=TipComment
        val color=ArgbEvaluator().evaluate(TipPercent.toFloat()/seekBarTip.max,ContextCompat.getColor(this,R.color.colorWorstTip),ContextCompat.getColor(this,R.color.colorBestTip))
        as Int
        tvTipComment.setTextColor(color)
    }
}