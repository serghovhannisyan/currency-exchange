package com.serg.currencyexchange.batch.service;

import java.time.LocalDateTime;

public interface Periodic {

    void handleData(final String currencyName, final LocalDateTime date);
}
