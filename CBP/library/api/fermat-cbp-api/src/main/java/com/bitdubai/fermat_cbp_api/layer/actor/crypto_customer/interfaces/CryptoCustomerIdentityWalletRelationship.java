package com.bitdubai.fermat_cbp_api.layer.actor.crypto_customer.interfaces;

import com.bitdubai.fermat_cbp_api.layer.actor.crypto_customer.exceptions.CantDeleteCustomerIdentiyWalletRelationshipException;
import com.bitdubai.fermat_cbp_api.layer.actor.crypto_customer.exceptions.CantGetCustomerIdentiyWalletRelationshipException;

import java.util.Collection;
import java.util.UUID;

/**
 * Created by Yordin Alayn on 12.11.15.
 */
public interface CryptoCustomerIdentityWalletRelationship {

    CryptoCustomerIdentityWalletRelationshipRecord createCustomerIdentityWalletRelationship(String walletPublicKey, String identityPublicKey) throws com.bitdubai.fermat_cbp_api.layer.actor.crypto_customer.exceptions.CantCreateCustomerIdentiyWalletRelationshipException;

    CryptoCustomerIdentityWalletRelationshipRecord updateCustomerIdentityWalletRelationship(UUID relationshipId, String walletPublicKey, String identityPublicKey) throws com.bitdubai.fermat_cbp_api.layer.actor.crypto_customer.exceptions.CantUpdateCustomerIdentiyWalletRelationshipException;

    Collection<CryptoCustomerIdentityWalletRelationshipRecord> getAllCustomerIdentityWalletRelationships() throws CantGetCustomerIdentiyWalletRelationshipException;

    CryptoCustomerIdentityWalletRelationshipRecord getCustomerIdentityWalletRelationships(UUID relationshipId) throws CantGetCustomerIdentiyWalletRelationshipException;

    CryptoCustomerIdentityWalletRelationshipRecord getCustomerIdentityWalletRelationshipsByIdentity(String identityPublicKey) throws CantGetCustomerIdentiyWalletRelationshipException;

    CryptoCustomerIdentityWalletRelationshipRecord getCustomerIdentityWalletRelationshipsByWallet(String walletPublicKey) throws CantGetCustomerIdentiyWalletRelationshipException;
}