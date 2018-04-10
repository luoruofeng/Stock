package org.lrf.stock.entity;

//利润表
public class Profits {
    //营业收入
    private Double operatingIncome;
    //营业成本
    private Double operatingCost;
    //营业利润
    private Double operatingProfit;
    //利润总额
    private Double totalProfit;
    //所得税费用
    private Double incomeTaxExpense;
    //净利润
    private Double netProfit;
    //基本每股收益
    private Double basicEarningsPerShare;

    public Double getOperatingIncome() {
        return operatingIncome;
    }

    public void setOperatingIncome(Double operatingIncome) {
        this.operatingIncome = operatingIncome;
    }

    public Double getOperatingCost() {
        return operatingCost;
    }

    public void setOperatingCost(Double operatingCost) {
        this.operatingCost = operatingCost;
    }

    public Double getOperatingProfit() {
        return operatingProfit;
    }

    public void setOperatingProfit(Double operatingProfit) {
        this.operatingProfit = operatingProfit;
    }

    public Double getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(Double totalProfit) {
        this.totalProfit = totalProfit;
    }

    public Double getIncomeTaxExpense() {
        return incomeTaxExpense;
    }

    public void setIncomeTaxExpense(Double incomeTaxExpense) {
        this.incomeTaxExpense = incomeTaxExpense;
    }

    public Double getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(Double netProfit) {
        this.netProfit = netProfit;
    }

    public Double getBasicEarningsPerShare() {
        return basicEarningsPerShare;
    }

    public void setBasicEarningsPerShare(Double basicEarningsPerShare) {
        this.basicEarningsPerShare = basicEarningsPerShare;
    }
}
