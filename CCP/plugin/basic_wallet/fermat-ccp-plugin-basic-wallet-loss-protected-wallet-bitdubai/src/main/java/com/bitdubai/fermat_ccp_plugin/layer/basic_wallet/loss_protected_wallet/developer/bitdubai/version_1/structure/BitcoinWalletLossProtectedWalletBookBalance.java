package com.bitdubai.fermat_ccp_plugin.layer.basic_wallet.loss_protected_wallet.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.FermatException;

import com.bitdubai.fermat_api.layer.osa_android.broadcaster.Broadcaster;
import com.bitdubai.fermat_api.layer.osa_android.broadcaster.BroadcasterType;

import com.bitdubai.fermat_api.layer.all_definition.enums.BlockchainNetworkType;


import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.common.enums.BalanceType;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.common.exceptions.CantCalculateBalanceException;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.common.exceptions.CantRegisterCreditException;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.common.exceptions.CantRegisterDebitException;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.loss_protected_wallet.interfaces.BitcoinLossProtectedWalletBalance;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.loss_protected_wallet.interfaces.BitcoinLossProtectedWalletTransactionRecord;


/**
 * Created by ciencias on 7/6/15.
 *
 */
public class BitcoinWalletLossProtectedWalletBookBalance implements BitcoinLossProtectedWalletBalance {

    /**
     * BitcoinWalletBasicWallet member variables.
     */
    private Database database;

    private BitcoinWalletLossProtectedWalletDao bitcoinWalletBasicWalletDao;

    private Broadcaster broadcaster;

    /**
     * Constructor.
     */
    public BitcoinWalletLossProtectedWalletBookBalance(final Database database,final Broadcaster broadcaster){
        this.database = database;
        this.broadcaster = broadcaster;
    }

    @Override
    public long getBalance() throws CantCalculateBalanceException {
        try {
            bitcoinWalletBasicWalletDao = new BitcoinWalletLossProtectedWalletDao(this.database);
            return bitcoinWalletBasicWalletDao.getBookBalance();
        } catch(CantCalculateBalanceException exception){
            database.closeDatabase();
            throw exception;
        } catch(Exception exception){
            database.closeDatabase();
            throw new CantCalculateBalanceException(CantCalculateBalanceException.DEFAULT_MESSAGE, FermatException.wrapException(exception), null, null);
        }
    }

    @Override
    public long getBalance(BlockchainNetworkType blockchainNetworkType) throws CantCalculateBalanceException {
        try {
            bitcoinWalletBasicWalletDao = new BitcoinWalletLossProtectedWalletDao(this.database);
            return bitcoinWalletBasicWalletDao.getBookBalance(blockchainNetworkType);
        } catch(CantCalculateBalanceException exception){
            database.closeDatabase();
            throw exception;
        } catch(Exception exception){
            database.closeDatabase();
            throw new CantCalculateBalanceException(CantCalculateBalanceException.DEFAULT_MESSAGE, FermatException.wrapException(exception), null, null);
        }
    }

    /*
        * NOTA:
        *  El debit y el credit debería mirar primero si la tramsacción que
        *  se quiere aplicar existe. Si no existe aplica los cambios normalmente, pero si existe
        *  debería ignorar la transacción.
        */
    @Override
    public void debit(BitcoinLossProtectedWalletTransactionRecord cryptoTransaction) throws CantRegisterDebitException {
        try {
            bitcoinWalletBasicWalletDao = new BitcoinWalletLossProtectedWalletDao(this.database);
            bitcoinWalletBasicWalletDao.addDebit(cryptoTransaction, BalanceType.BOOK);
            //broadcaster balance amount
            broadcaster.publish(BroadcasterType.UPDATE_VIEW, cryptoTransaction.getTransactionHash());
        } catch(CantRegisterDebitException exception){
            throw exception;
        } catch(Exception exception){
            throw new CantRegisterDebitException(CantRegisterDebitException.DEFAULT_MESSAGE, FermatException.wrapException(exception), null, null);
        }
    }

    @Override
    public void credit(BitcoinLossProtectedWalletTransactionRecord cryptoTransaction) throws CantRegisterCreditException {
        try {
            bitcoinWalletBasicWalletDao = new BitcoinWalletLossProtectedWalletDao(this.database);
            bitcoinWalletBasicWalletDao.addCredit(cryptoTransaction, BalanceType.BOOK);
            //broadcaster balance amount
            broadcaster.publish(BroadcasterType.UPDATE_VIEW, cryptoTransaction.getTransactionHash());
        } catch(CantRegisterCreditException exception){
            throw exception;
        } catch(Exception exception){
            throw new CantRegisterCreditException(CantRegisterCreditException.DEFAULT_MESSAGE, FermatException.wrapException(exception), null, null);
        }
    }

    @Override
    public void revertCredit(BitcoinLossProtectedWalletTransactionRecord cryptoTransaction) throws CantRegisterCreditException {

        bitcoinWalletBasicWalletDao = new BitcoinWalletLossProtectedWalletDao(this.database);
        try {
            bitcoinWalletBasicWalletDao.revertCredit(cryptoTransaction,BalanceType.BOOK);
        } catch (CantRegisterDebitException e) {
            throw new CantRegisterCreditException("CANT REVERT CREDIT EN BOOK", FermatException.wrapException(e), null, null);

        }


    }
}
