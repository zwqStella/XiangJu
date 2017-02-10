package com.atom.socketServer.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import com.atom.socketServer.presisitence.User;

public class ServerUtil {
		private String accountId;
		private ArrayList<User> users;
		private int count;
		
		public ServerUtil(String userId) {
			super();
			this.accountId = userId;
			this.users = new ArrayList<User>(); 
			this.count = 0;
		}
		
		public String getAccountId() {
			return accountId;
		}

		public ArrayList<User> getUsers() {
			return users;
		}
		
		public void login(User user){
			user.setId(count);
			count++;
			this.users.add(user);
		}
		
		public void checkOut(int id) throws IOException{
			User user = find(id);
			if(user != null){
				user.getSocket().close();
				this.users.remove(user);
			}else{
				System.out.println("User has checked out before this command.");
			}
			
		}
		
		public User find(int id){
			Iterator<User> iter = this.users.iterator();
			User temp;
			while(iter.hasNext()){
				temp = iter.next();
				if(temp.getId() == id){
					return temp;
				}
			}
			return null;
		}
		
		public void changeUserStatus(int id, double lan ,double lon){
			User u = find(id);
			if(u != null){
				u.setLat(lan);
				u.setLon(lon);
			}else{
				System.out.println("User has checked out. Can not change position.");
			}
		}
		
		public String sibiInfo(int id){
			if(this.users.size() == 1){
				return "";
			}
			String result = ";";
			Iterator<User> iter = this.users.iterator();
			while(iter.hasNext()){
				User temp = iter.next();
				if(temp.getId() != id){
					String info = "";
					if(temp.getLat() > 200 || temp.getLon() > 200){
						info = temp.getId()+","+temp.getNickname()+"-";
					}else{
						info = temp.getId()+","+temp.getNickname()+","+temp.getLat()+","+temp.getLon()+"-";
					}
					result = result + info;
				}
			}
			result = result.substring(0, result.length()-1);
			return result;
		}

		public boolean noticeSibi(int id){
			Iterator<User> iter = this.users.iterator();
			while(iter.hasNext()){
				User temp = iter.next();
				Socket tempSocket = temp.getSocket();
				PrintWriter os=temp.getOs();
				String message = "2;"+id;
				os.println(message);
				os.flush();
			}
			return true;
		}
		
		public boolean noticeSibi(int id,String nickname){
			Iterator<User> iter = this.users.iterator();
			while(iter.hasNext()){
				User temp = iter.next();
				if(temp.getId() != id){
					Socket tempSocket = temp.getSocket();
					PrintWriter os=temp.getOs();
					String message = "2;"+id+";"+nickname;
					os.println(message);
					os.flush();
				}
			}
			return true;
		}
		
		public boolean noticeSibi(int id,double lat,double lon){
			Iterator<User> iter = this.users.iterator();
			while(iter.hasNext()){
				User temp = iter.next();
				if(temp.getId() != id){
					Socket tempSocket = temp.getSocket();
					PrintWriter os=temp.getOs();
					String message = "2;"+id+";"+lat+";"+lon;
					os.println(message);
					os.flush();
				}
			}
			return true;
		}
}

