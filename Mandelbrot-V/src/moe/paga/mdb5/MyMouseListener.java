package moe.paga.mdb5;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.EventObject;
import java.util.Objects;

public class MyMouseListener extends MouseAdapter {
	private final MyPanel target;

	/**
	 * Create a mouse listener that handles events for the specified target.
	 * Events received with different sources will be ignored. Created listener
	 * will <i>not</i> be automatically added to the target.
	 * 
	 * @param target
	 *            the specified target
	 * @see #addFor(MyPanel)
	 */
	public MyMouseListener(MyPanel target) {
		this.target = Objects.requireNonNull(target);
	}

	/**
	 * Add a {@code MyMouseListener} to the specified target.
	 * 
	 * @param target
	 *            the specified target
	 * @return the added listener; events received with different sources will
	 *         be ignored, if added elsewhere
	 * @see #MyMouseListener(MyPanel)
	 */
	public static MyMouseListener addFor(MyPanel target) {
		MyMouseListener listener = new MyMouseListener(target);
		target.addMouseListener(listener);
		target.addMouseMotionListener(listener);
		target.addMouseWheelListener(listener);
		return listener;
	}

	private int lastX, lastY;

	@Override
	public void mousePressed(MouseEvent e) {
		if (!checkEvent(e))
			return;
		lastX = e.getX();
		lastY = e.getY();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (!checkEvent(e))
			return;
		int x = e.getX(), y = e.getY();
		Offset offset = target.getOffset();
		target.setOffset(new Offset(offset.getX() - (x - lastX), offset.getY() - (y - lastY)));
		lastX = x;
		lastY = y;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (!checkEvent(e))
			return;
		int rot = e.getWheelRotation();
		if (rot == 0)
			return;
		Location location = target.getImageLocation();
		if (rot > 0 && location.isRoot())
			return;
		Offset offset = target.getOffset();
		int x = offset.getX(), y = offset.getY();
		x += e.getX();
		y += e.getY();
		if (rot < 0) {
			location = location.child(Location.Quadrant.III);
			x *= 2;
			y *= 2;
		} else {
			location = location.parent();
			x /= 2;
			y /= 2;
		}
		x -= e.getX();
		y -= e.getY();

		target.setImageLocationAndOffset(location, new Offset(x, y));
	}

	private boolean checkEvent(EventObject e) {
		if (e.getSource() != target) {
			System.err.println("Event ignored (expected source: " + target + "; found: " + e.getSource() + ")");
			return false;
		}
		return true;
	}
}
