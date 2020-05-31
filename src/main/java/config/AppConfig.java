package config;

import com.anim.tradeprofiler.main.StateMachineService;
import com.anim.tradeprofiler.main.TradeService;
import com.anim.tradeprofiler.tradeconsumer.TradeDetailsLoggerRunnable;
import com.anim.tradeprofiler.tradeproducer.entity.TradeDetails;
import com.anim.tradeprofiler.tradeproducer.loader.TradeDetailsLoader;
import com.anim.tradeprofiler.tradeproducer.loader.TradesJsonLoaderRunnable;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
public class AppConfig {

  @Bean
  public TradeDetailsLoader tradeDetailsLoader() {return new TradeDetailsLoader();}

  @Bean
  public ExecutorService workerThread1() {return Executors.newSingleThreadExecutor();}

  @Bean
  public ExecutorService workerThread2() {return Executors.newSingleThreadExecutor();}

  @Bean
  public BlockingQueue<TradeDetails> tradeDetailsBuffer() { return new LinkedBlockingQueue<TradeDetails>();}

  @Bean
  public TradesJsonLoaderRunnable tradesLoaderRunnable() { return new TradesJsonLoaderRunnable(tradeDetailsBuffer(),"trades.json", latch());
  }

  @Bean
  public TradeDetailsLoggerRunnable tradeDetailsLoggerRunnable() { return new TradeDetailsLoggerRunnable(tradeDetailsBuffer(), "output.json", latch());}

  @Bean
  public CountDownLatch latch() {return new CountDownLatch(1);}

  @Bean
  public TradeService tradeService() { return new TradeService();}

  @Bean
  public StateMachineService stateMachineService() { return new StateMachineService();}

}
