package cn.gisdata.footprint.module.my.func.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fxs on 2016/9/19.
 * 功能：TabLayout的适配器
 */
public class MyPagerAdapter extends FragmentPagerAdapter {
    public List<Fragment> fragmentList = new ArrayList<>();
    public List<String> fragmentTitleList = new ArrayList<>();

    public MyPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        fragmentList.clear();
        fragmentTitleList.clear();
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }
}
