package com.bitdubai.fermat_pip_addon.layer.platform_service.error_manager.developer.bitdubai.version_1;

import com.bitdubai.fermat_api.Addon;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.Service;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Wallets;
import com.bitdubai.fermat_api.layer.dmp_engine.sub_app_runtime.enums.SubApps;
import com.bitdubai.fermat_api.layer.all_definition.enums.Addons;
import com.bitdubai.fermat_api.layer.all_definition.enums.PlatformComponents;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;

//import com.bitdubai.fermat_pip_addon.layer.platform_service.error_manager.developer.bitdubai.version_1.functional.ErrorReport;
import com.bitdubai.fermat_pip_addon.layer.platform_service.error_manager.developer.bitdubai.version_1.functional.ErrorReport;

import java.io.Serializable;

/**
 * Created by ciencias on 05.02.15
 * Modified by Federico Rodriguez on 01.05.15
 * Updated by lnacosta (laion.cj91@gmail.com) on 16/10/2015.
 *      Deleted reference to Plugin Database System.
 */
public class ErrorManagerPlatformServiceAddonRootOld implements Addon, com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.ErrorManager, Service,Serializable {

    /**
     * Service Interface member variables.
     */
    private ServiceStatus serviceStatus = ServiceStatus.CREATED;

    /**
     * ErrorManager Interface implementation.
     */
    @Override
    public void reportUnexpectedPlatformException(PlatformComponents exceptionSource, com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.UnexpectedPlatformExceptionSeverity unexpectedPlatformExceptionSeverity, Exception exception) {
        processException(exceptionSource.name(), unexpectedPlatformExceptionSeverity.name(), exception);
    }

    @Override
    public void reportUnexpectedPluginException(Plugins exceptionSource, com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.UnexpectedPluginExceptionSeverity unexpectedPluginExceptionSeverity, Exception exception) {
        processException(exceptionSource.toString(), unexpectedPluginExceptionSeverity.toString(), exception);
    }

    @Override
    public void reportUnexpectedWalletException(Wallets exceptionSource, com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.UnexpectedWalletExceptionSeverity unexpectedWalletExceptionSeverity, Exception exception) {
        processException(exceptionSource.toString(), unexpectedWalletExceptionSeverity.toString(),exception);
    }

    @Override
    public void reportUnexpectedAddonsException(Addons exceptionSource, com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.UnexpectedAddonsExceptionSeverity unexpectedAddonsExceptionSeverity, Exception exception) {
        processException(exceptionSource.toString(), unexpectedAddonsExceptionSeverity.toString(), exception);
    }

    @Override
    public void reportUnexpectedSubAppException(SubApps exceptionSource, com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.UnexpectedSubAppExceptionSeverity unexpectedSubAppExceptionSeverity, Exception exception) {
        processException(exceptionSource.toString(), unexpectedSubAppExceptionSeverity.toString(), exception);
    }

    @Override
    public void reportUnexpectedUIException(UISource exceptionSource, com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.UnexpectedUIExceptionSeverity unexpectedAddonsExceptionSeverity, Exception exception) {
        processException(exceptionSource.toString(), unexpectedAddonsExceptionSeverity.toString(), exception);
    }

    @Override
    public void reportUnexpectedEventException(FermatEvent exceptionSource, Exception exception) {
        processException(exceptionSource.toString(), "Unknow", exception);
    }

    /**
     * Service Interface implementation.
     */

    @Override
    public void start() {
        this.serviceStatus = ServiceStatus.STARTED;
    }

    @Override
    public void pause() {
        this.serviceStatus = ServiceStatus.PAUSED;

    }

    @Override
    public void resume() {

        this.serviceStatus = ServiceStatus.STARTED;

    }

    @Override
    public void stop() {

        this.serviceStatus = ServiceStatus.STOPPED;
    }

    @Override
    public ServiceStatus getStatus() {
        return serviceStatus;
    }

    private void processException(final String source, final String severity, final Exception exception){
        printErrorReport(source, severity, FermatException.wrapException(exception));
    }

    private void printErrorReport(final String source, final String severity, final FermatException exception){
        System.err.println(new ErrorReport(source, severity, exception).generateReport());
    }

}
