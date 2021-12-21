package neuralevolution;

import java.util.Comparator;

public class NetworkComparator implements Comparator<Network> {

    @Override
    public int compare(Network n1, Network n2) {
        return n1.getScore() - n2.getScore();
    }
}
