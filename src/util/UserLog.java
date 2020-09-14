package util;

import java.util.HashMap;
import java.util.Map;

public class UserLog {
	
	public Map<String, User> users;
	
	public UserLog() {
		users = new HashMap<String, User>();
	}
	
	public Map<String, User> getMap() {
		return users;
	}
	
	public void putIfAbsent(String userName, User user) {
		users.putIfAbsent(userName, user);
	}
}
