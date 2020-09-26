package com.tc.utils.utils.helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.tc.utils.R;

import java.lang.reflect.Field;

import static android.content.Context.SEARCH_SERVICE;

public class MenuUtils {
    @SuppressLint("StaticFieldLeak")
    private static MenuUtils menuUtils = new MenuUtils();
    private ActionBar actionBar;

    public static synchronized MenuUtils getInstance() {
        return menuUtils;
    }

    public void setToolbar(AppCompatActivity activity, int title) {
        setTitle(activity, activity.getString(title));
    }

    public void setToolbar(AppCompatActivity activity, String title) {
        Toolbar toolbar = activity.findViewById(R.id.id_app_bar);
        activity.setSupportActionBar(toolbar);
        setTitle(activity, title);
    }

    public void setToolbarWithSearch(AppCompatActivity activity, int title) {
        setToolbarWithSearch(activity, activity.getString(title));
    }

    public void setToolbarWithSearch(AppCompatActivity activity, String title) {
        Toolbar toolbar = activity.findViewById(R.id.id_app_bar);
        activity.setSupportActionBar(toolbar);
        upArrow = activity.getResources().getDrawable(R.drawable.icon_vd_arrow_back);
        upArrow.setColorFilter(ContextCompat.getColor(activity, R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        actionBar = setTitle(activity, title);
        if (actionBar != null)
            actionBar.setHomeAsUpIndicator(upArrow);

    }

    public void hideToolBar() {
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    public ActionBar setTitle(AppCompatActivity activity, String title) {
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        return actionBar;
    }

    public boolean searchViewExpended = false;
    private MenuItem mSearchMenu;
    private SearchView searchView;
    private Drawable upArrow;

    public void initializeView(int id, final Activity activity, Menu menu, final View.OnClickListener onClickListener,
                               final OnQueryTextListener onQueryTextListener, SearchView.OnCloseListener onCloseListener, int appBarColor, boolean hideAtClose) {
        mSearchMenu = menu.findItem(id);
        searchView = (SearchView) mSearchMenu.getActionView();
        SearchManager searchManager = (SearchManager) activity.getSystemService(SEARCH_SERVICE);
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(activity.getComponentName()));
        }
        final AutoCompleteTextView searchTextView = searchView.findViewById(com.google.android.material.R.id.search_src_text);
        searchView.setOnSearchClickListener(v -> {
            onClickListener.onClick(null);
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
            upArrow.setColorFilter(ContextCompat.getColor(activity, R.color.colorAppBarIcon), PorterDuff.Mode.SRC_ATOP);
            searchViewExpended = true;
            ActionBarAnimReveal.circleReveal(activity, R.id.id_app_bar,
                    true, R.color.colorPrimary);
        });
        AppCompatImageView closeButton = searchView.findViewById(com.google.android.material.R.id.search_close_btn);
        closeButton.setImageResource(R.drawable.icon_vd_clear);
        searchTextView.setTextColor(ContextCompat.getColor(activity, R.color.colorTextPrimary));
        searchTextView.setHint("Search...");
        searchTextView.setTypeface(null, Typeface.NORMAL);
        searchTextView.setHintTextColor(ContextCompat.getColor(activity, R.color.colorTextHint));
        searchView.setOnCloseListener(() -> {
            if (!TextUtils.isEmpty(searchTextView.getText())) {
                searchTextView.setText("");
            } else {
                closeSearchView(activity, appBarColor, hideAtClose);
                onCloseListener.onClose();
            }
            return false;
        });
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchTextView, R.drawable.cursor_black);
        } catch (Exception ignored) {
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onQueryTextListener.getQueryString(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                onQueryTextListener.getQueryString(newText);
                return true;
            }
        });

    }

    public void closeSearchView(Activity activity, int appBarColor, boolean hideAtClose) {
        searchViewExpended = false;
        searchView.onActionViewCollapsed();
        mSearchMenu.collapseActionView();
        if (hideAtClose) {
            upArrow.setColorFilter(ContextCompat.getColor(activity, R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        } else {
            hideToolBar();
        }
        ActionBarAnimReveal.circleReveal(activity, R.id.id_app_bar, false, appBarColor);
    }


    public boolean isSearchViewExpended() {
        return searchViewExpended;
    }

    public boolean setHomeBackButton(Activity activity, MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            activity.onBackPressed();
            return true;
        }
        return false;
    }

    public interface OnQueryTextListener {
        void getQueryString(String query);
    }

    public void menuItemIconTint(MenuItem menuItem, int color) {
        Drawable drawable = menuItem.getIcon();
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
    }

    public void menuItemIconChange(Context context, int drawableId, MenuItem menuItem) {
        menuItem.setIcon(ContextCompat.getDrawable(context, drawableId));
    }

    public void menuItemTextColor(MenuItem menuItem, int color) {
        SpannableString s = new SpannableString(menuItem.getTitle());
        s.setSpan(new ForegroundColorSpan(color), 0, s.length(), 0);
        menuItem.setTitle(s);
    }

}
