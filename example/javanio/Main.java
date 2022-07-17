package example.javanio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel server = ServerSocketChannel
                .open()
                .bind(new InetSocketAddress(8002), 0);

        Selector selector = Selector.open();
        SelectionKey accept = server
                .configureBlocking(false)
                .register(selector, SelectionKey.OP_ACCEPT);

        Set<SelectionKey> connections = new HashSet<>();
        boolean finished = false;

        while (!finished) {
            selector.select();
            Set<SelectionKey> ks = selector.selectedKeys();
            for (final SelectionKey k : ks) {
                ks.remove(k);
                if (k.equals(accept)) {
                    connections.add(server
                            .accept()
                            .configureBlocking(false)
                            .register(selector, SelectionKey.OP_READ));
                } else if (connections.contains(k)) {
                    k.cancel();
                    SocketChannel connection = (SocketChannel) k.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(2048);
                    int size = connection.read(buffer);
                    System.out.println(size);
                    System.out.println(new String(buffer.array()));

                    connection.write(ByteBuffer.wrap("Hello World".getBytes()));
                    connection.close();
                    connections.remove(k);
                } else {
                    finished = true;
                }
            }
        }

        server.close();
    }
}
