package pl.allegro.tech.embeddedelasticsearch.junit4;

import org.junit.rules.ExternalResource;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElasticsearchStartupException;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * An {@link ExternalResource} rule that wraps an {@link EmbeddedElastic}
 * instance and performs the start/stop operations before in the
 * {@link #before()} and {@link #after()} methods respectively. This
 * is probably better suited to be used with {@link org.junit.ClassRule @ClassRule}
 * since the instance would only be started and stopped once when running
 * test cases.
 */
public class EmbeddedElasticRule extends ExternalResource {

    private final EmbeddedElastic embeddedElastic;

    /**
     * Create a new {@link EmbeddedElasticRule}.
     *
     * @param embeddedElastic the {@link EmbeddedElastic.Builder Builder} is used
     *                        here so that the rule can managed the lifecycle of
     *                        the {@link EmbeddedElastic} instance.
     */
    public EmbeddedElasticRule(EmbeddedElastic.Builder embeddedElastic) {
        this.embeddedElastic = embeddedElastic.build();
    }

    /**
     * Get the {@link EmbeddedElastic} instance.
     *
     * @return the {@link EmbeddedElastic} instance.
     */
    public EmbeddedElastic getElastic() {
        return embeddedElastic;
    }

    /**
     * Start the {@link EmbeddedElastic} instance.
     *
     * @throws IOException          if there is a problem starting the instance.
     * @throws InterruptedException if there is a problem starting the instance.
     */
    @Override
    protected void before() throws IOException, InterruptedException {
        this.embeddedElastic.start();
    }

    /**
     * Stop the {@link EmbeddedElastic} instance.
     */
    @Override
    protected void after() {
        this.embeddedElastic.stop();
    }

    /**
     * Find an available port.
     *
     * @return the available port.
     * @throws EmbeddedElasticsearchStartupException if there are no ports available.
     */
    public static int getAvailablePort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (IOException e) {
            throw new EmbeddedElasticsearchStartupException("Unable to find available port.");
        }
    }
}
