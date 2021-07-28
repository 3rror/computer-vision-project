# Sudoku grid and number recognizer

This very simple code tries to recognize cells and the external edge of the
numbers inside a Sudoku.

It works well with the provided example image, but it might not work with other photos.

The program comes in two versions: one _normal_ and one _extended_.

The extended version stops at (almost) every step to show the result of the
applied operation. It also applies OCR using tesseract, which must be installed
separately.

## How to use it

Install required packages

```shell
pip3 install python-opencv numpy # Add pytesseract imutils for the extended version
```

Run the code

```shell
python3 sudoku.py [input image] [output image]
```
