package com.atom.socketServer.presisitence;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class User {
	//private static int uniquel_id = 1;
	private int id;
	private String nickname;
	private double lat;
	private double lon;
	private Socket socket;
	private BufferedReader is;
	private PrintWriter os;
	
	public User(String nickname,Socket socket) {
		super();
		this.nickname = nickname;
		this.id = -1;
		this.lat = 1000;
		this.lon = 1000;
		this.socket = socket;
		try {
			this.is = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.os = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getId() {
		return id;
	}

	public String getNickname() {
		return nickname;
	}
	public double getLat() {
		return lat;
	}
	public double getLon() {
		return lon;
	}
	
	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}

	public BufferedReader getIs() {
		return is;
	}

	public PrintWriter getOs() {
		return os;
	}
	
}

