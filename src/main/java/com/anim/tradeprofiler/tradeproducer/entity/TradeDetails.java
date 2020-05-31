package com.anim.tradeprofiler.tradeproducer.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class TradeDetails {

    String sym;
    BigDecimal P;
    BigDecimal Q;
    long TS2;
    int barnum;

}
