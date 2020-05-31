package com.anim.tradeprofiler.tradeconsumer;

import com.anim.tradeprofiler.tradeproducer.entity.TradeDetails;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class TradeDetailsLoggerRunnable implements Runnable {

  BlockingQueue<TradeDetails> queue;
  CountDownLatch latch;
  String filePath;
  Gson gson;
  TradesAggregator aggregator;

  public TradeDetailsLoggerRunnable(BlockingQueue<TradeDetails> queue, String filePath, CountDownLatch latch) {
    this.queue = queue;
    this.filePath = filePath;
    this.gson = new Gson();
    aggregator = new TradesAggregator(this.gson);
    this.latch = latch;
  }

  @Override
  public void run() {
    try {
      log.info("Starting to get messages initial quantity " + queue.size());
      while (!queue.isEmpty() || latch.getCount() > 0) {
        TradeDetails tradeDetails = null;
        try {
          tradeDetails = queue.take();
        } catch (InterruptedException ex) { }
        aggregator.accept(tradeDetails);
      }
      aggregator.closeAll();
      latch.await();
      log.info("All trades processed");
      log.info("Everything Completed - Shutting down");
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }
}
