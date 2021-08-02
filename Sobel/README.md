# Sobel

This algorithm applies the Sobel operator to implement basic edge detection.

## How it works

1. Clone the pixel matrix
2. Flip Sobel filters horizontally and vertically
3. Apply cross-correlation using the calculated kernels
4. Calculate the gradient module
5. Limit the range of values between 0 and 255

## How to use it

Compile the code

```shell
javac Sobel.java
```

Execute it

```shell
java Sobel [input image] [output image]
```

## A note about the Utils class

This code requires an `Utils` class that provides the following static methods:

* `loadMatrix()`
* `saveMatrix()`

This file is not present in the repository because of licence reasons.
At the moment, it can be found [inside this zip file](https://vision.unipv.it/corsi/ComputerVision/slides/examples-java.zip).

In any case, it is easy to write those methods from scratch.
