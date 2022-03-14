package com.zsw.tests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author Administrator
 **/
@Slf4j
public class SelectorClient  extends LeaderSelectorListenerAdapter implements Closeable {

    private final String name;

    private final String path;

    private final CuratorFramework clieht;

    private final LeaderSelector leaderSelector;

    public SelectorClient(String name, String path, CuratorFramework clieht) {
        this.name = name;
        this.path = path;
        this.clieht = clieht;
        this.leaderSelector = new LeaderSelector(this.clieht, path, this);
    }


    public void start() {
        this.leaderSelector.start();
    }

    @Override
    public void close() throws IOException {
        this.leaderSelector.close();
    }

    @Override
    public void takeLeadership(CuratorFramework client) throws Exception {
        log.info("{} 现在是 leader，持续成为leader", this.name);
        // 阻塞当前线程，一直持有 leader 权限节点，知道进程关闭
        System.in.read();
    }

}
