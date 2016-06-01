package cn.myapp.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

public class User extends Model<User> {
	
	private static final long serialVersionUID = 1L ;
		
	/**
	 * String	 userName ;
	 * String	 password ;
	 * int 		 kindOfJob ; // -1admin 1.subaojiang. 2.xiaoxuzi
	 */	  	
	private Integer		userID ;
	private String 		userName ;
	private String 		password ;
	private Integer		kind ;	
	
	public Integer getUserID() {
		return userID;
	}
	public void setUserID(Integer userID) {
		this.userID = userID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getKind() {
		return kind;
	}
	public void setKind(Integer kind) {
		this.kind = kind;
	}

	// record to user .
	private User transferFromRecord(Record record) {
		User user = new User() ;
		user.userID = record.getInt("userID") ;
		user.userName = record.getStr("userName") ;
		user.password = record.getStr("password") ;
		user.kind = record.getInt("kind") ;		
		return user ;
	}
	
	// construct . new user .
	public User(String userName, String password, Integer kind) {
		super();
		this.userName = userName;
		this.password = password;
		this.kind = kind;
	}
	
	// construct . overwrite super .
	private User() {
		super();		
	}
	
	// dao insert
	public void addin() {
		Record user = new Record().set("userName", this.userName).set("password", this.password).set("kind", this.kind) ;
		Db.save("user", "userID", user) ;
	}
	
	// dao select
	public User selectUserByName(String name) {
		Record record = Db.findById("user", "userName", name) ;
		if (record != null) {
			return transferFromRecord(record) ;	
		}
		return null ;
	}
	
}
