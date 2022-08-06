package example.armeria.server.blog;

import com.linecorp.armeria.server.docs.DocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;

public class Main {
  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) throws Exception {
    Server server = newServer(8080);

    server.closeOnJvmShutdown();

    server.start().join();

    logger.info("Server has been started. Serving dummy service at http://127.0.0.1:{}\"",
            server.activeLocalPort());
    logger.info("Server has been started. Serving DocService at http://127.0.0.1:{}/docs",
            server.activeLocalPort());
  }

  static Server newServer(int port) {
    ServerBuilder sb = Server.builder();
    DocService docService =
            DocService.builder()
                    .exampleRequests(BlogService.class,
                            "createBlogPost",   // Name of service method
                            "{\"title\":\"My first blog\", \"content\":\"Hello Armeria!\"}")
                    .build();
    return sb.http(port)
            .annotatedService(new BlogService())
            .serviceUnder("/docs", docService)  // Add Documentation service
            .build();
  }
}
