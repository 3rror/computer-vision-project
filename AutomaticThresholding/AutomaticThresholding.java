import java.io.IOException;

public class AutomaticThresholding {
    public static void main(String[] args) throws IOException {
        String inputImagePath = args[0];
        String outputImagePath = args[1];

        double[][] image = Utils.loadMatrix(inputImagePath);

        double[][] binaryImage = automaticThreshold(image);

        Utils.saveMatrix(outputImagePath, binaryImage);
    }

    public static double[][] automaticThreshold(double[][] image) {
        double th = 128; // Starting threshold value

        // Adjust the threshold iteratively, until it converges
        while (true) {
            double underThAccumulator = 0;
            int underThCounter = 0;

            double aboveThAccumulator = 0;
            int aboveThCounter = 0;

            for (double[] pixelRow : image) {
                for (double pixel : pixelRow) {
                    if (pixel <= th) {
                        underThAccumulator += pixel;
                        underThCounter += 1;
                    } else {
                        aboveThAccumulator += pixel;
                        aboveThCounter += 1;
                    }
                }
            }

            // Calculate the next threshold as the mid point between means
            double leftMean = underThAccumulator / underThCounter;
            double rightMean = aboveThAccumulator / aboveThCounter;
            double nextTh = (leftMean + rightMean) / 2;

            // Exit the loop if the threshold is the same of the previous step
            if (nextTh == th) break;

            th = nextTh;
        }

        final int width = image.length;
        final int height = image[0].length;

        double[][] outputBinaryImage = new double[width][height];

        // Apply the final threshold
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (image[i][j] < th) {
                    outputBinaryImage[i][j] = 0;
                } else {
                    outputBinaryImage[i][j] = 255;
                }
            }
        }

        return outputBinaryImage;
    }
}
