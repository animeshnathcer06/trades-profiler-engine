package com.anim.tradeprofiler.main;

import com.anim.tradeprofiler.tradeconsumer.TradeDetailsLoggerRunnable;
import com.anim.tradeprofiler.tradeproducer.loader.TradesJsonLoaderRunnable;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Setter public class StateMachineService implements ApplicationContextAware {

  @Resource
  private ExecutorService workerThread2;

  @Resource
  private ApplicationContext applicationContext;

  public void executeAsynchronously() {
    workerThread2.execute(applicationContext.getBean(TradeDetailsLoggerRunnable.class));
    try {
      workerThread2.awaitTermination(15, TimeUnit.SECONDS);
      workerThread2.shutdownNow();
    } catch (InterruptedException exception) {
      workerThread2.shutdownNow();
    }
  }
}
