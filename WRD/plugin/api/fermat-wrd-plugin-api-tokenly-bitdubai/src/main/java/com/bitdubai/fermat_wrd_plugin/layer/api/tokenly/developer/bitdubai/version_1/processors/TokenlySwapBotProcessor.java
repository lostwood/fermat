package com.bitdubai.fermat_wrd_plugin.layer.api.tokenly.developer.bitdubai.version_1.processors;

import com.bitdubai.fermat_wrd_api.all_definitions.interfaces.RemoteJSonProcessor;
import com.bitdubai.fermat_wrd_api.layer.api.tokenly.exceptions.CantGetBotException;
import com.bitdubai.fermat_wrd_api.layer.api.tokenly.exceptions.CantGetJSonObjectException;
import com.bitdubai.fermat_wrd_api.layer.api.tokenly.interfaces.Bot;
import com.bitdubai.fermat_wrd_api.layer.api.tokenly.interfaces.ImageDetails;
import com.bitdubai.fermat_wrd_api.layer.api.tokenly.interfaces.Swap;
import com.bitdubai.fermat_wrd_api.layer.api.tokenly.interfaces.TokenlyBalance;
import com.bitdubai.fermat_wrd_plugin.layer.api.tokenly.developer.bitdubai.version_1.config.TokenlyBotJSonAttNames;
import com.bitdubai.fermat_wrd_plugin.layer.api.tokenly.developer.bitdubai.version_1.config.TokenlyConfiguration;
import com.bitdubai.fermat_wrd_plugin.layer.api.tokenly.developer.bitdubai.version_1.config.TokenlySwapJSonAttNames;
import com.bitdubai.fermat_wrd_plugin.layer.api.tokenly.developer.bitdubai.version_1.records.SwapBotRecord;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.sql.Date;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 03/03/16.
 */
public class TokenlySwapBotProcessor extends AbstractTokenlyProcessor {

    private static String swabotTokenlyURL= TokenlyConfiguration.URL_TOKENLY_SWAPBOT_API;

    /**
     * This method returns a bot from tokenly API by a request URL.
     * @param botId
     * @return
     * @throws CantGetBotException
     */
    public static Bot getBotURLByBotId(String botId) throws CantGetBotException {
        //Request URL to get a bot by tokenly Id.
        String requestedURL=swabotTokenlyURL+"bot/"+botId;
        try{
            JsonObject jSonObject = RemoteJSonProcessor.getJSonObject(requestedURL);
            return getBotFromJsonObject(jSonObject);
        } catch (CantGetJSonObjectException e) {
            throw new CantGetBotException(
                    e,
                    "Getting swap bot from given Id",
                    "Cannot get JSon from tokenly API using URL "+requestedURL);
        }

    }

    /**
     * This method returns a bot from a JsonObject.
     * @param jSonObject
     * @return
     */
    private static Bot getBotFromJsonObject(JsonObject jSonObject){

        Gson gSonProcessor = new Gson();
        //Bot Id
        String id = getStringFromJsonObject(jSonObject, TokenlyBotJSonAttNames.ID);
        //Bot name
        String name = getStringFromJsonObject(jSonObject, TokenlyBotJSonAttNames.NAME);
        //Bot address
        String address = getStringFromJsonObject(jSonObject, TokenlyBotJSonAttNames.ADDRESS);
        //Bot username
        String username = getStringFromJsonObject(jSonObject, TokenlyBotJSonAttNames.USERNAME);
        //Bot description
        String description = getStringFromJsonObject(jSonObject, TokenlyBotJSonAttNames.DESCRIPTION);
        //Bot description HTML
        String descriptionHtml = getStringFromJsonObject(jSonObject, TokenlyBotJSonAttNames.DESCRIPTION_HTML);
        //Bot Background image details
        ImageDetails backgroundImageDetails =
                TokenlyImageDetailsProcessor.getImageDetailsFromJsonObject(
                        jSonObject.getAsJsonObject(TokenlyBotJSonAttNames.BACKGROUND_DETAILS));
        //Bot logo image details
        ImageDetails logoImageDetails =
                TokenlyImageDetailsProcessor.getImageDetailsFromJsonObject(
                        jSonObject.getAsJsonObject(TokenlyBotJSonAttNames.LOGO_DETAILS));
        //Bot background overlay setting
        String[] backgroudOverlaySettings = getArrayStringFromJsonObject(
                jSonObject,
                TokenlyBotJSonAttNames.BACKGROUND_OVERLAY_SETTINGS);
        //Bot swaps
        Swap[] swaps = TokenlySwapProcessor.getSwapArrayFromJsonObject(jSonObject);
        //Bot balances
        TokenlyBalance[] tokenlyBalances = TokenlyBalanceProcessor.
                getTokenlyBalancesByJsonObject(
                        jSonObject.getAsJsonObject(TokenlyBotJSonAttNames.BALANCES));
        //Bot all balances by type
        TokenlyBalance[][] allTokenlyBalancesByType = TokenlyBalanceProcessor.
                getTokenlyBalancesByType(
                        jSonObject.getAsJsonObject(TokenlyBotJSonAttNames.ALL_BALANCES_BY_TYPE));
        //Bot return fee.
        float returnFee = (float) getDoubleFromJsonObject(jSonObject, TokenlyBotJSonAttNames.RETURN_FEE);
        //Bot state.
        String state = getStringFromJsonObject(jSonObject, TokenlyBotJSonAttNames.STATE);
        //Bot return fee.
        long confirmationsRequired =
                getLongFromJsonObject(jSonObject, TokenlyBotJSonAttNames.RETURN_FEE);
        //Bot refund after blocks.
        long refundsAfterBlocks = getLongFromJsonObject(jSonObject, TokenlyBotJSonAttNames.REFUND_AFTER_BLOCKS);
        //Bot created date
        Date createdAt = getDateFromJsonObject(jSonObject, TokenlySwapJSonAttNames.CREATED_AT);
        //Bot hash
        String hash = getStringFromJsonObject(jSonObject, TokenlyBotJSonAttNames.HASH);
        //Create bot
        Bot bot = new SwapBotRecord(
                id,
                name,
                address,
                username,
                description,
                descriptionHtml,
                backgroundImageDetails,
                logoImageDetails,
                backgroudOverlaySettings,
                swaps,
                tokenlyBalances,
                allTokenlyBalancesByType,
                returnFee,
                state,
                confirmationsRequired,
                refundsAfterBlocks,
                createdAt,
                hash);

        return bot;
    }



}
