package org.opentdc.jsonTest;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;

public class Activity {
	private String name = "undefined";
	private long id = 9999L;
	private ArrayList<Activity> activities = null;

	public Activity() {
		activities = new ArrayList<Activity>();
	}

	public Activity(String newName, long newId) {
		activities = new ArrayList<Activity>();
		name = newName;
		id = newId;
	}

	public String getName() {
		return name;
	}

	public void setName(String newName) {
		name = newName;
	}

	public long getId() {
		return id;
	}

	public void setId(long newId) {
		id = newId;
	}

	public void addActivity(Activity activity) {
		activities.add(activity);
	}

	public ArrayList<Activity> getActivities() {
		return activities;
	}

	public int size() {
		return activities.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder _sb = new StringBuilder();
		Formatter _formatter = new Formatter(_sb, Locale.US);
		_formatter.format("Activity {\n\tid:\t%s\n\tname:\t%s\n}", getId(),
				getName());
		_formatter.close();
		return _sb.toString();
	}
}
