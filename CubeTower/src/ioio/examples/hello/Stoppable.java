package ioio.examples.hello;

import ioio.lib.api.exception.ConnectionLostException;

public interface Stoppable {

	
		public void stop() throws ConnectionLostException;
}
