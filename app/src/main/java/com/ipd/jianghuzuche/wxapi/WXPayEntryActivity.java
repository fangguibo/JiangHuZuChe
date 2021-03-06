package com.ipd.jianghuzuche.wxapi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.ipd.jianghuzuche.R;
import com.ipd.jianghuzuche.activity.MainActivity;
import com.ipd.jianghuzuche.base.BaseActivity;
import com.ipd.jianghuzuche.base.BasePresenter;
import com.ipd.jianghuzuche.base.BaseView;
import com.ipd.jianghuzuche.common.view.TopView;
import com.ipd.jianghuzuche.utils.ApplicationUtil;
import com.ipd.jianghuzuche.utils.LogUtils;
import com.ipd.jianghuzuche.utils.isClickUtil;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.BindView;
import butterknife.OnClick;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    @BindView(R.id.tv_pay_type_top)
    TopView tvPayTypeTop;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.ll_top_back)
    LinearLayout llTopBack;
    @BindView(R.id.iv_pay_type)
    ImageView ivPayType;
    @BindView(R.id.tv_pay_type)
    TextView tvPayType;
    @BindView(R.id.tv_pay_type_tips)
    TextView tvPayTypeTips;
    @BindView(R.id.bt_pay_type)
    Button btPayType;
    @BindView(R.id.ll_pay_type)
    LinearLayout llPayType;

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
    private IWXAPI api;

    @Override
    public int getLayoutId() {
        return R.layout.activity_pay_type;
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
    public void init() {
        //将每个Activity加入到栈中
        ApplicationUtil.getManager().addActivity(this);
        //防止状态栏和标题重叠
        ImmersionBar.setTitleBar(this, tvPayTypeTop);

        api = WXAPIFactory.createWXAPI(this, "wx1a65c563b86ec579");
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void onResp(BaseResp resp) {
        LogUtils.d(TAG, "onPayFinish, errCode = " + resp.errCode);

        if (resp.errCode == 0) {
            ivPayType.setImageResource(R.drawable.ic_pay_success);
            tvPayType.setText("支付成功");
            tvPayTypeTips.setText("您已支付成功，请在约定时间内前往店内取车");
            llPayType.setVisibility(View.GONE);
        } else {
            ivPayType.setImageResource(R.drawable.ic_pay_fail);
            tvPayType.setText("支付失败");
            tvPayTypeTips.setText("很遗憾，您支付失败，请重新支付");
            llPayType.setVisibility(View.VISIBLE);
        }
//        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("支付结果");
//            builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
//            builder.show();
//        }
    }

    @Override
    public void onBackPressed() {
        if (this instanceof Activity && isClickUtil.isFastClick()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            if (getCurrentFocus() != null) {
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        finish();
    }

    @OnClick({R.id.bt_pay_type, R.id.ll_top_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_top_back:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.bt_pay_type:
                ApplicationUtil.getManager().finishActivity(MainActivity.class);
                startActivity(new Intent(this, MainActivity.class).putExtra("howPage", 1));
                finish();
                break;
        }
    }
}
