# spotpix
An image quantization application written in plain Java.

## Downloads
- TODO

## Features
### Quantization
- Quantize using K-Means algorithm.
- Quantize using Median-Cut algoritm.
- Quantize using Octree algorithm.
- Quantize using RGB collapse algorithm.
### Misc.
- Search using a set of colors (from an open image) and a target images count.
- Crop.
- Upscale and downscale using linear and nearest filters
- Display colors' amounts per image
- Display RGB color graphs.

## FLT format
An indexed images format that saves each color in the colormap's repeatition, the format's standard is provided in [Encoder's code](./src/main/java/io/codeberg/spotpix/model/encoders/FLTEncoder.java).

# Snapshots
### Original Image
![original](./snapshots/grad_default.png)
### 8-Colors K-Means
#### RGB
![8rgbkmeans](./snapshots/grad_8RGBKMeans.png)
#### LAB
![8labkmeans](./snapshots/grad_8LABKMeans.png)
### 8-Colors Median Cut
#### RGB
![8rgbmedcut](./snapshots/grad_8RGBMedianCut.png)
#### LAB
![8labmedcut](./snapshots/grad_8LABMedianCut.png)
### 8-Colors Octree
#### RGB
![8rgboct](./snapshots/grad_8RGBOctree.png)
#### LAB
![8laboct](./snapshots/grad_8LABOctree.png)

## Development Requirements
- Anything that understands POM files.

## Contributions
We are open to adding more features (especially quantization algorithms !) to our project, gui improvements are also encouraged.

## License
Under [GPL 3.0](./LICENSE)
