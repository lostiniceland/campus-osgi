package campus.osgi.swt.launch.internal;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * This Service handles the SWT application. 
 * As long as the service is running, a SWT-Display and Shell is run in a separate thread which is the event-loop for SWT.
 * NOTE: obviously this is not running on Mac-OS, because there it the event-loop must run on the main thread. I dont care for Mac right now :-)
 */
@Component
public class App {

	private volatile Thread thread;

	@Activate
	public void startBundle() {
		thread = new Thread(new SwtEventLoop());
		thread.start();
	}

	@Deactivate
	public void stopBundle() {
		thread.interrupt();
		thread = null;
	}

	private final class SwtEventLoop implements Runnable {
		@Override
		public void run() {
			Thread thisThread = Thread.currentThread();

			Display display = new Display();
			Shell shell = new Shell(display);
			shell.open();

			// run the event loop as long as the window is open and thread was not interrupted
			while (!thisThread.isInterrupted() && !shell.isDisposed()) {
				// read the next OS event queue and transfer it to a SWT event
				if (!display.readAndDispatch()) {
					// if there are currently no other OS event to process
					// sleep until the next OS event is available
					display.sleep(); 
				}
			}
			shell.close();
			display.close();
			shell.dispose();
			display.dispose();
		}
	}
}
