package utiltestpackage;

import java.awt.Toolkit;

import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

/**
 * The listener interface for receiving ring events. The class that is
 * interested in processing a ring event implements this interface, and the
 * object created with that class is registered with a component using the
 * component's <code>addRingListener<code> method. When the ring event occurs,
 * that object's appropriate method is invoked.
 *
 * @see RingEvent
 */
public class RingListener extends RunListener {

	/*
	 * (non-Javadoc)
	 *
	 * @see org.junit.runner.notification.RunListener#testFinished(org.junit.runner.
	 * Description)
	 */
	@Override
	public void testFinished(Description description) {
		final Runnable runnable = (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
		if (runnable != null) {
			runnable.run();
		}
	}
}