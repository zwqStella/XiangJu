package com.atom.socketServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import com.atom.socketServer.presisitence.Account;
import com.atom.socketServer.presisitence.User;
import com.atom.socketServer.service.*;
import com.atom.socketServer.util.SendEmailValidateNumber;
import com.atom.socketServer.util.ServerUtil;


public class SocketServer {
    private static final int PORT = 9999;//自定义端口号
    private ServerSocket server = null;
    private ExecutorService executorService = null; //线程池
    private static ArrayList<Socket> RequireSocketList = new ArrayList<Socket>();//请求队列
    private static ArrayList<ServerUtil> serverList = new ArrayList<ServerUtil>();
    
    //启动服务器
    public static void main(String[] args) {
        new SocketServer();
    }
    
    //服务器功能
    public SocketServer() {
        try {
            server = new ServerSocket(PORT);
            //InetAddress inetAddress = server.getInetAddress();
            //inetAddress.toString();
            //System.out.println(inetAddress);
            executorService = Executors.newCachedThreadPool();  //create a thread pool
            System.out.println("服务器已启动...");
            //Socket client = null;
            while(true) {
            	Socket client = new Socket();
            	client = server.accept();
                //把客户端放入客户端集合中
                RequireSocketList.add(client);
                executorService.execute(new Service(client)); //start a new thread to handle the connection
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    public class Service implements Runnable {
    	private static final int LOGIN_REQUIREMENT = 0;
        private static final int REGISTER_REQUIREMENT = 1;
        private static final int ACTIVATE_ACCOUNT= 2;
        private static final int REVISE_ACCOUNT_INFORMATION_REQUIREMENT = 3;
        private static final int NEW_ACCOUNT_USER = 4;
        private static final int CHANGE_USER_LOCATION = 5;
        private static final int USER_SIGN_OUT = 6;
    	
        //private ArrayList<Socket> RequireSocketList;//请求队列
        //private ArrayList<ServerUtil> serverList;
        private Socket socket;
        private BufferedReader inputStream = null;
        private String message = "";
        private int requireNumber;
        private String account;
    	private int id;
        SendEmailValidateNumber sendEmailValidateNumber;
        
        public Service(Socket socket) {
            this.socket = socket;
            //RequireSocketList = RequireSocketList;
            //serverList = serverList;
            try {
            	inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            	System.out.println("当前已有 " + RequireSocketList.size() + "个客户端连接到服务器。");
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }

        @Override
        public void run() {
        	System.out.println("启动了新线程"+this.toString());
        	while(!this.socket.isClosed()) {
                	try {
                		try{
                			message = inputStream.readLine();
                		}catch(java.net.SocketException e){
                			message = null;
                		}
                		System.out.println("线程"+this.toString()+"处理了一个请求"+"Socket:"+this.socket.toString()+"内容"+message);
    					if(message == null){
    						this.socket.close();
    						ServerUtil server = getServer(account, serverList);
    						server.checkOut(this.id);
    						System.out.println("server"+this.account+"的用户"+this.id+"强制退出了程序，已下线");
    						return;
    		            }
    					String[] pieces = message.split(";");
		            	String reply;
		            	if(pieces.length == 1){
		            		System.out.println("send message:" + message);
		            	}else{
		            		requireNumber = Integer.parseInt(pieces[0]);
    		            	switch(requireNumber){
    		            	case LOGIN_REQUIREMENT:{
    		            		Account account = new Account(pieces[1], pieces[2]);
    		                  	message = DAOService.loginByUsernameAndPassWord(account);
    		                  	this.sendmsg(message);
    		                  	//this.socket.close();
    		                  	break;
    		            	}
    		            	case REGISTER_REQUIREMENT:{
    		            		Account account = new Account(pieces[1], pieces[2],pieces[3]);
    		            		message = DAOService.registerByUsernameAndPasswordAndEmail(account);
    		            		if(message.equals("true")){
    		            			sendEmailValidateNumber = new SendEmailValidateNumber();
    		            			sendEmailValidateNumber.send(account.getEmail());
    		            		}
    		            		message = message + ";" + sendEmailValidateNumber.getValidateNumber();
    		                  	this.sendmsg(message);
    		                  	break;
    		            	}
    		            	case ACTIVATE_ACCOUNT:{
    		            		Account account = new Account(pieces[1]);
    		            		message = DAOService.activateUser(account);
    		            		this.sendmsg(message);
    		            		break;
    		            	}
    		            	case REVISE_ACCOUNT_INFORMATION_REQUIREMENT:{break;}
    		            	case NEW_ACCOUNT_USER:{
    		            		if(pieces.length == 3){
    		        				String account = pieces[1];
    		        				String nickname = pieces[2];
    		        				ServerUtil server = getServer(account, serverList);
    		        				if(server == null){
    		        					server = new ServerUtil(account);
    		        					serverList.add(server);
    		        					System.out.println("新建一个用户server："+account);
    		        				}
    		        				User user = new User(nickname,socket);
    		        				server.login(user);
    		        				this.account = account;
    		        				this.id = user.getId();
    		        				System.out.println("server"+account+"增加了用户："+user.getId());
    		        				server.noticeSibi(user.getId(), nickname);
    		        				reply = "0;"+ user.getId() + server.sibiInfo(user.getId());
    		        			}else{
    		        				reply = "-1;"+"登录请求的格式不正确";
    		        			}
    		            		sendmsg(reply);
    		        			break;
    		    			}
    		            	case CHANGE_USER_LOCATION:{
    		            	
    		            		System.out.println("接收到一个位置变更请求");
    		        			if(pieces.length == 5){
    		        				String account = pieces[1];
    		        				int id = Integer.parseInt(pieces[2]);
    		        				double lat = Double.parseDouble(pieces[3]);
    		        				double lon = Double.parseDouble(pieces[4]);
    		        				ServerUtil server = getServer(account, serverList);
    		        				if(server == null){
    		        					reply = "-1;"+"不存在该用户";
    		        					break;
    		        				}
    		        				server.changeUserStatus(id, lat, lon);
    		        				System.out.println("server"+account+"的用户："+ id + "变更了位置" + lat + "," + lon);
    		        				server.noticeSibi(id, lat, lon);
    		        				reply = "1";
    		        			}else{
    		        				reply = "-1;"+"位置变更请求的格式不正确";
    		        			}
    		        			sendmsg(reply);
    		        			break;
    		            	}
    		            	case USER_SIGN_OUT:{
    		            		this.socket.close();
    		        			if(pieces.length == 3){
    		        				String account = pieces[1];
    		        				int id = Integer.parseInt(pieces[2]);
    		        				ServerUtil server = getServer(account, serverList);
    		        				if(server == null){
    		        					reply = "-1;"+"不存在该用户";
    		        					break;
    		        				}
    		        				server.checkOut(id);
    		        				System.out.println("server"+account+"的用户："+id+"下线");
    		        				server.noticeSibi(id);
    		        				reply = "3";
    		        			}else{
    		        				reply = "-1;"+"下线请求的格式不正确";
    		        			}
    		        			sendmsg(reply);
    		        			
    		        			break;
    		            	}
    		            	default:{
    		            		reply = "-1;"+"无法识别的请求";
    		            		sendmsg(reply);
    		        			break;
    		        		}
    		            	}
		            	}
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
        	}
          }
            
       
      
       public void sendmsg(String message) {
           //for (int index = 0; index < RequireSocketList.size(); index ++) {
               //Socket socket = RequireSocketList.get(index);
               PrintWriter pout = null;
               try {
                   pout = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
                   pout.println(message);
               }catch (IOException e) {
                   e.printStackTrace();
               }
           //}
       }
       /**
        * 循环遍历客户端集合，给每个客户端都发送信息。
        */
       public void sendAllmsg() {
           for (int index = 0; index < RequireSocketList.size(); index ++) {
               Socket socket = RequireSocketList.get(index);
               PrintWriter pout = null;
               try {
                   pout = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
                   pout.println(message);
               }catch (IOException e) {
                   e.printStackTrace();
               }
           }
       }
       
       public ServerUtil getServer(String accountId, ArrayList<ServerUtil> serverList){
    		if(accountId == null){
    			return null;
    		}
    		Iterator<ServerUtil> iter = serverList.iterator();
    		while(iter.hasNext()){
    			ServerUtil temp = iter.next();
    			if(temp.getAccountId().equals(accountId)){
    				return temp;
    			}
    		}
    		return null;
    	}
      
    }    
    
    
    
}