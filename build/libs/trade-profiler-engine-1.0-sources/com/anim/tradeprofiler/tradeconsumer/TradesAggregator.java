package com.anim.tradeprofiler.tradeconsumer;

import com.anim.tradeprofiler.tradeproducer.entity.TradeDetails;
import com.google.gson.Gson;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Objects;

@Setter
@Slf4j
public class TradesAggregator {
  private long startingTime = -1;
  private static final long INTERVAL_BATCH_TIME = 15_000_000_000l;
  private static final AggregatedOHLCTradeDetails BLANK_DETAILS = AggregatedOHLCTradeDetails.builder().build();
  private int currentBarNum = -1;
  private int lastEntered = -1;
  private BigDecimal openPrice = BigDecimal.valueOf(0);
  private BigDecimal lastPrice = BigDecimal.valueOf(0);
  private BigDecimal high = BigDecimal.valueOf(0);
  private BigDecimal low = BigDecimal.valueOf(0);
  private BigDecimal quantity = BigDecimal.valueOf(0);
  private AggregatedOHLCTradeDetails previousOhlc = BLANK_DETAILS;
  private Gson gson;

  public TradesAggregator(Gson gson) {
    this.gson = gson;
  }


  public void accept(TradeDetails tradeDetails) {
    if (Objects.isNull(tradeDetails)) {
      return;
    }
    if (startingTime == -1 || (tradeDetails.getTS2() - startingTime > INTERVAL_BATCH_TIME)) {
      tryClosePreviousBar();
      setupNewBar(tradeDetails);
    } else {
      pushPreviousInSameBar();
    }

    this.high = high.compareTo(tradeDetails.getP()) < 0 ? tradeDetails.getP() : high;
    this.low = low.compareTo(tradeDetails.getP()) > 0 ? tradeDetails.getP() : low;
    this.quantity = this.quantity.add(tradeDetails.getQ());

    AggregatedOHLCTradeDetails ohlcTradeDetails =
        AggregatedOHLCTradeDetails.builder()
            .barnum(currentBarNum)
            .c(new BigDecimal(0))
            .event("ohlc_notify")
            .h(high)
            .l(low)
            .o(openPrice)
            .volume(this.quantity)
            .symbol(tradeDetails.getSym())
            .build();

    previousOhlc = ohlcTradeDetails;

  }

  private void setupNewBar(TradeDetails tradeDetails) {
    startingTime = tradeDetails.getTS2();
    currentBarNum++;
    lastEntered++;
    high = tradeDetails.getP();
    low = tradeDetails.getP();
    lastPrice = tradeDetails.getP();
    openPrice = tradeDetails.getP();
    quantity = BigDecimal.valueOf(0);
  }

  private void tryClosePreviousBar() {
    if (startingTime != -1) {
      previousOhlc = previousOhlc.toBuilder().c(lastPrice).build();
      logJsonDetails(previousOhlc);
    }
  }

  private void pushPreviousInSameBar() {
    logJsonDetails(previousOhlc);
  }

  public void closeAll() {
    tryClosePreviousBar();
  }

  private void logJsonDetails(AggregatedOHLCTradeDetails ohlcTradeDetails) {
    log.info(gson.toJson(ohlcTradeDetails));
  }
}
