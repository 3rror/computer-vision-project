# Automatic thresholding

This algorithm tries to automatically find a *good* threshold to extract a
binary version of an image.

## How it works

1. Set a threshold (e.g. 128)
2. Calculate the mean of all the pixels under this threshold
3. Calculate the mean of all the pixels above this threshold
4. Calculate a new threshold as the midpoint between the means
5. Repeat the process until it converges to a final threshold

## How to use it

Compile the code

```shell
javac AutomaticThresholding
```

Execute it

```shell
java AutomaticThresholding [input image] [output image]
```

## A note about the Utils class

This code requires an `Utils` class that provides the following static methods:

* `loadMatrix()`
* `saveMatrix()`

This file is not present in the repository because of licence reasons.
At the moment, it can be found [inside this zip file](https://vision.unipv.it/corsi/ComputerVision/slides/examples-java.zip).

In any case, it is easy to write those methods from scratch.
