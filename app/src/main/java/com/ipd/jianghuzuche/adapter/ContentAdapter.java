package com.ipd.jianghuzuche.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Elvira on 2016/10/10.
 * 引导页存放tab的页面
 */

public class ContentAdapter extends PagerAdapter {

    private final List<View> listView;
    private final String[] titless;

    public ContentAdapter(List<View> viewList, String[] titless) {
        super();
        this.listView = viewList;
        this.titless = titless;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        View x = listView.get(position);
        collection.addView(x, 0);
        return x;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) listView.get(position));
    }

    @Override
    public int getCount() {
        return listView .size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titless[position];
    }
}
