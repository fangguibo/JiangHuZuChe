package com.ipd.jianghuzuche.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gyf.barlibrary.ImmersionBar;
import com.ipd.jianghuzuche.R;
import com.ipd.jianghuzuche.adapter.CarPhotoAdapter;
import com.ipd.jianghuzuche.adapter.CarReturnVehicleConditionAdapter;
import com.ipd.jianghuzuche.adapter.ExtendTimeAdapter;
import com.ipd.jianghuzuche.adapter.OrderDetailsVehicleConditionAdapter;
import com.ipd.jianghuzuche.base.BaseActivity;
import com.ipd.jianghuzuche.bean.CancelOrderBean;
import com.ipd.jianghuzuche.bean.CarReturnConfirmBean;
import com.ipd.jianghuzuche.bean.CarReturnDetailsBean;
import com.ipd.jianghuzuche.bean.OrderDetailsBean;
import com.ipd.jianghuzuche.common.view.DividerItemDecoration;
import com.ipd.jianghuzuche.common.view.TopView;
import com.ipd.jianghuzuche.contract.OrderDetailsContract;
import com.ipd.jianghuzuche.presenter.OrderDetailsPresenter;
import com.ipd.jianghuzuche.utils.ApplicationUtil;
import com.ipd.jianghuzuche.utils.SPUtil;
import com.ipd.jianghuzuche.utils.ToastUtil;
import com.ipd.jianghuzuche.utils.isClickUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.ObservableTransformer;

import static com.ipd.jianghuzuche.common.config.IConstants.USER_ID;
import static com.ipd.jianghuzuche.common.config.UrlConfig.BASE_LOCAL_URL;
import static com.ipd.jianghuzuche.utils.ExchangeMapUtil.BD2GCJ;
import static com.ipd.jianghuzuche.utils.isClickUtil.isFastClick;

/**
 * Description ：查看退车单
 * Author ： rmy
 * Email ： 942685687@qq.com
 * Time ： 2019/5/31.
 */
public class SelectReturnCarActivity extends BaseActivity<OrderDetailsContract.View, OrderDetailsContract.Presenter> implements OrderDetailsContract.View {

    @BindView(R.id.tv_order_details_top)
    TopView tvOrderDetailsTop;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.iv_order_details)
    ImageView ivOrderDetails;
    @BindView(R.id.tv_order_details_brand)
    TextView tvOrderDetailsBrand;
    @BindView(R.id.tv_order_details_introduce)
    TextView tvOrderDetailsIntroduce;
    @BindView(R.id.tv_order_details_store_name)
    TextView tvOrderDetailsStoreName;
    @BindView(R.id.tv_order_details_store_path)
    TextView tvOrderDetailsStorePath;
    @BindView(R.id.tv_order_details_get_car_time)
    TextView tvOrderDetailsGetCarTime;
    @BindView(R.id.tv_order_details_go_choice_store)
    TextView tvOrderDetailsGoChoiceStore;
    @BindView(R.id.tv_order_details_use_car_time)
    TextView tvOrderDetailsUseCarTime;
    @BindView(R.id.tv_use_car_service_time)
    TextView tvUseCarServiceTime;
    @BindView(R.id.tv_use_car_service_charge)
    TextView tvUseCarServiceCharge;
    @BindView(R.id.tv_use_car_equipment_cost)
    TextView tvUseCarEquipmentCost;
    @BindView(R.id.tv_use_car_deposit)
    TextView tvUseCarDeposit;
    @BindView(R.id.tv_use_car_coupon_name)
    TextView tvUseCarCouponName;
    @BindView(R.id.tv_use_car_coupon_money)
    TextView tvUseCarCouponMoney;
    @BindView(R.id.tv_use_car_money_sum)
    TextView tvUseCarMoneySum;
    @BindView(R.id.tv_use_car_money_type)
    TextView tvUseCarMoneyType;
    @BindView(R.id.ll_order_details)
    LinearLayout llOrderDetails;
    @BindView(R.id.ll_car_contract)
    LinearLayout llCarContract;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.bt_cancel_pay)
    Button btCancelPay;
    @BindView(R.id.bt_go_pay)
    Button btGoPay;
    @BindView(R.id.tv_refund_sum)
    TextView tvRefundSum;
    @BindView(R.id.ll_refund)
    LinearLayout llRefund;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.ll_end_time)
    LinearLayout llEndTime;
    @BindView(R.id.rv_extended_fee)
    RecyclerView rvExtendedFee;
    @BindView(R.id.ll_extended_fee)
    LinearLayout llExtendedFee;
    @BindView(R.id.rv_vehicle_condition)
    RecyclerView rvVehicleCondition;
    @BindView(R.id.rv_car_photo)
    RecyclerView rvCarPhoto;
    @BindView(R.id.ll_basic_fee)
    LinearLayout llBasicFee;
    @BindView(R.id.tv_vehicle_deposit)
    TextView tvVehicleDeposit;
    @BindView(R.id.tv_overdue_deduction)
    TextView tvOverdueDeduction;
    @BindView(R.id.tv_overdue_deduction_fee)
    TextView tvOverdueDeductionFee;
    @BindView(R.id.ll_car_return_details)
    LinearLayout llCarReturnDetails;

    private OrderDetailsBean.DataBean orderDetailsBean;
    private OrderDetailsVehicleConditionAdapter choiceStoreAdapter;
    private CarReturnVehicleConditionAdapter carReturnVehicleConditionAdapter;
    private List<OrderDetailsBean.DataBean.VehicleOrstatusBean> vehicleOrstatusBean = new ArrayList<>();
    private List<CarReturnDetailsBean.DataBean.VehicleOrstatusBean> carReturnDetailsBean = new ArrayList<>();
    private CarPhotoAdapter carPhotoAdapter;
    private OrderDetailsBean.DataBean.VehiclePicBean vehiclePicBean;
    private CarReturnDetailsBean.DataBean.VehiclePicBean vehiclePicTwoBean;
    private List<String> imgList;
    private List<OrderDetailsBean.DataBean.VehicleEndcostBean> vehicleEndcostBean = new ArrayList<>();
    private ExtendTimeAdapter extendTimeAdapter;
    private CarReturnDetailsBean.DataBean.OrderBean onrderBean = new CarReturnDetailsBean.DataBean.OrderBean();
    private CarReturnDetailsBean.DataBean.StoreBean storeBean = new CarReturnDetailsBean.DataBean.StoreBean();
    private int type;
    private int takeStatus;
    private int orderId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_return_car;
    }

    @Override
    public OrderDetailsContract.Presenter createPresenter() {
        return new OrderDetailsPresenter(this);
    }

    @Override
    public OrderDetailsContract.View createView() {
        return this;
    }

    @Override
    public void init() {
        //将每个Activity加入到栈中
        ApplicationUtil.getManager().addActivity(this);
        //防止状态栏和标题重叠
        ImmersionBar.setTitleBar(this, tvOrderDetailsTop);

        type = getIntent().getIntExtra("type", 0);
        takeStatus = getIntent().getIntExtra("take_status", 0);
        orderId = getIntent().getIntExtra("order_id", 0);

        // 设置管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvVehicleCondition.setLayoutManager(layoutManager);
        rvVehicleCondition.setNestedScrollingEnabled(false);
        rvVehicleCondition.setHasFixedSize(true);
        rvVehicleCondition.setItemAnimator(new DefaultItemAnimator());

        carReturnVehicleConditionAdapter = new CarReturnVehicleConditionAdapter(carReturnDetailsBean);
        rvVehicleCondition.setAdapter(carReturnVehicleConditionAdapter);


        // 延长租期
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        rvExtendedFee.setLayoutManager(layoutManager1);
        rvExtendedFee.setNestedScrollingEnabled(false);
        rvExtendedFee.setHasFixedSize(true);
        rvExtendedFee.setItemAnimator(new DefaultItemAnimator());

        extendTimeAdapter = new ExtendTimeAdapter(vehicleEndcostBean);
        rvExtendedFee.setAdapter(extendTimeAdapter);

        GridLayoutManager NotUseList = new GridLayoutManager(this, 4);
        rvCarPhoto.setLayoutManager(NotUseList);
        rvCarPhoto.addItemDecoration(new DividerItemDecoration(this));
        rvCarPhoto.setNestedScrollingEnabled(false);
        rvCarPhoto.setHasFixedSize(true); //item如果一样的大小，可以设置为true让RecyclerView避免重新计算大小
        rvCarPhoto.setItemAnimator(new DefaultItemAnimator()); //默认动画

        vehiclePicBean = new OrderDetailsBean.DataBean.VehiclePicBean();
        vehiclePicTwoBean = new CarReturnDetailsBean.DataBean.VehiclePicBean();
        imgList = new ArrayList<>();
        carPhotoAdapter = new CarPhotoAdapter(imgList);
        rvCarPhoto.setAdapter(carPhotoAdapter);

        llOrderDetails.setVisibility(View.VISIBLE);
        tvUseCarMoneyType.setText("已支付");
        llCarReturnDetails.setVisibility(View.VISIBLE);
        llBasicFee.setVisibility(View.GONE);
        tvTopTitle.setText("查看退车单");
        btCancelPay.setText("取消订单");
        btGoPay.setText("确认退车");
    }

    @Override
    public void initData() {
        TreeMap<String, String> orderDetailsMap = new TreeMap<>();
        orderDetailsMap.put("userId", SPUtil.get(this, USER_ID, "") + "");
        orderDetailsMap.put("orderId", orderId + "");
        getPresenter().getCarReturnDetails(orderDetailsMap, false, false);
    }

    @Override
    public void initListener() {

    }

    private void setDocumentsReceivedDialog(final int type) {
        final TextView tv;
        final Dialog mCameraDialog = new Dialog(this, R.style.BottomDialog);
        //Dialog布局
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.dialog_center, null);
        root.findViewById(R.id.tv_dialog_center_start).setVisibility(View.GONE);
        tv = root.findViewById(R.id.tv_dialog_center_end);
        tv.setText("是否取消订单？");

        //初始化视图
        root.findViewById(R.id.dialog_center_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClickUtil.isFastClick()) {
                    switch (type) {
                        case 0:
                            if (isFastClick()) {
                                TreeMap<String, String> unpaidCancelOrderMap = new TreeMap<>();
                                unpaidCancelOrderMap.put("userId", SPUtil.get(SelectReturnCarActivity.this, USER_ID, "") + "");
                                unpaidCancelOrderMap.put("orderId", orderId + "");
                                getPresenter().getUnpaidCancelOrder(unpaidCancelOrderMap, false, false);
                            }
                            break;
                        case 1:
                            if (isFastClick()) {
                                TreeMap<String, String> getCarCancelOrderMap = new TreeMap<>();
                                getCarCancelOrderMap.put("userId", SPUtil.get(SelectReturnCarActivity.this, USER_ID, "") + "");
                                getCarCancelOrderMap.put("orderId", orderId + "");
                                getPresenter().getGetCarCancelOrder(getCarCancelOrderMap, false, false);
                            }
                            break;
                        case 2:
                            if (isFastClick()) {
                                TreeMap<String, String> carReturnCancelOrderMap = new TreeMap<>();
                                carReturnCancelOrderMap.put("userId", SPUtil.get(SelectReturnCarActivity.this, USER_ID, "") + "");
                                carReturnCancelOrderMap.put("orderId", orderId + "");
                                getPresenter().getCarReturnCancelOrder(carReturnCancelOrderMap, false, false);
                            }
                            break;
                    }
                    mCameraDialog.dismiss();
                }
            }
        });

        root.findViewById(R.id.dialog_center_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraDialog.dismiss();
            }
        });
        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER); //设置弹出方式
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = 700;
        root.measure(0, 0);
        lp.height = 320;

        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mCameraDialog.show();
    }

    @OnClick({R.id.ll_car_contract, R.id.tv_order_details_go_choice_store, R.id.bt_cancel_pay, R.id.bt_go_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_car_contract:
                startActivity(new Intent(this, WebViewActivity.class).putExtra("h5Type", 2));
                break;
            case R.id.tv_order_details_go_choice_store:
                setMapDialog();
                break;
            case R.id.bt_cancel_pay:
                setDocumentsReceivedDialog(2);
                break;
            case R.id.bt_go_pay:
                if (isClickUtil.isFastClick()) {
                    TreeMap<String, String> carReturnConfirmMap = new TreeMap<>();
                    carReturnConfirmMap.put("userId", SPUtil.get(this, USER_ID, "") + "");
                    carReturnConfirmMap.put("orderId", orderId + "");
                    getPresenter().getCarReturnConfirm(carReturnConfirmMap, false, false);
                }
                break;
        }
    }

    // 选择地图
    private void setMapDialog() {
        final Dialog mCameraDialog = new Dialog(this, R.style.BottomDialog);
        //Dialog布局
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.dialog_map, null);
        root.findViewById(R.id.ll_baidu_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBaiduMap(onrderBean.getDescAddress());
                mCameraDialog.dismiss();
            }
        });
        root.findViewById(R.id.ll_gaode_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGaodeMap(storeBean.getLatitude(), storeBean.getLongitude(), onrderBean.getDescAddress());
                mCameraDialog.dismiss();
            }
        });
        root.findViewById(R.id.ll_tengxun_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToTengxunMap(storeBean.getLatitude(), storeBean.getLongitude(), onrderBean.getDescAddress());
                mCameraDialog.dismiss();
            }
        });
        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM); //设置弹出方式
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = 500;

        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mCameraDialog.show();
    }

    /**
     * 跳转高德地图
     */
    private void goToGaodeMap(String lat, String lon, String descAddress) {
        if (!isInstalled("com.autonavi.minimap")) {
            ToastUtil.showShortToast("请先安装高德地图客户端");
            return;
        }
        LatLng endPoint = BD2GCJ(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon)));//坐标转换
        StringBuffer stringBuffer = new StringBuffer("amapuri://openFeature?featureName=").append("OnRideNavi");
        stringBuffer.append("&rideType=").append("bike")
                .append("&sourceApplication=").append("amap")
                .append("&lat=").append(endPoint.latitude)
                .append("&lon=").append(endPoint.longitude)
                .append("&dev=").append(0);
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(stringBuffer.toString()));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setPackage("com.autonavi.minimap");
        startActivity(intent);
    }

    /**
     * 跳转腾讯地图
     */
    private void goToTengxunMap(String lat, String lon, String descAddress) {
        if (!isInstalled("com.tencent.map")) {
            ToastUtil.showShortToast("请先安装腾讯地图客户端");
            return;
        }
        LatLng endPoint = BD2GCJ(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon)));//坐标转换
        StringBuffer stringBuffer = new StringBuffer("qqmap://map/routeplan?type=bike")
                .append("&tocoord=").append(endPoint.latitude).append(",").append(endPoint.longitude).append("&to=" + descAddress);
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(stringBuffer.toString()));
        startActivity(intent);
    }

    /**
     * 跳转百度地图
     */
    private void goToBaiduMap(String descAddress) {
        if (!isInstalled("com.baidu.BaiduMap")) {
            ToastUtil.showShortToast("请先安装百度地图客户端");
            return;
        }
        Intent intent = new Intent();
        intent.setData(Uri.parse("baidumap://map/direction?destination="
                + descAddress + // 终点
                "&mode=riding" + // 导航路线方式
                "&coord_type=bd09ll" + // 坐标系
                "&src=" + getPackageName()));
        startActivity(intent); // 启动调用
    }

    /**
     * 检测程序是否安装
     *
     * @param packageName
     * @return
     */
    private boolean isInstalled(String packageName) {
        PackageManager manager = this.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> installedPackages = manager.getInstalledPackages(0);
        if (installedPackages != null) {
            for (PackageInfo info : installedPackages) {
                if (info.packageName.equals(packageName))
                    return true;
            }
        }
        return false;
    }

    @Override
    public void resultOrderDetails(OrderDetailsBean data) {
    }

    @Override
    public void resultUnpaidCancelOrder(CancelOrderBean data) {
        ToastUtil.showLongToast(data.getMsg());
        if (data.getCode() == 200) {
            setResult(RESULT_OK, new Intent().putExtra("refresh", 0));
            finish();
        }
    }

    @Override
    public void resultCarReturnDetails(CarReturnDetailsBean data) {
        if (data.getData().getStatus() == 2) {
            onrderBean = data.getData().getOrder();
            storeBean = data.getData().getStore();
            Glide.with(ApplicationUtil.getContext()).load(BASE_LOCAL_URL + data.getData().getOrder().getVehicleLogo()).apply(new RequestOptions().placeholder(R.mipmap.ic_launcher)).into(ivOrderDetails);
            tvOrderDetailsBrand.setText(data.getData().getOrder().getVehicleName());
            tvOrderDetailsIntroduce.setText(data.getData().getOrder().getVehicleModel());
            tvOrderDetailsStoreName.setText(data.getData().getOrder().getStoreName());
            tvOrderDetailsStorePath.setText(data.getData().getOrder().getDescAddress());
            tvOrderDetailsGetCarTime.setText(data.getData().getOrder().getTakevehicleTime() + "（" + data.getData().getOrder().getWeek() + "）");
            tvOrderDetailsUseCarTime.setText(data.getData().getOrder().getRentDuration() + "个月");
            tvEndTime.setText(data.getData().getOrder().getExpireTime());

            tvVehicleDeposit.setText(data.getData().getVehicleCost().getDeposit() + "元");
            if (type == 3) {
                tvOverdueDeduction.setText("违约扣款");
                tvOverdueDeductionFee.setText("-" + data.getData().getVehicleCost().getDefaultMoney() + "元");
            } else if (type == 4) {
                tvOverdueDeduction.setText("逾期扣款");
                tvOverdueDeductionFee.setText("-" + data.getData().getVehicleCost().getOverdueMoney() + "元");
            }

            llRefund.setVisibility(View.VISIBLE);
            tvRefundSum.setText(data.getData().getVehicleCost().getRefundMoney() + "元");

            carReturnDetailsBean.clear();
            carReturnDetailsBean.addAll(data.getData().getVehicleOrstatus());
            carReturnVehicleConditionAdapter.setNewData(carReturnDetailsBean);

            vehiclePicTwoBean = data.getData().getVehiclePic();
            String[] strs = vehiclePicTwoBean.getPicPath().split(",");
            for (int i = 0, len = strs.length; i < len; i++) {
                imgList.add(strs[i].toString());
            }
            carPhotoAdapter.setNewData(imgList);

            vehicleEndcostBean.clear();
            if (data.getData().getVehicleEndcost().size() > 0) {
                llExtendedFee.setVisibility(View.GONE);
                for (int i = 0; i < data.getData().getVehicleEndcost().size(); i++) {
                    OrderDetailsBean.DataBean.VehicleEndcostBean aaa = new OrderDetailsBean.DataBean.VehicleEndcostBean();
                    aaa.setTenancyService(data.getData().getVehicleEndcost().get(i).getTenancyService());
                    aaa.setChargeMoney(data.getData().getVehicleEndcost().get(i).getChargeMoney());
                    aaa.setCoupon(data.getData().getVehicleEndcost().get(i).getCoupon());
                    aaa.setTotal(data.getData().getVehicleEndcost().get(i).getTotal());
                    aaa.setVehicleRent(data.getData().getVehicleEndcost().get(i).getVehicleRent());
                    vehicleEndcostBean.add(aaa);
                }
                extendTimeAdapter.setNewData(vehicleEndcostBean);
            }
        } else if (data.getData().getStatus() == 1) {
            //空数据时的页面
            setContentView(R.layout.null_select_car);

            TopView tvNullSelectCarTop = findViewById(R.id.tv_null_select_car_top);
            //防止状态栏和标题重叠
            ImmersionBar.setTitleBar(this, tvNullSelectCarTop);
        }
    }

    @Override
    public void resultCarReturnCancelOrder(CancelOrderBean data) {
        ToastUtil.showLongToast(data.getMsg());
        if (data.getCode() == 200) {
            setResult(RESULT_OK, new Intent().putExtra("refresh", 0));
            finish();
        }
    }

    @Override
    public void resultCarReturnConfirm(CarReturnConfirmBean data) {
        ToastUtil.showLongToast(data.getMsg());
        if (data.getCode() == 200) {
            setResult(RESULT_OK, new Intent().putExtra("refresh", 0));
            finish();
        }
    }

    @Override
    public void resultGetCarCancelOrder(CancelOrderBean data) {
        ToastUtil.showLongToast(data.getMsg());
        if (data.getCode() == 200) {
            setResult(RESULT_OK, new Intent().putExtra("refresh", 0));
            finish();
        }
    }

    @Override
    public <T> ObservableTransformer<T, T> bindLifecycle() {
        return this.bindToLifecycle();
    }
}
