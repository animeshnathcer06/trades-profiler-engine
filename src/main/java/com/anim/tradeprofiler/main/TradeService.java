package com.anim.tradeprofiler.main;

import com.anim.tradeprofiler.tradeproducer.loader.TradesJsonLoaderRunnable;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Setter public class TradeService implements ApplicationContextAware {

  @Resource
  private ExecutorService workerThread1;

  @Resource
  private ApplicationContext applicationContext;

  public void executeAsynchronously() {
    workerThread1.execute(applicationContext.getBean(TradesJsonLoaderRunnable.class));
    try {
      workerThread1.awaitTermination(5, TimeUnit.SECONDS);
      workerThread1.shutdown();
    } catch (InterruptedException exception) {
      workerThread1.shutdown();
    }
  }
}
