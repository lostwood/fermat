package com.bitdubai.reference_wallet.crypto_customer_wallet.fragments.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_android_api.ui.Views.PresentationDialog;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.CryptoCurrency;
import com.bitdubai.fermat_api.layer.all_definition.enums.FiatCurrency;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Wallets;
import com.bitdubai.fermat_api.layer.all_definition.settings.structure.SettingsManager;
import com.bitdubai.fermat_api.layer.pip_engine.interfaces.ResourceProviderManager;
import com.bitdubai.fermat_api.layer.world.interfaces.Currency;
import com.bitdubai.fermat_cbp_api.all_definition.enums.MoneyType;
import com.bitdubai.fermat_cbp_api.layer.identity.crypto_customer.interfaces.CryptoCustomerIdentity;
import com.bitdubai.fermat_cbp_api.layer.wallet_module.crypto_customer.interfaces.CryptoCustomerWalletManager;
import com.bitdubai.fermat_cbp_api.layer.wallet_module.crypto_customer.interfaces.CryptoCustomerWalletModuleManager;
import com.bitdubai.fermat_cbp_api.layer.wallet_module.crypto_customer.interfaces.settings.CryptoCustomerWalletAssociatedSetting;
import com.bitdubai.fermat_cbp_api.layer.wallet_module.crypto_customer.interfaces.settings.CryptoCustomerWalletPreferenceSettings;
import com.bitdubai.fermat_cbp_api.layer.wallet_module.crypto_customer.interfaces.settings.CryptoCustomerWalletProviderSetting;
import com.bitdubai.fermat_cer_api.layer.provider.exceptions.CantGetProviderInfoException;
import com.bitdubai.fermat_cer_api.layer.provider.interfaces.CurrencyExchangeRateProviderManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedWalletExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;
import com.bitdubai.fermat_wpd_api.layer.wpd_middleware.wallet_manager.exceptions.CantListWalletsException;
import com.bitdubai.fermat_wpd_api.layer.wpd_middleware.wallet_manager.interfaces.InstalledWallet;
import com.bitdubai.reference_wallet.crypto_customer_wallet.R;
import com.bitdubai.reference_wallet.crypto_customer_wallet.common.adapters.ProvidersAdapter;
import com.bitdubai.reference_wallet.crypto_customer_wallet.common.adapters.SingleDeletableItemAdapter;
import com.bitdubai.reference_wallet.crypto_customer_wallet.common.models.CurrencyPairAndProvider;
import com.bitdubai.reference_wallet.crypto_customer_wallet.fragments.common.SimpleListDialogFragment;
import com.bitdubai.reference_wallet.crypto_customer_wallet.session.CryptoCustomerWalletSession;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by guillermo on 16/02/16.
 */
public class SettingsProvidersFragment extends AbstractFermatFragment<CryptoCustomerWalletSession, ResourceProviderManager>
        implements SingleDeletableItemAdapter.OnDeleteButtonClickedListener<CurrencyExchangeRateProviderManager>, AdapterView.OnItemSelectedListener, DialogInterface.OnDismissListener {

    // Constants
    private static final String TAG = "settingsprovidersCCW";

    // Data
    private List<CurrencyPairAndProvider> selectedProviders;
    private List<Currency> currencies;
    private Currency currencyFrom;
    private Currency currencyTo;
    private List<InstalledWallet> bitcoinWallets;
    private InstalledWallet selectedBitcoinWallet;
    private CryptoCustomerIdentity selectedIdentity;

    // UI
    private RecyclerView recyclerView;
    private FermatTextView emptyView;

    // Fermat Managers
    private CryptoCustomerWalletManager walletManager;
    private CryptoCustomerWalletModuleManager moduleManager;
    private ErrorManager errorManager;
    private ProvidersAdapter adapter;


    public static SettingsProvidersFragment newInstance() {
        SettingsProvidersFragment fragment = new SettingsProvidersFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        selectedProviders = new ArrayList<>();
        currencies = getCurrenciesList();
        moduleManager = appSession.getModuleManager();

        try {
            walletManager = moduleManager.getCryptoCustomerWallet(appSession.getAppPublicKey());
            errorManager = appSession.getErrorManager();
            bitcoinWallets = getBitcoinWallets(walletManager);

            // Verify if wallet configured, if it is, show this fragment, else show the home fragment (the second start fragment)
            boolean walletConfigured;
            try {
                walletConfigured = walletManager.isWalletConfigured(appSession.getAppPublicKey());
            } catch (Exception ex) {
                Object data = appSession.getData(CryptoCustomerWalletSession.CONFIGURED_DATA);
                walletConfigured = (data != null);
            }

            if (walletConfigured) {
                //TODO: Nelson revisa esto para que funcione ya el metodo devuelve true o false si la wallet esta configurada
                //changeActivity(Activities.CBP_CRYPTO_CUSTOMER_WALLET_HOME, appSession.getAppPublicKey());
                getRuntimeManager().changeStartActivity(1);
            }

            //Obtain walletSettings or create new wallet settings if first time opening wallet
            CryptoCustomerWalletPreferenceSettings walletSettings;
            try {
                walletSettings = moduleManager.getSettingsManager().loadAndGetSettings(appSession.getAppPublicKey());
            } catch (Exception e) {
                walletSettings = null;
            }

            if (walletSettings == null) {
                walletSettings = new CryptoCustomerWalletPreferenceSettings();
                walletSettings.setIsPresentationHelpEnabled(true);
                moduleManager.getSettingsManager().persistSettings(appSession.getAppPublicKey(), walletSettings);
            }
            if (selectedProviders.size()==0){
                /*Map<String, CurrencyExchangeRateProviderManager> providersMap = walletManager.getProviderReferencesFromCurrencyPair(currencyFrom, currencyTo);
                if (providersMap != null) {
                    Collection<CurrencyExchangeRateProviderManager> providerManagers = providersMap.values();
                    for (CurrencyExchangeRateProviderManager providerManager : providerManagers)
                        providers.add(new CurrencyPairAndProvider(currencyFrom, currencyTo, providerManager));
                }*/
                List<CryptoCustomerWalletProviderSetting> list  = walletManager.getAssociatedProviders(appSession.getAppPublicKey());
                for (CryptoCustomerWalletProviderSetting aux: list){
                    Map<String, CurrencyExchangeRateProviderManager> providersMap = walletManager.getProviderReferencesFromCurrencyPair(aux.getCurrencyFrom(), aux.getCurrencyTo());
                    if (providersMap != null) {
                        Collection<CurrencyExchangeRateProviderManager> providerManagers = providersMap.values();
                        for (CurrencyExchangeRateProviderManager providerManager: providerManagers){
                            CurrencyPairAndProvider provider = new CurrencyPairAndProvider(aux.getCurrencyFrom(),aux.getCurrencyTo(),providerManager);
                            selectedProviders.add(provider);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            if (errorManager != null)
                errorManager.reportUnexpectedWalletException(Wallets.CBP_CRYPTO_CUSTOMER_WALLET,
                        UnexpectedWalletExceptionSeverity.DISABLES_THIS_FRAGMENT, ex);
            else
                Log.e(TAG, ex.getMessage(), ex);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View layout=inflater.inflate(R.layout.ccw_settings_providers,container,false);
        configureToolbar();
        recyclerView = (RecyclerView) layout.findViewById(R.id.ccw_selected_providers_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        adapter = new ProvidersAdapter(getActivity(), selectedProviders);
        adapter.setDeleteButtonListener(this);
        recyclerView.setAdapter(adapter);

        emptyView = (FermatTextView) layout.findViewById(R.id.ccw_selected_providers_empty_view);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.ccw_spinner_item, getFormattedCurrencies(currencies));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner currencyFromSpinner = (Spinner) layout.findViewById(R.id.currency_from_spinner);
        currencyFromSpinner.setOnItemSelectedListener(this);
        currencyFromSpinner.setAdapter(adapter);

        final Spinner currencyToSpinner = (Spinner) layout.findViewById(R.id.currency_to_spinner);
        currencyToSpinner.setOnItemSelectedListener(this);
        currencyToSpinner.setAdapter(adapter);

        adapter = new ArrayAdapter<>(getActivity(), R.layout.ccw_spinner_item, getFormattedBitcoinWallets(bitcoinWallets));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner bitCoinWalletsSpinner = (Spinner) layout.findViewById(R.id.bitcoin_wallets_spinner);
        bitCoinWalletsSpinner.setOnItemSelectedListener(this);
        bitCoinWalletsSpinner.setAdapter(adapter);

        final View selectProvidersButton = layout.findViewById(R.id.ccw_select_providers_button);
        selectProvidersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProvidersDialog();
            }
        });

        final View nextStepButton = layout.findViewById(R.id.ccw_next_step_button);
        nextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSettingAndGoNextStep();
            }
        });

        //showHelpDialog();
        if(selectedProviders.size()!=0)
            this.adapter.changeDataSet(selectedProviders);
        return layout;
    }

    private void configureToolbar() {
        Toolbar toolbar = getToolbar();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            toolbar.setBackground(getResources().getDrawable(R.drawable.ccw_action_bar_gradient_colors, null));
        else
            toolbar.setBackground(getResources().getDrawable(R.drawable.ccw_action_bar_gradient_colors));

        toolbar.setTitleTextColor(Color.WHITE);
    }


    private void showHelpDialog() {

        try {
            final boolean haveAssociatedIdentity = walletManager.haveAssociatedIdentity(appSession.getAppPublicKey());
            if (haveAssociatedIdentity)
                return;

            PresentationDialog presentationDialog;

            if (walletManager.getListOfIdentities().isEmpty()) {
                presentationDialog = new PresentationDialog.Builder(getActivity(), appSession)
                        .setTemplateType(PresentationDialog.TemplateType.TYPE_PRESENTATION)
                        .setBannerRes(R.drawable.cbp_banner_crypto_customer_wallet)
                        .setIconRes(R.drawable.cbp_crypto_customer)
                        .setBody(R.string.cbw_wizard_providers_dialog_body)
                        .setSubTitle(R.string.cbw_wizard_providers_dialog_sub_title)
                        .setTextFooter(R.string.cbw_wizard_providers_dialog_footer)
                        .build();
            } else {
                presentationDialog = new PresentationDialog.Builder(getActivity(), appSession)
                        .setBannerRes(R.drawable.cbp_banner_crypto_customer_wallet)
                        .setIconRes(R.drawable.cbp_crypto_customer)
                        .setBody(R.string.cbw_wizard_providers_dialog_body)
                        .setSubTitle(R.string.cbw_wizard_providers_dialog_sub_title)
                        .setTextFooter(R.string.cbw_wizard_providers_dialog_footer)
                        .setTemplateType(PresentationDialog.TemplateType.TYPE_PRESENTATION_WITHOUT_IDENTITIES)
                        .build();
            }

            presentationDialog.setOnDismissListener(this);

            final SettingsManager<CryptoCustomerWalletPreferenceSettings> settingsManager = moduleManager.getSettingsManager();
            final CryptoCustomerWalletPreferenceSettings preferenceSettings = settingsManager.loadAndGetSettings(appSession.getAppPublicKey());

            final boolean showDialog = preferenceSettings.isHomeTutorialDialogEnabled();
            if (showDialog)
                presentationDialog.show();

        } catch (FermatException ex) {
            Log.e(TAG, ex.getMessage(), ex);
            if (errorManager != null)
                errorManager.reportUnexpectedWalletException(Wallets.CBP_CRYPTO_BROKER_WALLET,
                        UnexpectedWalletExceptionSeverity.DISABLES_THIS_FRAGMENT, ex);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {

        try {
            List<CryptoCustomerIdentity> listOfIdentities = walletManager.getListOfIdentities();
            if (listOfIdentities.isEmpty())
                getActivity().onBackPressed();
            else {
                invalidate();
                selectedIdentity = listOfIdentities.get(0);
            }

        } catch (FermatException e) {
            Log.e(TAG, e.getMessage(), e);
            if (errorManager != null)
                errorManager.reportUnexpectedWalletException(Wallets.CBP_CRYPTO_CUSTOMER_WALLET,
                        UnexpectedWalletExceptionSeverity.DISABLES_THIS_FRAGMENT, e);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.currency_from_spinner) {
            currencyFrom = currencies.get(position);

        } else if (parent.getId() == R.id.currency_to_spinner) {
            currencyTo = currencies.get(position);

        } else if (parent.getId() == R.id.bitcoin_wallets_spinner) {
            selectedBitcoinWallet = bitcoinWallets.get(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void showProvidersDialog() {

        try {
            List<CurrencyPairAndProvider> providers = new ArrayList<>();

            Map<String, CurrencyExchangeRateProviderManager> providersMap = walletManager.getProviderReferencesFromCurrencyPair(currencyFrom, currencyTo);
            if (providersMap != null) {
                Collection<CurrencyExchangeRateProviderManager> providerManagers = providersMap.values();
                for (CurrencyExchangeRateProviderManager providerManager : providerManagers)
                    providers.add(new CurrencyPairAndProvider(currencyFrom, currencyTo, providerManager));
            }


            final SimpleListDialogFragment<CurrencyPairAndProvider> dialogFragment = new SimpleListDialogFragment<>();
            dialogFragment.configure("Select a Provider", providers);
            dialogFragment.setListener(new SimpleListDialogFragment.ItemSelectedListener<CurrencyPairAndProvider>() {
                @Override
                public void onItemSelected(CurrencyPairAndProvider selectedItem) {
                    if (!containProvider(selectedItem)) {
                        selectedProviders.add(selectedItem);
                        adapter.changeDataSet(selectedProviders);
                        Log.i("DATA PROVIDERSS:", "" + selectedProviders + " Item seleccionado: " + selectedItem);
                        showOrHideNoProvidersView();
                    }
                }
            });

            dialogFragment.show(getFragmentManager(), "ProvidersDialog");

        } catch (FermatException ex) {
            Log.e(TAG, ex.getMessage(), ex);
            if (errorManager != null)
                errorManager.reportUnexpectedWalletException(Wallets.CBP_CRYPTO_CUSTOMER_WALLET,
                        UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, ex);
        }
    }

    private void saveSettingAndGoNextStep() {

        if (selectedProviders.isEmpty()) {
            Toast.makeText(getActivity(), R.string.ccw_select_providers_warning_msg, Toast.LENGTH_SHORT).show();
            // TODO descomentar cuando pueda agregar un proveedor en la lista
            // return;
        }

        try {
            walletManager.associateIdentity(selectedIdentity, appSession.getAppPublicKey());

            final CryptoCustomerWalletAssociatedSetting associatedWallet = walletManager.newEmptyCryptoBrokerWalletAssociatedSetting();
            associatedWallet.setId(UUID.randomUUID());
            associatedWallet.setMoneyType(MoneyType.CRYPTO);
            associatedWallet.setMerchandise(selectedBitcoinWallet.getCryptoCurrency());
            associatedWallet.setPlatform(selectedBitcoinWallet.getPlatform());
            associatedWallet.setWalletPublicKey(selectedBitcoinWallet.getWalletPublicKey());
            associatedWallet.setCustomerPublicKey(appSession.getAppPublicKey());

            walletManager.saveWalletSettingAssociated(associatedWallet, appSession.getAppPublicKey());

            for (CurrencyPairAndProvider provider : selectedProviders) {
                CurrencyExchangeRateProviderManager providerManager = provider.getProvider();

                CryptoCustomerWalletProviderSetting setting = walletManager.newEmptyCryptoCustomerWalletProviderSetting();
                setting.setCustomerPublicKey(appSession.getAppPublicKey());
                setting.setDescription(providerManager.getProviderName());
                setting.setId(providerManager.getProviderId());
                setting.setPlugin(providerManager.getProviderId());
                setting.setCurrencyFrom(provider.getCurrencyFrom());
                setting.setCurrencyTo(provider.getCurrencyTo());

                walletManager.saveCryptoCustomerWalletProviderSetting(setting, appSession.getAppPublicKey());
            }

        } catch (FermatException ex) {
            Log.e(TAG, ex.getMessage(), ex);
            if (errorManager != null)
                errorManager.reportUnexpectedWalletException(Wallets.CBP_CRYPTO_CUSTOMER_WALLET,
                        UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, ex);
        }  catch (Exception ex){
            Log.e(TAG, ex.getMessage(), ex);
            if (errorManager != null)
                errorManager.reportUnexpectedWalletException(Wallets.CBP_CRYPTO_CUSTOMER_WALLET,
                        UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, ex);
        }

        changeActivity(Activities.CBP_CRYPTO_CUSTOMER_WALLET_SETTINGS, appSession.getAppPublicKey());
    }

    @Override
    public void deleteButtonClicked(CurrencyExchangeRateProviderManager data, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.ccw_delete_wallet_dialog_title).setMessage(R.string.ccw_delete_wallet_dialog_msg);
        builder.setPositiveButton(R.string.ccw_delete_caps, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectedProviders.remove(position);
                adapter.changeDataSet(selectedProviders);
                showOrHideNoProvidersView();
            }
        });
        builder.setNegativeButton(R.string.ccw_cancel_caps, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        builder.show();
    }

    private void showOrHideNoProvidersView() {
        if (selectedProviders.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private boolean containProvider(CurrencyPairAndProvider selectedProvider) {
        if (selectedProviders.isEmpty())
            return false;

        try {
            for (CurrencyPairAndProvider provider : selectedProviders) {
                CurrencyExchangeRateProviderManager providerManager = provider.getProvider();
                UUID providerId = providerManager.getProviderId();
                CurrencyExchangeRateProviderManager selectedProviderManager = selectedProvider.getProvider();
                UUID selectedProviderId = selectedProviderManager.getProviderId();

                Currency providerFrom = provider.getCurrencyFrom();
                Currency providerTo = provider.getCurrencyTo();

                Currency SelectedFrom = selectedProvider.getCurrencyFrom();
                Currency SelectedTo = selectedProvider.getCurrencyTo();

                if (providerId.equals(selectedProviderId) && providerFrom == SelectedFrom && providerTo == SelectedTo)
                    return true;
            }
        } catch (CantGetProviderInfoException ex) {
            Log.e(TAG, ex.getMessage(), ex);
            if (errorManager != null)
                errorManager.reportUnexpectedWalletException(Wallets.CBP_CRYPTO_CUSTOMER_WALLET,
                        UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, ex);
        }

        return false;
    }

    private List<Currency> getCurrenciesList() {
        ArrayList<Currency> data = new ArrayList<>();

        FiatCurrency[] fiatCurrencies = FiatCurrency.values();
        Collections.addAll(data, fiatCurrencies);

        CryptoCurrency[] cryptoCurrencies = CryptoCurrency.values();
        Collections.addAll(data, cryptoCurrencies);

        return data;
    }

    private List<String> getFormattedCurrencies(List<Currency> currencies) {
        ArrayList<String> data = new ArrayList<>();
        for (Currency currency : currencies) {
            data.add(currency.getFriendlyName() + " (" + currency.getCode() + ")");
        }

        return data;
    }

    private List<InstalledWallet> getBitcoinWallets(CryptoCustomerWalletManager walletManager) {
        ArrayList<InstalledWallet> data = new ArrayList<>();

        try {
            List<InstalledWallet> installedWallets = walletManager.getInstallWallets();

            if (installedWallets != null)
                for (InstalledWallet wallet : installedWallets)
                    if (wallet.getPlatform().equals(Platforms.CRYPTO_CURRENCY_PLATFORM))
                        if (wallet.getCryptoCurrency().equals(CryptoCurrency.BITCOIN))
                            data.add(wallet);

        } catch (CantListWalletsException ex) {
            Log.e(TAG, ex.getMessage(), ex);
            if (errorManager != null)
                errorManager.reportUnexpectedWalletException(Wallets.CBP_CRYPTO_CUSTOMER_WALLET,
                        UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, ex);
        }

        return data;
    }

    private List<String> getFormattedBitcoinWallets(List<InstalledWallet> bitcoinWallets) {
        ArrayList<String> data = new ArrayList<>();
        for (InstalledWallet wallet : bitcoinWallets) {
            data.add(wallet.getWalletName());
        }
        return data;
    }
}
