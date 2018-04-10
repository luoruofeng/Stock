package org.lrf.stock.entity;

//现金流量表
public class CashFlow {
    //期初现金及现金等价物余额
    private Double netIncreaseInCashAndCashEquivalents;
    //经营活动产生的现金流量净额
    private Double netCashFlowsFromOperatingActivities;
    //投资活动产生的现金流量净额
    private Double netCashFlowsFromInvestmentActivities;
    //筹资活动产生的现金流量净额
    private Double netCashFlowsFromFinancingActivities ;
    //现金及现金等价物净增加额
    private Double netIncreaseInCashAndCashEquivalens;
    //期末现金及现金等价物余额
    private Double cashAndCashEquivalentsAtEndOfYear;


    public Double getNetIncreaseInCashAndCashEquivalents() {
        return netIncreaseInCashAndCashEquivalents;
    }

    public void setNetIncreaseInCashAndCashEquivalents(Double netIncreaseInCashAndCashEquivalents) {
        this.netIncreaseInCashAndCashEquivalents = netIncreaseInCashAndCashEquivalents;
    }

    public Double getNetCashFlowsFromOperatingActivities() {
        return netCashFlowsFromOperatingActivities;
    }

    public void setNetCashFlowsFromOperatingActivities(Double netCashFlowsFromOperatingActivities) {
        this.netCashFlowsFromOperatingActivities = netCashFlowsFromOperatingActivities;
    }

    public Double getNetCashFlowsFromInvestmentActivities() {
        return netCashFlowsFromInvestmentActivities;
    }

    public void setNetCashFlowsFromInvestmentActivities(Double netCashFlowsFromInvestmentActivities) {
        this.netCashFlowsFromInvestmentActivities = netCashFlowsFromInvestmentActivities;
    }

    public Double getNetCashFlowsFromFinancingActivities() {
        return netCashFlowsFromFinancingActivities;
    }

    public void setNetCashFlowsFromFinancingActivities(Double netCashFlowsFromFinancingActivities) {
        this.netCashFlowsFromFinancingActivities = netCashFlowsFromFinancingActivities;
    }

    public Double getNetIncreaseInCashAndCashEquivalens() {
        return netIncreaseInCashAndCashEquivalens;
    }

    public void setNetIncreaseInCashAndCashEquivalens(Double netIncreaseInCashAndCashEquivalens) {
        this.netIncreaseInCashAndCashEquivalens = netIncreaseInCashAndCashEquivalens;
    }

    public Double getCashAndCashEquivalentsAtEndOfYear() {
        return cashAndCashEquivalentsAtEndOfYear;
    }

    public void setCashAndCashEquivalentsAtEndOfYear(Double cashAndCashEquivalentsAtEndOfYear) {
        this.cashAndCashEquivalentsAtEndOfYear = cashAndCashEquivalentsAtEndOfYear;
    }
}
