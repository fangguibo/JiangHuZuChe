package com.ipd.jianghuzuche.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.ipd.jianghuzuche.R;
import com.ipd.jianghuzuche.activity.MainActivity;
import com.ipd.jianghuzuche.adapter.ViewPagerAdapter;
import com.ipd.jianghuzuche.base.BaseFragment;
import com.ipd.jianghuzuche.base.BasePresenter;
import com.ipd.jianghuzuche.base.BaseView;
import com.ipd.jianghuzuche.common.view.NavitationFollowScrollLayoutText;
import com.ipd.jianghuzuche.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Description ：订单查看
 * Author ： rmy
 * Email ： 942685687@qq.com
 * Time ： 2019/4/29.
 */
public class SelectOrderFragment extends BaseFragment {

    @BindView(R.id.nl_select_order)
    NavitationFollowScrollLayoutText nlSelectOrder;
    @BindView(R.id.vp_select_order)
    ViewPager vpSelectOrder;

    private String[] titles = new String[]{"全部", "待付款", "待取车", "使用中", "已到期", "已完成", "已取消"};
    private ViewPagerAdapter viewPagerAdapter;
    private List<Fragment> fragments;
    private AllOrderTypeFragment fm;
    private int positions = 0;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_select_order;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public BaseView createView() {
        return null;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.tvTopTitle.setText(this.getResources().getString(R.string.select_order));
        mainActivity.tvTopTitle.setTextColor(Color.BLACK);
        if (fm != null) {
            LogUtils.i("rmy", "onHiddenChanged");
            fm.Aaa(positions);
        }
    }

    @Override
    public void init(View view) {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.tvTopTitle.setText(this.getResources().getString(R.string.select_order));
        mainActivity.tvTopTitle.setTextColor(Color.BLACK);
        fragments = new ArrayList<>();

        //向集合添加Fragment
        for (int i = 0; i < titles.length; i++) {
            fm = new AllOrderTypeFragment();
            Bundle args = new Bundle();
            args.putInt("fm_type", i);
            fm.setArguments(args);
            fragments.add(fm);
        }

        viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), fragments);
        vpSelectOrder.setAdapter(viewPagerAdapter);
        vpSelectOrder.setOffscreenPageLimit(titles.length);

        //设置导航条
        nlSelectOrder.setViewPager(getActivity(), titles, vpSelectOrder, R.color.tx_bottom_navigation, R.color.tx_bottom_navigation_select, 14, 14, 24, true, R.color.black, 0, 0, 0, 80);
        nlSelectOrder.setBgLine(getActivity(), 1, R.color.whitesmoke);
        nlSelectOrder.setNavLine(getActivity(), 3, R.color.tx_bottom_navigation_select);
    }

    @Override
    public void initListener() {
        nlSelectOrder.setOnNaPageChangeListener(new NavitationFollowScrollLayoutText.OnNaPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                positions = position;
                LogUtils.i("rmy", "onPageScrolled = " + position);
//                fm.Aaa(position);
            }

            @Override
            public void onPageSelected(int position) {
                LogUtils.i("rmy", "onPageSelected = " + position);
                fm.Aaa(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                LogUtils.i("rmy", "onPageScrollStateChanged = " + state);
            }
        });
    }

    @Override
    public void initData() {

    }
}
