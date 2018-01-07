package edu.sbu.dialog.core.feature;

import java.util.List;
import java.util.Map;

import edu.sbu.dialog.core.dialogmanager.Context;
import edu.sbu.dialog.core.pojos.NLPResponse;

public class NLPBehaviour2 implements Behaviour<String,NLPResponse>{

	@Override
	public NLPResponse doAction(Context context, String input) {
		NLPResponse response=new NLPResponse();
		// write code here to populate NLPResponse object
		//create and populate map
		response.setMap(null);
		return response;
	}
	
}
