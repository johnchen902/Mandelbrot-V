package moe.paga.mdb5.func;

import java.util.function.BiFunction;

import moe.paga.mdb5.Location;
import moe.paga.mdb5.Size;
import moe.paga.mdb5.Location.Quadrant;

/**
 * Compute Mandelbrot set using <code>double</code> and escape time algorithm.
 * 
 * @author johnchen902
 */
public class MandelbrotDouble implements BiFunction<Location, Size, byte[]> {
	@Override
	public byte[] apply(Location loc, Size size) {
		double x = -2, y = -1.25, sz = 2.5;
		for (Quadrant q : loc.getQuadrants()) {
			sz *= 0.5;
			if (q.isXPositive())
				x += sz;
			if (q.isYPositive())
				y += sz;
		}

		int w = size.getWidth(), h = size.getHeight();
		byte[] data = new byte[w * h];

		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {
				double cr = x + i * sz / w, ci = y + j * sz / h;
				double zr = cr, zi = ci;
				int count;
				for (count = 0; zr * zr + zi * zi < 4.0 && count < 0xFF; count++) {
					double newr = zr * zr - zi * zi + cr;
					double newi = 2.0 * zr * zi + ci;
					zr = newr;
					zi = newi;
				}
				data[j * w + i] = (byte) count;
			}
		}

		return data;
	}
}
