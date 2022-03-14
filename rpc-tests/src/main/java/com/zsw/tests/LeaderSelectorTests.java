package com.zsw.tests;

import lombok.SneakyThrows;
import org.junit.Test;

/**
 * @author Administrator
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
