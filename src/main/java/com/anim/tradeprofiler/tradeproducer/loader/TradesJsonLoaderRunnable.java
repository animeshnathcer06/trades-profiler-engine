package com.anim.tradeprofiler.tradeproducer.loader;

import com.anim.tradeprofiler.tradeproducer.entity.TradeDetails;
import lombok.extern.java.Log;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

@Log public class TradesJsonLoaderRunnable implements Runnable {

  private static final int MAINTENABLE_SIZE = 1000;
  private final CountDownLatch latch;
  BlockingQueue<TradeDetails> queue;
  String filePath;

  @Resource
  TradeDetailsLoader tradeDetailsLoader;

  public TradesJsonLoaderRunnable(BlockingQueue<TradeDetails> queue, String filePath, CountDownLatch latch) {
    this.queue = queue;
    this.filePath = filePath;
    this.latch = latch;
  }

  @Override
  public void run() {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource("trades.json").getInputStream()))) {
      for (String line; (line = reader.readLine()) != null; ) {
        queue.put(tradeDetailsLoader.load(line));
      }
      log.info("All trades read from the source");
      verifyCountAndReleaseLatch();
    } catch (Exception exception) {
      try {
        verifyCountAndReleaseLatch();
      } catch (InterruptedException e) {
        latch.countDown();
      }
      throw new RuntimeException(exception.getMessage());
    }
  }

  private void verifyCountAndReleaseLatch() throws InterruptedException {
    while(queue.size() > MAINTENABLE_SIZE) {
      Thread.sleep(500);
    }
    latch.countDown();
  }
}
