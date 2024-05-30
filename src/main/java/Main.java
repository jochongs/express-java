import app.App;
import app.user.UserRouter;
import server.Server;

public class Main {
    public static void main(String[] args) {
        UserRouter.register();

        App.server.use((exception, req, res) -> {
            res.status(exception.status).send(exception.message);
        });

        App.server.listen(80);
    }
}
