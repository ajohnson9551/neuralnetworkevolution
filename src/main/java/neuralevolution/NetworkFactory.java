package neuralevolution;

public class NetworkFactory {

    private final NetworkParameters params;
    private final Class networkClassName;

    public NetworkFactory(NetworkParameters params, Class networkClassName) {
        this.params = params;
        this.networkClassName = networkClassName;
    }

    public Network createNew() {
        try {
            return (Network) networkClassName.getDeclaredConstructor(params.getClass())
                    .newInstance(params);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        return null;
    }

    public Network createNew(Network net) {
        try {
            return (Network) networkClassName.getDeclaredConstructor(networkClassName)
                    .newInstance(net);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        return null;
    }

    public Network createNew(Network net1, Network net2) {
        Network net = createNew(net1);
        net.averageWith(net2);
        return net;
    }
}
