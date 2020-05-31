package com.anim.tradeprofiler.tradeconsumer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
public class AggregatedOHLCTradeDetails {

  private BigDecimal o;
  private BigDecimal h;
  private BigDecimal l;
  private BigDecimal c;
  private BigDecimal volume;
  private String event;
  private String symbol;
  private int barnum;

}
