import pathlib
import sys

import cv2
import numpy as np

if len(sys.argv) > 2:
    input_image_path = sys.argv[1]
    output_image_path = sys.argv[2]
else:
    program_dir = pathlib.Path(__file__).parent.resolve()
    input_image_path = str(program_dir / "sudoku.png")
    output_image_path = str(program_dir / "output.png")

# Read the image
image = cv2.imread(input_image_path)

# Convert to grayscale
gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)

# Apply bilateral filter (compared to Gaussian blur, it maintains edges better)
blur = cv2.bilateralFilter(src=gray, d=9, sigmaColor=75, sigmaSpace=75)

# Apply a threshold using Otsu's algorithm. The threshold is inverted because
# we want to have the foreground in white
_, thresh = cv2.threshold(gray, 0, 255, cv2.THRESH_BINARY_INV+cv2.THRESH_OTSU)

# Find the biggest contour (the Sudoku)
cntrs, hierarchy = cv2.findContours(
    thresh, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
sudoku_grid_contour = max(cntrs, key=lambda c: cv2.contourArea(c))
sudoku_grid_area = cv2.contourArea(sudoku_grid_contour)

# Remove everything outside of the Sudoku grid
mask = np.zeros((thresh.shape), np.uint8)
cv2.drawContours(mask, [sudoku_grid_contour], 0, 255, -1)
cv2.drawContours(mask, [sudoku_grid_contour], 0, 0, 2)
masked_th = np.zeros_like(thresh)
masked_th[mask == 255] = thresh[mask == 255]

# Apply some dilation to add some weight to the lines of the grid
kernel = cv2.getStructuringElement(cv2.MORPH_RECT, (2, 2))
masked_th = cv2.dilate(masked_th, kernel, iterations=2)

# Find contours inside the Sudoku
cntrs, hierarchy = cv2.findContours(
    masked_th, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)

hierarchy = hierarchy[0]  # get the actual inner list of hierarchy descriptions

# Find the index of the outmost square
index_square = None
for i, h in enumerate(hierarchy):
    # [Next, Previous, First_Child, Parent]
    if h[3] == -1:  # No parent
        index_square = i
        break

# Find the indexes of the cell contours
cells_indexes = []
for i, h in enumerate(hierarchy):
    # [Next, Previous, First_Child, Parent]
    if h[3] == index_square:
        cells_indexes.append(i)

# Draw the cell contours
for i in cells_indexes:
    cv2.drawContours(image, cntrs, i, (0, 255, 0), 3)

# Find the indexes of the number contours
numbers_indexes = []
for i, h in enumerate(hierarchy):
    # [Next, Previous, First_Child, Parent]
    if h[3] in cells_indexes and cv2.contourArea(cntrs[i]) > 100:
        numbers_indexes.append(i)

# Draw the number contours
for i in numbers_indexes:
    cv2.drawContours(image, cntrs, i, (0, 0, 255), 3)

cv2.imwrite(output_image_path, image)
