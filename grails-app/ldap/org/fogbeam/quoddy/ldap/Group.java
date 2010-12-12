package org.fogbeam.quoddy.ldap;

import java.util.ArrayList;
import java.util.List;


public class Group 
{
	public String name;
	public String owner;
	public List<String> memberNames = new ArrayList<String>();
	public List<Person> members = new ArrayList<Person>();
	
}