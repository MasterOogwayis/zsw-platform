package com.zsw.tests;

import lombok.SneakyThrows;
import org.junit.Test;

/**
 * @author ZhangShaowei on 2019/7/16 15:31
 **/
public class LeaderSelectorTests extends BaseCuratorTests {


    @Test
    @SneakyThrows
    public void test() {
        SelectorClient selectorClient = new SelectorClient("clientB", "/leader", super.client);
        selectorClient.start();

        System.in.read();

    }


}
