package com.anim.tradeprofiler.tradeproducer;


import com.anim.tradeprofiler.tradeproducer.entity.TradeDetails;
import com.anim.tradeprofiler.tradeproducer.loader.TradeDetailsLoader;
import config.AppConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class TestTradeDetailsLoader {

    @Resource
    TradeDetailsLoader tradeDetailsLoader;

    @Test
    public void testLoad() {
        TradeDetails tradeDetails = tradeDetailsLoader.load("{\"sym\":\"XZECXXBT\", \"T\":\"Trade\",  \"P\":0.01947, \"Q\":0.1, \"TS\":1538409720.3813, \"side\": \"s\", \"TS2\":1538409725339216503}");
        Assert.assertNotNull(tradeDetails);
        Assert.assertEquals("XZECXXBT", tradeDetails.getSym());
    }

    public void testLoadBulk() {
    }
}