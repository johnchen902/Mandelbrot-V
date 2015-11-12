package moe.paga.mdb5.func;

import java.awt.Image;
import java.awt.Point;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.function.BiFunction;

import moe.paga.mdb5.Size;

/**
 * Directly create the image with built-in linear gray scale color space and the
 * byte array as data buffer of the image.
 * 
 * @see BufferedImage
 * @see ColorSpace#CS_GRAY
 * @see DataBufferByte
 * @author johnchen902
 */
public class DirectImage implements BiFunction<byte[], Size, Image> {
	@Override
	public Image apply(byte[] data, Size size) {
		ColorModel colorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY), false, false,
				Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
		DataBuffer dataBuffer = new DataBufferByte(data, data.length);
		SampleModel sampleModel = new ComponentSampleModel(DataBuffer.TYPE_BYTE, size.getWidth(), size.getHeight(), 1,
				size.getWidth(), new int[] { 0 });
		WritableRaster raster = Raster.createWritableRaster(sampleModel, dataBuffer, new Point(0, 0));
		return new BufferedImage(colorModel, raster, false, null);
	}
}
