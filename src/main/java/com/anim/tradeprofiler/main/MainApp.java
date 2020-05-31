package com.anim.tradeprofiler.main;

import com.anim.tradeprofiler.tradeproducer.loader.TradeDetailsLoader;
import config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


@Slf4j
public class MainApp {
    public static void startWorkerThreads(ApplicationContext context) {
        context.getBean(TradeService.class).executeAsynchronously();
        context.getBean(StateMachineService.class).executeAsynchronously();
    }

    public static void main(String[] args) {
        log.info("Main App started");

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        TradeDetailsLoader tradeDetailsLoader = context.getBean(TradeDetailsLoader.class);
        startWorkerThreads(context);
    }
}
