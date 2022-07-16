package httpserver.javasocket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Set;
import java.util.concurrent.Executor;

public class Server {

    private ServerSocketChannel sockchan;
    private Selector selector;
    private SelectionKey listenkey;

    Server(InetSocketAddress addr, int backlog) throws IOException {
        sockchan = ServerSocketChannel.open();
        selector = Selector.open();
        listenkey = sockchan.register(selector, SelectionKey.OP_ACCEPT);

        sockchan.socket().bind(addr, backlog);
    }

    public void start() {
        ServerExecutor x = new ServerExecutor();
    }

    // ---------------------- Classes --------------------------- //

    private static class ServerExecutor implements Executor {
        public void execute(Runnable r) {
            r.run();
        }
    }

    class Dispatcher implements Runnable {
        public void run() {
            while (true) {
                try {
                    selector.select(1000);
                    Set<SelectionKey> selected = selector.selectedKeys();
                    for (final SelectionKey key : selected.toArray(SelectionKey[]::new)) {
                        selected.remove(key);
                        if (key.equals(listenkey)) {
                            // TODO
                        } else {
                            // TODO
                        }
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
    }

}
