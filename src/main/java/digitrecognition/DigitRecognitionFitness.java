package digitrecognition;

import neuralevolution.Fitness;
import neuralevolution.Network;
import neuralevolution.Utility;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DigitRecognitionFitness implements Fitness {

    // if false, does test
    private final boolean training;
    private final double percentToDo;
    private double[][] images;
    private byte[] labels;
    private final String imagesPathString;
    private final String labelsPathString;

    public DigitRecognitionFitness(boolean training, double percentToDo) {
        this.training = training;
        this.percentToDo = percentToDo;
        if (training) {
            imagesPathString = "training/train-images.idx3-ubyte";
            labelsPathString = "training/train-labels.idx1-ubyte";
        } else {
            imagesPathString = "testing/t10k-images.idx3-ubyte";
            labelsPathString = "testing/t10k-labels.idx1-ubyte";
        }
        Path imagesPath = Paths.get(".").resolve(imagesPathString);
        Path labelsPath = Paths.get(".").resolve(labelsPathString);

        try {
            DataInputStream imagesDataInputStream =
                    new DataInputStream(new FileInputStream(imagesPath.toFile()));
            DataInputStream labelsDataInputStream =
                    new DataInputStream(new FileInputStream(labelsPath.toFile()));
            int magicImages = imagesDataInputStream.readInt();
            if (magicImages != 0x803)
            {
                throw new IOException("Expected magic header of 0x803 "
                        + "for images, but found " + magicImages);
            }

            int magicLabels = labelsDataInputStream.readInt();
            if (magicLabels != 0x801)
            {
                throw new IOException("Expected magic header of 0x801 "
                        + "for labels, but found " + magicLabels);
            }

            int numberOfImages = imagesDataInputStream.readInt();
            int numberOfLabels = labelsDataInputStream.readInt();

            if (numberOfImages != numberOfLabels)
            {
                throw new IOException("Found " + numberOfImages
                        + " images but " + numberOfLabels + " labels");
            }

            int numRows = imagesDataInputStream.readInt();
            int numCols = imagesDataInputStream.readInt();

            images = new double[numberOfImages][numRows * numCols];
            labels = new byte[numberOfLabels];

            for (int n = 0; n < numberOfImages; n++)
            {
                labels[n] = labelsDataInputStream.readByte();
                images[n] = new double[numRows * numCols];
                byte[] imagesBytes = new byte[numRows * numCols];
                read(imagesDataInputStream, imagesBytes);
                for (int i = 0; i < numRows * numCols; i++) {
                    images[n][i] = imagesBytes[i] + 128.0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void read(InputStream inputStream, byte data[])
            throws IOException
    {
        int offset = 0;
        while (true)
        {
            int read = inputStream.read(
                    data, offset, data.length - offset);
            if (read < 0)
            {
                break;
            }
            offset += read;
            if (offset == data.length)
            {
                return;
            }
        }
        throw new IOException("Tried to read " + data.length
                + " bytes, but only found " + offset);
    }

    @Override
    public void score(Network net) {
        double score = 0;
        double[] response;
        for (int i = 0; i < ((double) labels.length) * percentToDo; i++) {
            response = net.evaluate(images[i]);
            if (training) {
//                score += 0.1 * (10.0 - (double) Utility.getUtility().indexRank(response, labels[i]));
                Utility.getUtility().normalize(response);
                score += 1.0 - Utility.getUtility().mse(response, labels[i]);
            } else {
                score += Utility.getUtility().maxIndex(response) == labels[i] ? 1.0 : 0.0;
            }
        }
        net.setScore(score);
    }

    @Override
    public int getMaxScore() {
        return (int) (((double) labels.length) * percentToDo);
    }
}
