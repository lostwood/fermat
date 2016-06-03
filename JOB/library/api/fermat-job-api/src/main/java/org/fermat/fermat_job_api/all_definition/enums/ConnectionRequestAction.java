package org.fermat.fermat_job_api.all_definition.enums;

import com.bitdubai.fermat_api.layer.all_definition.enums.interfaces.FermatEnum;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 03/06/16.
 */
public enum ConnectionRequestAction implements FermatEnum {

    /**
     * Please for doing the code more readable, keep the elements of the enum ordered.
     */
    ACCEPT             ("ACC"), // accept a connection request.
    CANCEL             ("CAN"), // cancel a connection request.
    DENY               ("DEN"), // deny a connection request.
    DISCONNECT         ("DIS"), // disconnect from a crypto broker.
    NONE               ("NON"), // no action needed.
    REQUEST            ("REQ"), // created the connection request.
    ;

    private final String code;

    ConnectionRequestAction(final String code) {
        this.code = code;
    }

    //PUBLIC METHODS

    public static ConnectionRequestAction getByCode(final String code) throws InvalidParameterException {

        for (ConnectionRequestAction value : values()) {
            if (value.getCode().equals(code)) return value;
        }
        throw new InvalidParameterException(
                InvalidParameterException.DEFAULT_MESSAGE,
                null, "Code Received: " + code,
                "This Code Is Not Valid for the ConnectionRequestAction enum.");

    }


    //GETTERS AND SETTERS

    @Override
    public final String getCode() {
        return this.code;
    }
}
