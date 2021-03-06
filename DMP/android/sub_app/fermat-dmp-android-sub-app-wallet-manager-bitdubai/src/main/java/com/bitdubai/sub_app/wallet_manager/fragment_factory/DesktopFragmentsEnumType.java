package com.bitdubai.sub_app.wallet_manager.fragment_factory;

import com.bitdubai.fermat_android_api.layer.definition.wallet.enums.FermatFragmentsEnumType;

/**
 * Created by Matias Furszyfer on 2015.07.22..
 */

public enum DesktopFragmentsEnumType implements FermatFragmentsEnumType<DesktopFragmentsEnumType> {

    DESKTOP_MAIN("DAM"),
    SETTINGS("DS")
    ;

    private String key;

    DesktopFragmentsEnumType(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return this.key;
    }


    @Override
    public String toString() {
        return key;
    }

    public static DesktopFragmentsEnumType getValue(String name) {
        for (DesktopFragmentsEnumType fragments : DesktopFragmentsEnumType.values()) {
            if (fragments.key.equals(name)) {
                return fragments;
            }
        }
        return null;
    }
}
