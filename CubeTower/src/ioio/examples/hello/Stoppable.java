package ioio.examples.hello;

import ioio.lib.api.exception.ConnectionLostException;

public interface Stoppable {

		/**
		 * stops the moving object
		 * @throws ConnectionLostException
		 */
		public void stop() throws ConnectionLostException;
}
