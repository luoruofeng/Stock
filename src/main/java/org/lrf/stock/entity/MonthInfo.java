package org.lrf.stock.entity;

public class MonthInfo {
    //股票代码
    private String code;
    //利润表
    private Profits profits;
    //资产负债表
    private Debt debt;
    //现金流量表
    private CashFlow cashFlow;

    public MonthInfo() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Profits getProfits() {
        return profits;
    }

    public void setProfits(Profits profits) {
        this.profits = profits;
    }

    public Debt getDebt() {
        return debt;
    }

    public void setDebt(Debt debt) {
        this.debt = debt;
    }

    public CashFlow getCashFlow() {
        return cashFlow;
    }

    public void setCashFlow(CashFlow cashFlow) {
        this.cashFlow = cashFlow;
    }
}
