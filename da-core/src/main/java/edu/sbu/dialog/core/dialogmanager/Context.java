package edu.sbu.dialog.core.dialogmanager;

import edu.sbu.dialog.core.pojos.State;

public interface Context {
	State getCurrentState();
	
	State getPreviousState();
}
