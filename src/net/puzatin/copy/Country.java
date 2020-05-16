package net.puzatin.copy;

enum Country {
    CANADA("CAD"), POLAND("PLN"), GERMANY("EUR");

    String currency;

    Country(String currency) {
        this.currency = currency;
    }

}