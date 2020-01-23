package com.luka.mackovic.eus.ui.activity;

import android.annotation.SuppressLint;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.luka.mackovic.eus.R;
import com.luka.mackovic.eus.ui.adapter.HomeAdapter;
import com.luka.mackovic.eus.ui.fragment.EventsFragment_;
import com.luka.mackovic.eus.ui.fragment.NewsFragment_;
import com.luka.mackovic.eus.ui.fragment.ProfileFragment_;
import com.luka.mackovic.eus.usecase.crud.NotificationUseCase;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

@SuppressLint("Registered")
@EActivity(R.layout.activity_home)
public class HomeActivity extends DaggerAppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener,
        ViewPager.OnPageChangeListener {

    private final static int INITIALLY_SELECTED_FRAGMENT_INDEX = 0;

    @ViewById
    ViewPager viewPager;

    @ViewById
    BottomNavigationView bottomNavigationView;

    @Inject
    NotificationUseCase notificationUseCase;

    @AfterViews
    public void init() {
        setupNavigationView();
    }

    private void setupNavigationView() {
        HomeAdapter homeAdapter = new HomeAdapter(getSupportFragmentManager());

        homeAdapter.addFragment(new EventsFragment_());
        homeAdapter.addFragment(new NewsFragment_());
        homeAdapter.addFragment(new ProfileFragment_());

        viewPager.setAdapter(homeAdapter);
        viewPager.setCurrentItem(INITIALLY_SELECTED_FRAGMENT_INDEX);

        bottomNavigationView.setSelectedItemId(R.id.nav_events);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_events:
                viewPager.setCurrentItem(0);
                break;
            case R.id.nav_articles:
                viewPager.setCurrentItem(1);
                break;
            case R.id.nav_profile:
                viewPager.setCurrentItem(2);
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
    @Override
    public void onPageSelected(int position) {
        bottomNavigationView.getMenu().getItem(position).setChecked(true);
    }
    @Override
    public void onPageScrollStateChanged(int state) {}
}