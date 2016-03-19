
package com.doagain.gwt.client;

public class Contact implements Comparable<Contact> {
	public String lastname;
	public final String firstname;
	public final String jobtitle;
	public final String age;
	public final String nickname;
	public final String group;
	public boolean manager;
	public String delete;

	public Contact(String firstname, String lastname, String jobtitle,
			String age, String nickname, String group, boolean manager) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.jobtitle = jobtitle;
		this.age = age;
		this.nickname = nickname;
		this.group = group;
		this.manager = manager;
	}

	public int compareTo(Contact o) {
		if (this == o) {
			return 0;
		}

		return this.lastname.compareTo(o.lastname);
	}
}
