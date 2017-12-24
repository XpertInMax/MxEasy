package com.gaf.android.mxeasy.maximoobjects;

/**
 * Created by valla on 12/21/2017.
 */

public class PO {

    private String mPONum;
    private String mDescription;
    private String mStatus;
    private String mVendor;
    private String mPurchaseAgent;
    private Double mTotalCost;

    public PO(String ponum, String description, String status, String vendor, String purchaseAgent, Double totalCost){
        this.mPONum = ponum;
        this.mDescription = description;
        this.mStatus = status;
        this.mVendor = vendor;

        this.mPurchaseAgent = purchaseAgent;
        this.mTotalCost = totalCost;
    }

    public String getmPONum() {
        return mPONum;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmStatus() {
        return mStatus;
    }

    public String getmVendor() {
        return mVendor;
    }

    public String getmPurchaseAgent() {
        return mPurchaseAgent;
    }

    public Double getmTotalCost() { return mTotalCost; }

    @Override
    public String toString() {
        return "PO{" +
                "mPONum='" + mPONum + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mStatus='" + mStatus + '\'' +
                ", mVendor='" + mVendor + '\'' +
                ", mPurchaseAgent='" + mPurchaseAgent + '\'' +
                ", mTotalCost='" + mTotalCost + '\'' +
                '}';
    }
}
