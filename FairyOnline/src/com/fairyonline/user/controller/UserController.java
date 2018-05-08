package com.fairyonline.user.controller;

import java.io.File;

import java.io.IOException;


import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.fairyonline.user.entity.User;
import com.fairyonline.user.entity.UserLogin;

import com.fairyonline.user.service.UserServiceImpl;




@Controller
@RequestMapping("user")
public class UserController {
	/*
	    private static List<UserLogin> userLoginList;
		
		public UserController() {
			super();
			userLoginList = new ArrayList<UserLogin>();
		}
	*/
		
		@Resource
		private UserServiceImpl userServiceImpl;
		
		@RequestMapping("/userList1")
		public String list(Model model) {
			List<User> list = this.userServiceImpl.listAll();
			model.addAttribute("list",list);
			return "user/userList1"; 
		}
		
		@RequestMapping("/regist1")
		public String regist(String userName)throws IOException{
			List<UserLogin> list = this.userServiceImpl.allUserLogin();
			System.out.println(userName);
			boolean flag = true;
			if(userName != null) {
			for(int i=0;i<list.size();i++) {
				if(list.get(i).getUserName().equals(userName)) {
					flag=false;
					break;
				}
			}
			}else {
				flag=false;
			}
			System.out.println(flag);
			ObjectMapper x = new ObjectMapper();
			String isExist = x.writeValueAsString(flag);
			return isExist;
		}
		
		@RequestMapping("/regist")
		public String userRegist(HttpServletRequest request, HttpServletResponse response){
			String userName = request.getParameter("UserName");
			String passWord = request.getParameter("PassWord");
			List<UserLogin> list = this.userServiceImpl.allUserLogin();
			UserLogin userLogin = new UserLogin();
			userLogin.setUserName(userName);
			userLogin.setPassWord(passWord);
			list.add(userLogin);
			//UserLogin userLogin = new UserLogin("UserName","PassWord");
			//User user = new User("zhangsan","dddfdfd","zhangsan","Ů",userLogin);
			this.userServiceImpl.addUserLogin(userLogin);
			//this.userServiceImpl.addUser(user);
			return "user/personal";
			
			
		} 
		
		@RequestMapping("/login")
		public String userLogin(Model model,HttpServletRequest request,HttpServletResponse response)throws IOException{
			UserLogin userLogin2 = new UserLogin();
			String userName2;
			HttpSession session = request.getSession();
			if(session.getAttribute("userLogin")!=null) {
				session.removeAttribute("userLogin");
			}
			if(session.getAttribute("userLogin")==null) {
				String userName = request.getParameter("UserName");
				String passWord = request.getParameter("PassWord");
				UserLogin userLogin = this.userServiceImpl.login(userName,passWord);
				userLogin2 = userLogin;
				userName2 = userName;
			}else {
				String userName=(String)session.getAttribute("userLogin");
				UserLogin userLogin = this.userServiceImpl.findUserLogin(userName);
				userLogin2 = userLogin;
				userName2 = userName;
			}
			if(userLogin2!=null) {
				session.setAttribute("userLogin",userName2);
				model.addAttribute("admin",userName2);
				System.out.println("loginִ�гɹ�");
				return "user/index";
			}else {
				model.addAttribute("errormsg","�û������������");
				return "user/login";
			}
			
		}
		
		
		@RequestMapping(value="/updateitem",method= {RequestMethod.POST,RequestMethod.GET})
		public String updateItems(MultipartFile picture, String UserName,HttpServletRequest request,HttpServletResponse response) throws Exception {
			String userName = request.getParameter("UserName");
			List<UserLogin> list = this.userServiceImpl.allUserLogin();
			if(userName != null) {
			for(int i=0;i<list.size();i++) {
				if(list.get(i).getUserName().equals(userName)) {
					String PetName = request.getParameter("PetName");
					String Sex = request.getParameter("Sex");
					String Img = request.getParameter("Img");
					String TName = request.getParameter("TName");
					UserLogin userLogin = this.userServiceImpl.findUserLogin(userName);
					List<User> list1 = this.userServiceImpl.listAll();
					
					User user = new User();
					user.setPetName(PetName);
					//user.setImg(Img);
					user.setSex(Sex);
					user.setTName(TName);
					user.setUserLogin(userLogin);
					
					
					
					// �����ϴ��ĵ���ͼƬ    
				    String originalFileName = picture.getOriginalFilename();// ԭʼ����
				    // �ϴ�ͼƬ
				    if (picture != null && originalFileName != null && originalFileName.length() > 0) {
				    	 System.out.println("get add imgs  success");
				    	 String pic_path = "E:\\temp\\images\\";
				    	 String newFileName = UUID.randomUUID()
				                 + originalFileName.substring(originalFileName
				                         .lastIndexOf("."));     
				         File newFile = new File(pic_path + newFileName);//��ͼƬ
				         picture.transferTo(newFile);// ���ڴ��е�����д�����
				         user.setImg(newFileName);// ����ͼƬ����д��itemsCustom��
				    }else {
				    	System.out.println("else  success");
				    	//����û�û��ѡ��ͼƬ���ϴ��ˣ�����ԭ����ͼƬ
				       // User temp = this.userServiceImpl.findUserById(user.getID());
				       // user.setImg(temp.getImg());
				    }
				    list1.add(user);
				    
				    String userName1 = userLogin.getUserName();
				    User user1 = this.userServiceImpl.findUser(userName1);
				    if(user1 != null) {
				    	this.userServiceImpl.updateUser(user);
				    }
				  
					this.userServiceImpl.addUser(user); 
				}
			  }
			
			   
		   }
			 return "user/index";
			/*User items = new User("PetName","TName","Sex");
			this.userServiceImpl.addupUser(items);
			 
		   
		   }*/
       
		


		}
}