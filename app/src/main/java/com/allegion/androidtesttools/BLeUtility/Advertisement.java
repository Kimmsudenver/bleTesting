package com.allegion.androidtesttools.BLeUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Advertisement {

    private static final int COMPANY_BYTE1 = 0;
    private static final int COMPANY_BYTE2 = 1;
    private static final int VERSION_BYTE = 2;
    // private static final int MODEL_BYTE1 = 3;
    private static final int MODEL_BYTE2 = 4;
    private static final int STATUS_BYTE = 5;
    private static final int SECURITYVERSION_BYTE = 6;

    private List<UUID> uuids;
    private byte[] companyData;
    private byte[] serialNumber;

    public Advertisement() {
        this.uuids = new ArrayList<UUID>();
    }

    public List<UUID> getUuids() {
        return uuids;
    }

    public boolean isAllegion() {
        if (companyData == null || companyData.length <= COMPANY_BYTE2)
            return false;

        if (companyData[COMPANY_BYTE1] == 0x3B && companyData[COMPANY_BYTE2] == 0x01)
            return true;
        else
            return false;
    }

    public int getVersion() {
        if (companyData == null || companyData.length <= VERSION_BYTE)
            return -1;

        return companyData[VERSION_BYTE];
    }

    public String getModel() {
        if (companyData == null || companyData.length <= MODEL_BYTE2)
            return "Unknown";

        switch (companyData[MODEL_BYTE2]) {
            case 1:
                return "NDE";
            case 2:
                return "Jaguar";
            case 3:
                return "Krill";
            case 5:
                return "Gateway";

            default:
                return "Unknown";
        }
    }

    public int getState() {
        if (companyData == null || companyData.length <= STATUS_BYTE)
            return -1;

        return companyData[STATUS_BYTE];
    }

    public int getSecurityVersion() {
        if (companyData == null || companyData.length <= SECURITYVERSION_BYTE)
            return -1;

        return companyData[SECURITYVERSION_BYTE];
    }

    public byte[] getSerialNumber() {
        return this.serialNumber;
    }

    public void setCompanyData(byte[] company) {
        this.companyData = company;
    }

    public void addUUID(UUID uuid) {
        uuids.add(uuid);
    }

    public void setSerialNumber(byte[] serial) {
        this.serialNumber = serial;
    }
}