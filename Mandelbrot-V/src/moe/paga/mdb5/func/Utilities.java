package moe.paga.mdb5.func;

import java.awt.Image;
import java.util.function.BiFunction;

import moe.paga.mdb5.Location;
import moe.paga.mdb5.Size;

public class Utilities {
	private Utilities() {
	}

	public static BiFunction<Location, Size, Image> compose(BiFunction<Location, Size, byte[]> f,
			BiFunction<byte[], Size, Image> g) {
		return (loc, size) -> g.apply(f.apply(loc, size), size);
	}
}
