package com.craft.apps.countdowns.purchase;

import android.app.Activity;
import android.util.Log;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClient.BillingResponse;
import com.android.billingclient.api.BillingClient.SkuType;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.craft.apps.countdowns.common.privilege.UserPrivileges;
import com.craft.apps.countdowns.common.privilege.UserPrivileges.Privilege;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0.0
 * @since 1.0.0
 */
public class Biller implements PurchasesUpdatedListener {

    public static final String FEATURE_DISABLE_ADS = "feature_disable_ads";
    private static final String TAG = Biller.class.getSimpleName();
    private BillingClient mBillingClient;

    private Activity mActivity;

    public Biller(Activity activity) {
        mBillingClient = new BillingClient.Builder(activity).setListener(this).build();
        mActivity = activity;
    }

    public void disableAds(String userId) {
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(int resultCode) {
                if (resultCode == BillingResponse.OK) {
                    Log.d(TAG, "onBillingSetupFinished: result ok");
                    List<String> skuList = new ArrayList<>();
                    skuList.add(UserPrivileges.DISABLED_ADS);
                    mBillingClient.querySkuDetailsAsync(SkuType.INAPP, skuList, result -> {
                        Log.d(TAG, "onBillingSetupFinished: Fetching SKUs");
                        if (result.getResponseCode() == BillingResponse.OK) {
                            for (SkuDetails details : result.getSkuDetailsList()) {
                                Log.d(TAG, "onBillingSetupFinished: SKU details: " + details);
                                if (FEATURE_DISABLE_ADS.equals(details.getSku())) {
                                    // Process the result.
                                    purchaseDisableAds(mActivity, UserPrivileges.DISABLED_ADS,
                                            userId);

                                }
                            }
                            Log.d(TAG, "onBillingSetupFinished: Done fetching SKUs");
                        } else {
                            Log.d(TAG, "onBillingSetupFinished: Failed fetching SKU details");
                        }
                    });
                } else {
                    Log.w(TAG, "onBillingSetupFinished: Result not okay");
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                Log.i(TAG, "onBillingServiceDisconnected: ");
            }
        });
    }

    private void purchaseDisableAds(Activity activity, @Privilege String sku, String userId) {
        Log.d(TAG, "purchaseDisableAds: Starting purchase");
        BillingFlowParams.Builder builder = new BillingFlowParams.Builder()
                .setSku(UserPrivileges.DISABLED_ADS)
                .setType(SkuType.INAPP);
        if (mBillingClient.launchBillingFlow(activity, builder.build()) == BillingResponse.OK) {
            UserPrivileges.enableFor(sku, userId, hasPrivilege -> {
                Log.d(TAG, "purchaseDisableAds: Enabling privilege");
            });
        }
    }

    @Override
    public void onPurchasesUpdated(int responseCode, List<Purchase> purchases) {

    }
}
