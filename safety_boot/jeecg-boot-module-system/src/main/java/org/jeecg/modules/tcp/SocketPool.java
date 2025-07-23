package org.jeecg.modules.tcp;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.net.Socket;

public class SocketPool {

    private final GenericObjectPool<Socket> pool;

    public SocketPool(String serverAddress, int serverPort) {
        // 配置连接池
        GenericObjectPoolConfig<Socket> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(10); // 最大连接数
        config.setMinIdle(2);   // 最小空闲连接数
        config.setMaxIdle(5);   // 最大空闲连接数
        config.setTestOnBorrow(true); // 从池中获取连接时验证连接是否有效
        config.setTestOnReturn(true); // 将连接返回池时验证连接是否有效

        // 初始化连接池
        this.pool = new GenericObjectPool<>(new SocketFactory(serverAddress, serverPort), config);
    }

    public Socket borrowSocket() throws Exception {
        return pool.borrowObject();
    }

    public void returnSocket(Socket socket) {
        pool.returnObject(socket);
    }

    public void close() {
        pool.close();
    }
}
