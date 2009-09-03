/**
 * junit-interceptors: JUnit Interceptors Collection
 *
 * Copyright (c) 2009 by Alistair A. Israel.
 * This software is made available under the terms of the MIT License.
 *
 * Created Aug 28, 2009
 */
package junit.interceptors;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;

import org.junit.rules.ExternalResource;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * 
 * @author Alistair A. Israel
 */
public class HttpServerInterceptor extends ExternalResource {

    /**
     * The default HTTP port to listen to, port 80
     */
    public static final int DEFAULT_HTTP_PORT = 8000;

    private final InetSocketAddress address;

    private HttpServer httpServer;

    /**
     *
     */
    public HttpServerInterceptor() {
        this(DEFAULT_HTTP_PORT);
    }

    /**
     * @param port
     *        the port to listen (HTTP) on
     */
    public HttpServerInterceptor(final int port) {
        this(new InetSocketAddress(port));
    }

    /**
     * @param address
     *        {@link InetSocketAddress}
     */
    public HttpServerInterceptor(final InetSocketAddress address) {
        this.address = address;
    }

    /**
     * @return the address
     */
    public final InetSocketAddress getAddress() {
        return address;
    }

    /**
     * @param path
     *        the root URI path to associate the context with
     * @param handler
     *        the handler to invoke for incoming requests
     */
    public final void addHandler(final String path, final HttpHandler handler) {
        httpServer.createContext(path, handler);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.junit.rules.ExternalResource#before()
     */
    @Override
    protected final void before() throws Throwable {
        super.before();
        httpServer = HttpServer.create(address, 0);
        httpServer.start();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.junit.rules.ExternalResource#after()
     */
    @Override
    protected final void after() {
        httpServer.stop(0);
        super.after();
    }

    /**
     * @param path
     *        the URI path to GET
     * @return the HttpURLConnection
     * @throws IOException
     *         on exception
     */
    public final HttpURLConnection get(final String path) throws IOException {
        final URL url = new URL("http://" + address.getHostName() + ":" + address.getPort() + path);
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        return connection;
    }
}
