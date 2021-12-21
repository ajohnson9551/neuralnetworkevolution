package lunarlander;

import neuralevolution.*;

public class LunarLanderNetwork extends BasicNetwork {

    @Override
    public Output evaluate(Input in) {
        Output out = new LunarLanderOutput();
        out.assignFromArray(evaluate(in.toArray()));
        return out;
    }

    public LunarLanderNetwork(BasicNetworkParameters params) {
        super(params);
    }

    public LunarLanderNetwork(LunarLanderNetwork net) {
        super(net);
    }
}
