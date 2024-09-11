package com.gibatekpro.ecommerceandroid.checkout.model;

public class PaymentInfo {
    private int amount;
    private String currency;
    private String description;
    private String receiptEmail;

    private PaymentInfo(Builder builder) {
        this.amount = builder.amount;
        this.currency = builder.currency;
        this.description = builder.description;
        this.receiptEmail = builder.receiptEmail;
    }

    public int getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDescription() {
        return description;
    }

    public String getReceiptEmail() {
        return receiptEmail;
    }

    @Override
    public String toString() {
        return "PaymentInfo{" +
                "amount=" + amount +
                ", currency='" + currency + '\'' +
                ", description='" + description + '\'' +
                ", receiptEmail='" + receiptEmail + '\'' +
                '}';
    }

    public static class Builder {
        private int amount;
        private String currency;
        private String description;
        private String receiptEmail;

        public Builder amount(int amount) {
            this.amount = amount;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder receiptEmail(String receiptEmail) {
            this.receiptEmail = receiptEmail;
            return this;
        }

        public PaymentInfo build() {
            return new PaymentInfo(this);
        }
    }
}