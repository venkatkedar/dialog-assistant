package edu.sbu.dialog.core.feature;

import edu.sbu.dialog.core.dialogmanager.Context;

/**
 * this interface declares the action method that takes input of type I
 * and output of type O
 * @author Kedar
 *
 * @param <I> input data type
 * @param <O> output data type
 */
public interface Behaviour<I,O>  {
	public O doAction(Context context,I input);
}
