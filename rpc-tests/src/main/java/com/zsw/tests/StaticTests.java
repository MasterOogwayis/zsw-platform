package com.zsw.tests;

import java.net.InetSocketAddress;

/**
 * @author Administrator
 **/
public class StaticTests {

    public static void main(String[] args) {

        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 80);
        System.out.println(address.getHostString());
        System.out.println(address.getAddress());
        System.out.println(address.getHostName());
        System.out.println(address.getPort());

        System.err.println(address.toString().split("/")[0]);

    }

}
