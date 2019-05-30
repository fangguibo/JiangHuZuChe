package com.ipd.jianghuzuche.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ipd.jianghuzuche.R;
import com.ipd.jianghuzuche.bean.SelectVehicleBean;

import java.util.List;

/**
 * Description ：
 * Author ： MengYang
 * Email ： 942685687@qq.com
 * Time ： 2019/5/13.
 */
public class VehicleConditionAdapter extends BaseQuickAdapter<SelectVehicleBean.DataBean.VehicleOrstatusBean, BaseViewHolder> {

    private String vehicleConditionType;

    public VehicleConditionAdapter(@Nullable List<SelectVehicleBean.DataBean.VehicleOrstatusBean> data) {
        super(R.layout.adapter_vehicle_condition, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SelectVehicleBean.DataBean.VehicleOrstatusBean item) {
        switch (item.getStatus()) {
            case 1:
                vehicleConditionType = "正常";
                break;
            case 2:
                vehicleConditionType = "破损";
                break;
        }
        helper.setText(R.id.tv_vehicle_condition_name, item.getVestatusName())
                .setText(R.id.tv_vehicle_condition_type, vehicleConditionType);
    }
}
