package com.kotakNeo.kotakNeo.model;

public class PredictionResponse {
    private String stock_name;
    private double differences;
    private int total_buy_quantity;
    private int total_sell_quantity;
    private double predicted_price;

    // getters and setters
    public String getStock_name() { return stock_name; }
    public void setStock_name(String stock_name) { this.stock_name = stock_name; }

    public double getDifferences() { return differences; }
    public void setDifferences(double differences) { this.differences = differences; }

    public int getTotal_buy_quantity() { return total_buy_quantity; }
    public void setTotal_buy_quantity(int total_buy_quantity) { this.total_buy_quantity = total_buy_quantity; }

    public int getTotal_sell_quantity() { return total_sell_quantity; }
    public void setTotal_sell_quantity(int total_sell_quantity) { this.total_sell_quantity = total_sell_quantity; }

    public double getPredicted_price() { return predicted_price; }
    public void setPredicted_price(double predicted_price) { this.predicted_price = predicted_price; }
}
