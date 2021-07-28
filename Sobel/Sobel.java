import java.io.IOException;

public class Sobel {
    public static void main(String[] args) throws IOException {
        String inputImagePath = args[0];
        String outputImagePath = args[1];

        double[][] image = Utils.loadMatrix(inputImagePath);

        double[][] edges = sobel(image);

        Utils.saveMatrix(outputImagePath, edges);
    }

    public static double[][] sobel(double[][] matrix) {
        final int w = matrix.length;
        final int h = matrix[0].length;

        double[][] output = new double[w][h];

        // Horizontal Sobel kernel
        final double[][] GX = {
                {1, 0, -1},
                {2, 0, -2},
                {1, 0, -1}
        };

        // Vertical Sobel kernel
        final double[][] GY = {
                {1, 2, 1},
                {0, 0, 0},
                {-1, -2, -1}
        };

        // Calculate both convolutions
        double[][] flippedGX = flipHorizontally(GX);
        double[][] hGradients = crossCorrelation(matrix, flippedGX);

        double[][] flippedGY = flipVertically(GY);
        double[][] vGradients = crossCorrelation(matrix, flippedGY);

        // Calculate the gradient module
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                // Calculate the gradient as sqrt(x*x + y*y)
                double gradient = Math.hypot(hGradients[i][j], vGradients[i][j]);
                output[i][j] = gradient;
            }
        }

        // Limit values between 0 and 255
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (output[i][j] < 0) {
                    output[i][j] = 0;
                } else if (output[i][j] > 255) {
                    output[i][j] = 255;
                }
            }
        }

        return output;
    }

    public static double[][] crossCorrelation(double[][] matrix, double[][] kernel) {
        final int width = matrix.length;
        final int height = matrix[0].length;

        double[][] result = new double[width][height]; // Assumes matrix is rectangular

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int newPixelValue = 0;

                for (int k = 0; k < kernel.length; k++) {
                    for (int l = 0; l < kernel[0].length; l++) {
                        // Underlining pixel coordinates
                        int x = i - 1 + k;
                        int y = j - 1 + l;

                        /*
                         Check if the underlining pixel coordinates are out of bounds (e.g. those at the border of the
                         image). Ignoring these is equivalent to considering them as black (clipping)
                        */
                        if ((x >= 0) && (x < width) && (y >= 0) && (y < height))
                            newPixelValue += kernel[k][l] * matrix[x][y];
                    }
                }

                result[i][j] = newPixelValue;
            }
        }

        return result;
    }

    public static double[][] cloneMatrix(double[][] matrix) {
        double[][] clonedMatrix = new double[matrix.length][matrix[0].length];

        for (int i = 0; i < matrix.length; i++)
            System.arraycopy(matrix[i], 0, clonedMatrix[i], 0, matrix[i].length);

        return clonedMatrix;
    }

    public static double[][] flipHorizontally(double[][] matrix) {
        double[][] flippedMatrix = cloneMatrix(matrix);

        // Swap columns
        for (int i = 0; i < flippedMatrix.length; i++) {
            for (int j = 0; j < flippedMatrix[i].length / 2; j++) {
                double temp = flippedMatrix[i][j];
                flippedMatrix[i][j] = flippedMatrix[i][flippedMatrix.length - 1 - j];
                flippedMatrix[i][flippedMatrix.length - 1 - j] = temp;
            }
        }

        return flippedMatrix;
    }

    public static double[][] flipVertically(double[][] matrix) {
        double[][] flippedMatrix = cloneMatrix(matrix);

        // Swap rows
        for (int i = 0; i < flippedMatrix.length / 2; i++) {
            for (int j = 0; j < flippedMatrix[i].length; j++) {
                double temp = flippedMatrix[i][j];
                flippedMatrix[i][j] = flippedMatrix[flippedMatrix.length - 1 - i][j];
                flippedMatrix[flippedMatrix.length - 1 - i][j] = temp;
            }
        }

        return flippedMatrix;
    }
}
