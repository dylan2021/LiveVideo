package com.android.livevideo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Gool Lee
 */
public class DCViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> list;
    private String[] tabList;


    public DCViewPagerAdapter(FragmentManager fm, List<Fragment> list, String[] tabList) {
        super(fm);
        this.list = list;
        this.tabList = tabList;
    }

    public void setList(List<Fragment> list, String[] tabList) {
        this.list = list;
        this.tabList = tabList;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabList==null?"":tabList[position];
    }
   /* @Override
    public void finishUpdate(ViewGroup container) {
        try{
            super.finishUpdate(container);
        } catch (Exception nullPointerException){
            Log.d("777", "游戏详情界面异常:Catch the NullPointerException in FragmentPagerAdapter.finishUpdate");
        }
    }*/
}