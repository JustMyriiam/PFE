package com.satoripop.insurance.domain.enumeration;

/**
 * The RegistrationType enumeration.
 */
public enum RegistrationType {
    STANDARD_PRIVATE_VEHICLE("TU"),
    SUSPENSIVE_REGIME("RS"),
    TEMPORARY_REGISTRATION("IT"),
    TRAILER("REM"),
    DIPLOMATIC_CORPS("CD"),
    DIPLOMATIC_MISSION("MD"),
    ADMINISTRATIVE_AND_TECHNICAL_STAFF("PAT"),
    CONSULAR_CORPS("CC"),
    CONSULAR_MISSION("MC");

    private final String value;

    RegistrationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
