package neuralevolution;

import java.util.Comparator;

public class NetworkComparator implements Comparator<Network> {

    @Override
    public int compare(Network n1, Network n2) {
        double diff = n1.getScore() - n2.getScore();
        if (diff > 0) {
            return 1;
        } else if (diff < 0) {
            return -1;
        }
        return 0;
    }
}
