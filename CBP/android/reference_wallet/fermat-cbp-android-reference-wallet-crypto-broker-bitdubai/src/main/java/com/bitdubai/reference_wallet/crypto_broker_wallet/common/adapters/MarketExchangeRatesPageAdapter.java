package com.bitdubai.reference_wallet.crypto_broker_wallet.common.adapters;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.bitdubai.fermat_cbp_api.layer.wallet_module.common.interfaces.IndexInfoSummary;
import com.bitdubai.fermat_cbp_api.layer.wallet_module.crypto_broker.interfaces.CryptoBrokerWalletManager;
import com.bitdubai.reference_wallet.crypto_broker_wallet.fragments.home.MarketRateStatisticsFragment;
import com.bitdubai.reference_wallet.crypto_broker_wallet.session.CryptoBrokerWalletSession;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * {@link FragmentStatePagerAdapter} for the fragment that show the market rates of different currencies
 *
 * @author Nelson Ramirez
 * @version 1.0
 * @since 16/11/15.
 */
public class MarketExchangeRatesPageAdapter extends FragmentStatePagerAdapter {

    private final List<IndexInfoSummary> summaryList;
    private final Activity activity;
    private CryptoBrokerWalletSession session;

    public MarketExchangeRatesPageAdapter(Activity activity, CryptoBrokerWalletSession session, Collection<IndexInfoSummary> summaryList) {
        super(activity.getFragmentManager());

        this.activity = activity;
        this.session = session;
        this.summaryList = new ArrayList<>();
        this.summaryList.addAll(summaryList);
    }

    @Override
    public int getCount() {
        return summaryList.size();
    }

    @Override
    public Fragment getItem(int position) {
        MarketRateStatisticsFragment fragment = MarketRateStatisticsFragment.newInstance();
        IndexInfoSummary summary = summaryList.get(position);
        fragment.bind(summary, session, activity);

        return fragment;
    }
}
