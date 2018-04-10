package org.lrf.stock.entity;

//资产负债表
public class Debt {
    private String code;

    // 货币资金
    private Double cashAndCashEquivalents;
    // 应收账款
    private Double accountReceivable;
    // 存货
    private Double inventory;
    // 流动资产合计
    private Double totalCurrentAssets;
    // 固定资产净额
    private Double netValueOfFixedAssets;
    // 资产总计
    private Double totalAssets;
    // 流动负债合计
    private Double totalCurrentLiabilities;
    // 非流动负债合计
    private Double totalNonCurrentLiabilities;
    // 负债合计
    private  Double totalLiabilities;
    //所有者权益(或股东权益)合计
    private Double totalEquity;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getCashAndCashEquivalents() {
        return cashAndCashEquivalents;
    }

    public void setCashAndCashEquivalents(Double cashAndCashEquivalents) {
        this.cashAndCashEquivalents = cashAndCashEquivalents;
    }

    public Double getAccountReceivable() {
        return accountReceivable;
    }

    public void setAccountReceivable(Double accountReceivable) {
        this.accountReceivable = accountReceivable;
    }

    public Double getInventory() {
        return inventory;
    }

    public void setInventory(Double inventory) {
        this.inventory = inventory;
    }

    public Double getTotalCurrentAssets() {
        return totalCurrentAssets;
    }

    public void setTotalCurrentAssets(Double totalCurrentAssets) {
        this.totalCurrentAssets = totalCurrentAssets;
    }

    public Double getNetValueOfFixedAssets() {
        return netValueOfFixedAssets;
    }

    public void setNetValueOfFixedAssets(Double netValueOfFixedAssets) {
        this.netValueOfFixedAssets = netValueOfFixedAssets;
    }

    public Double getTotalAssets() {
        return totalAssets;
    }

    public void setTotalAssets(Double totalAssets) {
        this.totalAssets = totalAssets;
    }

    public Double getTotalCurrentLiabilities() {
        return totalCurrentLiabilities;
    }

    public void setTotalCurrentLiabilities(Double totalCurrentLiabilities) {
        this.totalCurrentLiabilities = totalCurrentLiabilities;
    }

    public Double getTotalNonCurrentLiabilities() {
        return totalNonCurrentLiabilities;
    }

    public void setTotalNonCurrentLiabilities(Double totalNonCurrentLiabilities) {
        this.totalNonCurrentLiabilities = totalNonCurrentLiabilities;
    }

    public Double getTotalLiabilities() {
        return totalLiabilities;
    }

    public void setTotalLiabilities(Double totalLiabilities) {
        this.totalLiabilities = totalLiabilities;
    }

    public Double getTotalEquity() {
        return totalEquity;
    }

    public void setTotalEquity(Double totalEquity) {
        this.totalEquity = totalEquity;
    }
}
