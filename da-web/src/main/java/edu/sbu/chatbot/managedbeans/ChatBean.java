package edu.sbu.chatbot.managedbeans;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.servlet.http.Part;

import edu.sbu.dialog.core.dialogmanager.DialogContext;

/**
 * this is managed bean , it calls the dialog context to interpret user's response
 * @author Kedar
 *
 */
@ManagedBean
@SessionScoped
public class ChatBean {
	private boolean toggle = false;
	private Part file1;
	private String textFromUser;
	private String hiddenText;
	private String systemMessage;
	private List<Message> messages = new ArrayList<Message>();
	private boolean isStarted = false;
	
	DialogContext dc;
	
	@PostConstruct
	public void init() {
		dc=new DialogContext();
	}

	public Part getFile1() {
		return file1;
	}

	public void setFile1(Part file1) {
		this.file1 = file1;
	}

	public String getTextFromUser() {
		return textFromUser;
	}

	public void setTextFromUser(String textFromUser) {
		this.textFromUser = textFromUser;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public String getHiddenText() {
		return hiddenText;
	}

	public void setHiddenText(String hiddenText) {
		this.hiddenText = hiddenText;
	}

	public String getSystemMessage() {
		return systemMessage;
	}

	public void setSystemMessage(String systemMessage) {
		this.systemMessage = systemMessage;
	}

	public void remoteCallDC(ActionEvent ae) {
		String data = "";
		if (hiddenText == null || "".equals(hiddenText))
			if (textFromUser == null || "".equals(textFromUser))
				data = "";
			else
				data = textFromUser;
		else
			data = hiddenText;
		systemMessage=dc.processRequest(data);
		Message m = new Message(data, false);
		Message m1 = new Message(systemMessage, true);
		messages.add(m);
		messages.add(m1);
		textFromUser = "";
		hiddenText = "";
	}
}
