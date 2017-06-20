package com.dji.MyDjiGo.utils;

import com.dji.MyDjiGo.MyDjiGo;

import dji.sdk.products.Aircraft;
import dji.sdk.products.HandHeld;

/**
 * Created by dji on 16/1/6.
 */
public class ModuleVerificationUtil {
    public static boolean isProductModuleAvailable() {
        return (null != MyDjiGo.getProductInstance());
    }

    public static boolean isAircraft() {
        return MyDjiGo.getProductInstance() instanceof Aircraft;
    }

    public static boolean isHandHeld() {
        return MyDjiGo.getProductInstance() instanceof HandHeld;
    }

    public static boolean isCameraModuleAvailable() {
        return isProductModuleAvailable() && (null != MyDjiGo.getProductInstance().getCamera());
    }

    public static boolean isPlaybackAvailable() {
        return isCameraModuleAvailable() && (null != MyDjiGo.getProductInstance()
                                                                         .getCamera()
                                                                         .getPlaybackManager());
    }

    public static boolean isMediaManagerAvailable() {
        return isCameraModuleAvailable() && (null != MyDjiGo.getProductInstance()
                                                                         .getCamera()
                                                                         .getMediaManager());
    }

    public static boolean isRemoteControllerAvailable() {
        return isProductModuleAvailable() && isAircraft() && (null != MyDjiGo.getAircraftInstance()
                                                                                          .getRemoteController());
    }

    public static boolean isFlightControllerAvailable() {
        return isProductModuleAvailable() && isAircraft() && (null != MyDjiGo.getAircraftInstance()
                                                                                          .getFlightController());
    }

    public static boolean isCompassAvailable() {
        return isFlightControllerAvailable() && isAircraft() && (null != MyDjiGo.getAircraftInstance()
                                                                                             .getFlightController()
                                                                                             .getCompass());
    }

    public static boolean isFlightLimitationAvailable() {
        return isFlightControllerAvailable() && isAircraft();
    }

    public static boolean isGimbalModuleAvailable() {
        return isProductModuleAvailable() && (null != MyDjiGo.getProductInstance().getGimbal());
    }

    public static boolean isAirlinkAvailable() {
        return isProductModuleAvailable() && (null != MyDjiGo.getProductInstance().getAirLink());
    }

    public static boolean isWiFiLinkAvailable() {
        return isAirlinkAvailable() && (null != MyDjiGo.getProductInstance().getAirLink().getWiFiLink());
    }

    public static boolean isLightbridgeLinkAvailable() {
        return isAirlinkAvailable() && (null != MyDjiGo.getProductInstance()
                                                                    .getAirLink()
                                                                    .getLightbridgeLink());
    }
}
