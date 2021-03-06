package com.bitdubai.fermat_wrd_plugin.layer.api.tokenly.developer.bitdubai.version_1.records;

import com.bitdubai.fermat_wrd_api.all_definitions.enums.TokenlyBalancesType;
import com.bitdubai.fermat_wrd_api.all_definitions.enums.TokenlyCurrency;
import com.bitdubai.fermat_wrd_api.layer.api.tokenly.interfaces.TokenlyBalance;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 07/03/16.
 */
public class TokenlyBalanceRecord implements TokenlyBalance {

    private TokenlyBalancesType type;
    private long balance;
    private TokenlyCurrency currencyType;

    /**
     * Constructor with balance type set as CONFIRMED.
     * @param balance
     * @param currencyType
     */
    public TokenlyBalanceRecord(
            long balance,
            TokenlyCurrency currencyType) {
        this.balance = balance;
        this.currencyType = currencyType;
        this.type = TokenlyBalancesType.CONFIRMED;
    }

    /**
     * Constructor with all parameters sets.
     * @param type
     * @param balance
     * @param currencyType
     */
    public TokenlyBalanceRecord(
            TokenlyBalancesType type,
            long balance,
            TokenlyCurrency currencyType) {
        this.type = type;
        this.balance = balance;
        this.currencyType = currencyType;
    }

    /**
     * This method returns the balance type.
     * @return
     */
    @Override
    public TokenlyBalancesType getType() {
        return this.type;
    }

    /**
     * This method returns the balance.
     * @return
     */
    @Override
    public long getBalance() {
        return 0;
    }

    /**
     * This method returns the currency type.
     * @return
     */
    @Override
    public TokenlyCurrency getCurrencyType() {
        return this.currencyType;
    }

    @Override
    public String toString() {
        return "TokenlyBalanceRecord{" +
                "type=" + type +
                ", balance=" + balance +
                ", currencyType=" + currencyType +
                '}';
    }
}
