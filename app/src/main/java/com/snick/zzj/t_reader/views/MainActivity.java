package com.snick.zzj.t_reader.views;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.snick.zzj.t_reader.R;
import com.snick.zzj.t_reader.beans.NewsThemes;
import com.snick.zzj.t_reader.presenter.MainNavPresenter;
import com.snick.zzj.t_reader.presenter.impl.MainNavPresenterImpl;
import com.snick.zzj.t_reader.views.fragment.BaseFragment;
import com.snick.zzj.t_reader.views.fragment.SingleThemeFragment;

public class MainActivity extends BaseActivity
        implements MainNavView, NavigationView.OnNavigationItemSelectedListener {

    private MainNavPresenter mainNavPresenter;
    private NavigationView navigationView;

    private NewsThemes cachedNewsThemes;

    private SingleThemeFragment singleThemeFragment;
    private BaseFragment homepageFragment;

    private int oldItemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (null != getSupportActionBar())
            getSupportActionBar().setTitle(R.string.title_home_page);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mainNavPresenter = new MainNavPresenterImpl(this,this);
        mainNavPresenter.getThemes();

        homepageFragment = new BaseFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.content, homepageFragment, "homepage").commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int itemId = item.getItemId();
        int groupId = item.getGroupId();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


        /*
          这里用了hide & show 避免创建过多的fragment实例，由于没有做缓存，每次进入theme还是要加载。
         */
        if(groupId == 0) {
            if (null != getSupportActionBar())
                getSupportActionBar().setTitle(R.string.title_home_page);

            //跳转主页
            if(homepageFragment == null) {
                homepageFragment = new BaseFragment();
            }

            if(homepageFragment.isAdded()) {
                homepageFragment.refresh();
                if (singleThemeFragment != null)
                    ft.hide(singleThemeFragment);
                ft.show(homepageFragment);
                ft.commit();
            }
            if(oldItemId != -1) {
                navigationView.getMenu().getItem(oldItemId)
                        .getActionView().setBackgroundColor(Color.parseColor("#ffffff"));
                oldItemId = -1;
            }
        } else {
            if (null != getSupportActionBar())
                getSupportActionBar().setTitle(cachedNewsThemes.getOthers().get(itemId).getName());
            if(oldItemId != -1) {
                navigationView.getMenu().getItem(oldItemId)
                        .getActionView().setBackgroundColor(Color.parseColor("#ffffff"));
            }
            item.getActionView().setBackgroundColor(Color.parseColor("#e5e5e5"));
            /*
              因为把homepage单独作为了一个viewgroup，这里有一个注意点：
              item.getItemId是没有计算homepage这个group0的item的。
              但是navigationView.getMenu().getItem传入的Id是全局的，会把所有group包含在内。
             */
            oldItemId = item.getItemId()+1;
            //跳转分页
            if(singleThemeFragment == null) {
                singleThemeFragment = new SingleThemeFragment();
            }

            if(singleThemeFragment.isAdded()) {
                singleThemeFragment.refresh(String.valueOf(cachedNewsThemes.getOthers().get(itemId).getId()));
                ft.hide(homepageFragment);
                ft.show(singleThemeFragment);
                ft.commit();
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("theme_id", String.valueOf(cachedNewsThemes.getOthers().get(itemId).getId()));
                singleThemeFragment.setArguments(bundle);
                ft.add(R.id.content, singleThemeFragment, "singletheme");
                ft.hide(homepageFragment);
                ft.commit();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onThemesLoaded(NewsThemes themes) {
        cachedNewsThemes = themes;
        navigationView.getMenu().clear();
        navigationView.getMenu().add(0, 0, 0, null)
                .setTitle(R.string.title_home_page)
                .setIcon(R.drawable.ic_home_black_24dp);
        View view;
        TextView textView;
        for(int i = 0; i < themes.getOthers().size(); i ++) {
            view = LayoutInflater.from(this).inflate(R.layout.nav_menu_item_layout, null);
            textView = (TextView) view.findViewById(R.id.nav_item_title);
            textView.setText(themes.getOthers().get(i).getName());
            navigationView.getMenu().add(1, i, i, null).setActionView(view);
        }
        navigationView.invalidate();
    }
}
