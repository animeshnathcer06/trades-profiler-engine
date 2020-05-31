package com.anim.tradeprofiler.tradeconsumer;

import com.anim.tradeprofiler.tradeproducer.entity.TradeDetails;
import com.google.gson.Gson;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Objects;

@Setter
@Slf4j
public class EagerTradesAggregator {
  private long nextStartingTime = -1;
  private static final long INTERVAL_BATCH_TIME = 15_000_000_000l;
  private static final AggregatedOHLCTradeDetails BLANK_DETAILS =
      AggregatedOHLCTradeDetails.builder().event("ohlc_notify").barnum(1).build();
  private int currentBarNum = 1;
  private int lastEntered = -1;
  private BigDecimal openPrice = BigDecimal.valueOf(0);
  private BigDecimal lastPrice = BigDecimal.valueOf(0);
  private BigDecimal high = BigDecimal.valueOf(0);
  private BigDecimal low = BigDecimal.valueOf(0);
  private BigDecimal quantity = BigDecimal.valueOf(0);
  private AggregatedOHLCTradeDetails previousOhlc = BLANK_DETAILS;
  private Gson gson;

  public EagerTradesAggregator(Gson gson) {
    this.gson = gson;
  }


  public void accept(TradeDetails tradeDetails) {
    if (Objects.isNull(tradeDetails)) {
      return;
    }
    if (nextStartingTime == -1 || (tradeDetails.getTS2()  > nextStartingTime)) {
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
    if (nextStartingTime == -1) {
      nextStartingTime = tradeDetails.getTS2();
      currentBarNum = 1;
//      log.info("Next time " + (nextStartingTime / 1000_000_000l));
    } else {
      long intervalsMissed = (tradeDetails.getTS2() - nextStartingTime) / INTERVAL_BATCH_TIME;
      nextStartingTime += (intervalsMissed + 1) * INTERVAL_BATCH_TIME;
      currentBarNum += ((int) intervalsMissed + 1);
//      log.info("Missed intervals " + intervalsMissed);
//      log.info("Next time " + (nextStartingTime / 1000_000_000l));
    }
    high = tradeDetails.getP();
    low = tradeDetails.getP();
    lastPrice = tradeDetails.getP();
    openPrice = tradeDetails.getP();
    quantity = BigDecimal.valueOf(0);
  }

  private void tryClosePreviousBar() {
    if (nextStartingTime != -1) {
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
