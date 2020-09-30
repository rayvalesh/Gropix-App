package com.coagere.gropix.ui.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.coagere.gropix.R
import com.coagere.gropix.jetpack.entities.ViewImageModel
import com.coagere.gropix.ui.adapters.ViewImageAdapter
import com.coagere.gropix.ui.frags.ViewImageFrag
import com.coagere.gropix.utils.ElasticDragDismissFrameLayout
import com.tc.utils.elements.BaseActivity
import com.tc.utils.utils.helpers.HelperActionBar
import com.tc.utils.variables.abstracts.OnEventOccurListener
import com.tc.utils.variables.interfaces.IntentInterface
import kotlinx.android.synthetic.main.activity_view_image.*

/**
 * Image Viewer : Handling TouchPoints, Scaling and animated View Exploring
 */
class ViewImageActivity : BaseActivity() {
    private val fragList = arrayListOf<ViewImageFrag>()
    private val modelList: ArrayList<ViewImageModel> = arrayListOf()

    private val handler = Handler()
    private var lastItemSelected: Int = 0
    private val runnable = Runnable {
        id_parent_app_bar.setExpanded(true, true)
    }
    private var chromeFader: ElasticDragDismissFrameLayout.SystemChromeFader? = null
    private var adapter: ViewImageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        super.onCreate(savedInstanceState)
        val images = intent.getStringArrayExtra(IntentInterface.INTENT_FOR_URL)!!
        for (image in images) {
            modelList.add(ViewImageModel(image))
        }
        setContentView(R.layout.activity_view_image)
        setToolbar()
        initializeView()
        initializeTabView()
        initializeRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        id_container?.addListener(chromeFader)
    }

    override fun onPause() {
        super.onPause()
        id_container?.removeListener(chromeFader)
    }

    /**
     * Showing Background after few milliseconds
     * also managing Lollipop for scrolling animation
     */
    override fun setToolbar() {
        setSupportActionBar(findViewById(R.id.id_app_bar))
        HelperActionBar.setSupportActionBar(supportActionBar)
        id_view.visibility = View.VISIBLE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            chromeFader = ElasticDragDismissFrameLayout.SystemChromeFader(this)
            id_container.setOnApplyWindowInsetsListener { v, insets ->
                val listLp = v.layoutParams as MarginLayoutParams
                listLp.topMargin = insets.systemWindowInsetTop
                v.layoutParams = listLp
                insets
            }
        }
        handler.postDelayed(runnable, 300)
        id_view.animate()
            .alpha(1f).duration = 700

    }

    override fun initializeView() {
        super.initializeView()
        id_container?.addListener(object :
            ElasticDragDismissFrameLayout.ElasticDragDismissCallback() {
            override fun onDrag(
                elasticOffset: Float,
                elasticOffsetPixels: Float,
                rawOffset: Float,
                rawOffsetPixels: Float
            ) {
                super.onDrag(elasticOffset, elasticOffsetPixels, rawOffset, rawOffsetPixels)
                id_view.alpha = 1 - elasticOffset
                id_parent_app_bar.alpha = 1 - elasticOffset
            }

        })
    }

    /**
     * Initializing [ViewPager] and on page change updating [adapter] item for view selection
     * @author Jatin Sahgal
     */
    override fun initializeTabView() {
        super.initializeTabView()
        id_view_pager.adapter = ManagerAdapter(supportFragmentManager)
        id_view_pager.offscreenPageLimit = 2
        id_view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                modelList[lastItemSelected].selected = false
                adapter?.notifyAdapterItemChanged(lastItemSelected)
                modelList[position].selected = true
                adapter?.notifyAdapterItemChanged(position)
                lastItemSelected = position
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
        id_view_pager.currentItem = intent.getIntExtra(IntentInterface.INTENT_FOR_POSITION, 0)
    }

    /**
     * Initializing recycler and showing when items are more then one
     * @author Jatin Sahgal
     */
    override fun initializeRecyclerView() {
        super.initializeRecyclerView()
        if (modelList.size > 1) {
            id_recycler_view.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            adapter = ViewImageAdapter(modelList.toTypedArray(), object : OnEventOccurListener() {
                override fun getEventData(any: Any) {
                    super.getEventData(any)
                    id_view_pager.currentItem = any as Int
                }
            })
            id_recycler_view.adapter = adapter
            id_recycler_view.visibility = View.VISIBLE
        }
    }

    /**
     * Sending Touch events to [ViewImageFrag.onTouchEvent] for scaling image
     * @author Jatin Sahgal
     */
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return fragList[id_view_pager.currentItem].onTouchEvent(event)
    }

    /**
     * Handling Top Left Back button
     * @author Jatin Sahgal
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    /**
     * Removing handler task on window close
     * @author Jatin Sahgal
     */
    override fun onBackPressed() {
        super.onBackPressed()
        handler.removeCallbacks(runnable)
    }

    inner class ManagerAdapter(fragmentManager: FragmentManager) :
        FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int {
            return modelList.size
        }

        override fun getItem(position: Int): Fragment {
            val frag = ViewImageFrag.getInstance(modelList[position].image, position)
            fragList.add(frag)
            return frag
        }
    }

    companion object {
        /**
         * Launch [ViewImageActivity] by sending [Activity], array of [url] and [view] to show animation
         */
        fun launch(
            activity: Activity,
            url: Array<String>,
            view: View? = null,
            adapterPosition: Int? = 0
        ) {
            val intent = Intent(activity, ViewImageActivity::class.java)
                .putExtra(IntentInterface.INTENT_FOR_URL, url)
                .putExtra(IntentInterface.INTENT_FOR_POSITION, adapterPosition)
            if (view != null) {
                ActivityCompat.startActivity(
                    activity,
                    intent,
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity, view, activity.getString(R.string.string_icon_transition_image)
                    ).toBundle()
                )
            } else
                activity.startActivity(intent)
        }
    }


}