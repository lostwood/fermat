package com.bitdubai.fermat_cbp_plugin.layer.network_service.transaction_transmission.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_p2p_api.layer.all_definition.common.network_services.abstract_classes.AbstractNetworkService;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.WsCommunicationsCloudClientManager;
import com.bitdubai.fermat_p2p_api.layer.all_definition.common.network_services.template.structure.CommunicationRegistrationProcessNetworkServiceAgent;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 23/11/15.
 */
public class TransactionTransmissionCommunicationRegistrationProcessNetworkServiceAgent extends CommunicationRegistrationProcessNetworkServiceAgent {

    public TransactionTransmissionCommunicationRegistrationProcessNetworkServiceAgent(
            AbstractNetworkService networkServicePluginRoot,
            WsCommunicationsCloudClientManager communicationsClientConnection) {
        super(
                networkServicePluginRoot,
                communicationsClientConnection);
    }

}
