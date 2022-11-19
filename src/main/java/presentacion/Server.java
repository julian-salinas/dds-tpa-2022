package presentacion;

import spark.Spark;
import spark.debug.DebugScreen;

public class Server {
  public static void main(String[] args) {
    Bootstrap.init();
    Spark.port(9000);
    Router.configure();
    DebugScreen.enableDebugScreen();
  }
}
