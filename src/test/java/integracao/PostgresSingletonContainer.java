package integracao;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresSingletonContainer extends PostgreSQLContainer<PostgresSingletonContainer> {
  private static final String IMAGE_VERSION = "postgres:15-alpine";
  private static PostgresSingletonContainer container;

  private PostgresSingletonContainer() {
    super(IMAGE_VERSION);
    this.withDatabaseName("testdb").withUsername("test").withPassword("test");
  }

  public static synchronized PostgresSingletonContainer getInstance() {
    if (container == null) {
      container = new PostgresSingletonContainer();
      container.start();
    }
    return container;
  }
}
