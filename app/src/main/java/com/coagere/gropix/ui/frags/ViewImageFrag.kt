package com.coagere.gropix.ui.frags

import android.os.Bundle
import android.view.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.coagere.gropix.R
import com.coagere.gropix.utils.GestureImageView
import com.tc.utils.elements.BaseFragment
import com.tc.utils.variables.interfaces.ApiKeys
import com.tc.utils.variables.interfaces.IntentInterface
import kotlinx.android.synthetic.main.frag_view_image.*

/**
 * @author Jatin Sahgal by 14-Sep-2020 11:10
 */
class ViewImageFrag : BaseFragment() {
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var mScaleFactor = 1.0f
    private var isZoomIn = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_view_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scaleGestureDetector = ScaleGestureDetector(requireContext(), ScaleListener())
        initializeView()
        initializeListeners()
    }

    override fun initializeView() {
        super.initializeView()
        if (requireArguments().getInt(IntentInterface.INTENT_FOR_POSITION, 0) == 0) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                id_image.transitionName = getString(R.string.string_icon_transition_image)
            }
        }
        Glide.with(requireContext())
            .load(ApiKeys.URL_DOMAIN + requireArguments().getString(IntentInterface.INTENT_FOR_URL)!!)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(id_image)
    }

    override fun initializeListeners() {
        super.initializeListeners()
        id_image.onClickListener(object : GestureImageView.OnClick {
            override fun onDoubleClick() {
                if (!isZoomIn) {
                    isZoomIn = true
                    mScaleFactor *= 2
                    mScaleFactor = 1.0f.coerceAtLeast(mScaleFactor.coerceAtMost(2.0f))
                    initializeScales()
                } else {
                    onSingleClick()
                }
            }

            override fun onSingleClick() {
                if (isZoomIn) {
                    mScaleFactor /= 2f
                }
                isZoomIn = false
                initializeScales()
            }
        })
    }


    private fun initializeScales() {
        id_image.scaleX = mScaleFactor
        id_image.scaleY = mScaleFactor
    }

    fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event!!.pointerCount > 1) {
            scaleGestureDetector.onTouchEvent(event)
        }
        return true
    }


    inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector?): Boolean {
            mScaleFactor *= scaleGestureDetector.scaleFactor
            mScaleFactor = 1.0f.coerceAtLeast(mScaleFactor.coerceAtMost(10.0f))
            isZoomIn = false
            initializeScales()
            return true
        }
    }

    companion object {
        fun getInstance(image: String, position: Int): ViewImageFrag {
            val frag = ViewImageFrag()
            val bundle = Bundle()
            bundle.putString(IntentInterface.INTENT_FOR_URL, image)
            bundle.putInt(IntentInterface.INTENT_FOR_POSITION, position)
            frag.arguments = bundle
            return frag
        }
    }
}