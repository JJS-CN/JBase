package com.jjs.base.pay;

import android.text.TextUtils;

import com.tencent.mm.opensdk.modelbase.BaseResp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * 说明：
 * Created by jjs on 2018/7/18.
 */

public class PayResult {
    private String resultStatus;
    private String result;
    private String memo;
    private int PAY_TYPE = 0;//1为支付宝 2为微信，本类内部判断使用
    private BaseResp wxResp;

    public PayResult(Map<String, String> result) {
        if (result == null) {
            throw new NullPointerException("aliPay result is null");
        }
        this.PAY_TYPE = 1;
        this.memo = result.get("memo");
        this.result = result.get("result");
        this.resultStatus = result.get("resultStatus");
    }

    public PayResult(BaseResp wxResp) {
        if (wxResp == null) {
            new NullPointerException("wxPay result is null");
        }
        this.wxResp = wxResp;
        this.PAY_TYPE = 2;
    }


    //根据状态码，判断是否支付成功
    public boolean isSuccess() {
        if (PAY_TYPE == 1) {
            return TextUtils.equals(resultStatus, "9000");
        } else if (PAY_TYPE == 2) {
            return wxResp.errCode == 0;
        }
        return false;
    }

    //错误提示
    public String errMessage() {
        if (PAY_TYPE == 1) {
            if (TextUtils.equals(resultStatus, "9000")) {
                return "支付成功";
            } else {
                switch (resultStatus) {
                    case "8000":
                        return "正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态";
                    case "5000":
                        return "重复请求";
                    case "6001":
                        return "用户中途取消";
                    case "6002":
                        return "网络连接出错";
                    case "6004":
                        return "支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态";
                    case "4000":
                        // "支付失败";
                    default:
                        if (result != null) {
                            try {
                                JSONObject object = new JSONObject(result);
                                JSONObject info = object.getJSONObject("alipay_trade_app_pay_response");
                                String code = info.getString("code");
                                switch (code) {
                                    case "10000":
                                        return "商户接口调用成功";
                                    case "20000":
                                        return "商户服务不可用";
                                    case "20001":
                                        return "商户授权权限不足";
                                    case "40001":
                                        return "商户缺少必选参数";
                                    case "40002":
                                        return "商户非法的参数";
                                    case "40004":
                                        return "商户业务处理失败";
                                    case "40006":
                                        return "商户权限不足";

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                }
            }
        } else if (PAY_TYPE == 2) {
            switch (wxResp.errCode) {
                case 0:
                    return "支付成功";
                case -1:
                    return "支付失败";
                case -2:
                    return "支付取消";
                default:
                    return "支付失败";
            }
        }
        return "未知错误";
    }

    //支付状态码
    public String getResultStatus() {
        return resultStatus;
    }

    public String getMemo() {
        return memo;
    }

    public String getResult() {
        return result;
    }


    @Override
    public String toString() {
        return "resultStatus={" + resultStatus + "};memo={" + memo
                + "};result={" + result + "}";
    }
}
