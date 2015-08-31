package moe.paga.mdb5.func;

import java.util.Objects;
import java.util.function.BiFunction;

/**
 * Some utilities about functions.
 * 
 * @author johnchen902
 */
public class Utilities {
	private Utilities() {
	}

	/**
	 * Returns a function composed of two relevant functions.
	 */
	public static <T, U, R1, R2> BiFunction<T, U, R2> compose(BiFunction<T, U, R1> f, BiFunction<R1, U, R2> g) {
		Objects.requireNonNull(f);
		Objects.requireNonNull(g);
		return (t, u) -> g.apply(f.apply(t, u), u);
	}
}
