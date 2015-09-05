package com.bitdubai.fermat_dmp_plugin.layer.middleware.wallet_contacts.developer.bitdubai.version_1.database;

/**
 * The Class <code>com.bitdubai.fermat_dmp_plugin.layer.middleware.wallet_contacts.developer.bitdubai.version_1.database.WalletContactsMiddlewareDatabaseConstants</code>
 * keeps constants the column names of the database.<p/>
 * <p/>
 *
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 04/09/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class WalletContactsMiddlewareDatabaseConstants {

    /**
     * Wallet Contacts database table definition.
     */
    static final String WALLET_CONTACTS_TABLE_NAME = "wallet_contacts";

    static final String WALLET_CONTACTS_ACTOR_PUBLIC_KEY_COLUMN_NAME = "actor_public_key";
    static final String WALLET_CONTACTS_ACTOR_TYPE_COLUMN_NAME = "actor_type";
    static final String WALLET_CONTACTS_ACTOR_ALIAS_COLUMN_NAME = "actor_alias";
    static final String WALLET_CONTACTS_ACTOR_FIRST_NAME_COLUMN_NAME = "actor_first_name";
    static final String WALLET_CONTACTS_ACTOR_LAST_NAME_COLUMN_NAME = "actor_last_name";
    static final String WALLET_CONTACTS_WALLET_PUBLIC_KEY_COLUMN_NAME = "wallet_public_key";

    static final String WALLET_CONTACTS_FIRST_KEY_COLUMN = "contact_id";

    /**
     * Wallet Contact Addresses database table definition.
     */
    static final String WALLET_CONTACT_ADDRESSES_TABLE_NAME = "wallet_contact_addresses";

    static final String WALLET_CONTACT_ADDRESSES_ACTOR_PUBLIC_KEY_COLUMN_NAME = "actor_public_key";
    static final String WALLET_CONTACT_ADDRESSES_CRYPTO_ADDRESS_COLUMN_NAME = "crypto_address";
    static final String WALLET_CONTACT_ADDRESSES_CRYPTO_CURRENCY_COLUMN_NAME = "crypto_currency";

    static final String WALLET_CONTACT_ADDRESSES_FIRST_KEY_COLUMN = "crypto_address";

}