package CashMoneyDestockTransactionImpl;

import com.bitdubai.fermat_cbp_plugin.layer.stock_transactions.cash_money_destock.developer.bitdubai.version_1.structure.CashMoneyDestockTransactionImpl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;

/**
 * Created by Jose Vilchez on 18/01/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class SetAmountTest {

    @Test
    public void setAmount(){
        CashMoneyDestockTransactionImpl cashMoneyDestockTransaction = mock(CashMoneyDestockTransactionImpl.class, Mockito.RETURNS_DEEP_STUBS);
        doCallRealMethod().when(cashMoneyDestockTransaction).setAmount(Mockito.any(BigDecimal.class));
    }

}
