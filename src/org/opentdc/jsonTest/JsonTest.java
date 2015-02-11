package org.opentdc.jsonTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class JsonTest {
	private String[] names = { "[000.000.000] bla000", "[000.000.001] bla001",
			"[000.000.002] bla002", "[000.001.000] bla010",
			"[000.001.001] bla011", "[000.001.002] bla012",
			"[000.002.000] bla020", "[000.002.001] bla021",
			"[000.002.002] bla022", "[000.002.003] bla023",
			"[000.003.000] bla030", "[000.003.001] bla031",
			"[000.003.002] bla032", "[001.000.000] bla100",
			"[001.000.001] bla101", "[001.000.002] bla102",
			"[001.001.000] bla110", "[001.001.001] bla111",
			"[001.001.002] bla112", "[002.002.000] bla220",
			"[002.002.001] bla221", "[002.002.002] bla222",
			"[002.002.003] bla223", "[002.003.000] bla230",
			"[002.003.001] bla231", "[002.003.002] bla232",
			"[005.123.345] blaBig", "[999.999.999] blaBig" };

	private static final int NROF_ELEMS = 10;
	private static final String TESTDATA_FN = "testdata.json";

	public JsonTest() {
		System.out.println("JsonTest instantiated");
	}

	public String[] getNames() {
		return names;
	}

	private Activity importActivity(File f) {
		assertNotNull(f);
		System.out.println("importing Activity from " + f.getName());

		Reader _reader = null;
		Activity _activity = null;
		try {
			_reader = new InputStreamReader(new FileInputStream(f));
			Gson _gson = new GsonBuilder().create();
			_activity = _gson.fromJson(_reader, Activity.class);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} finally {
			try {
				if (_reader != null) {
					_reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return _activity;
	}

	private ArrayList<Activity> importList(File f) {
		assertNotNull(f);
		System.out.println("importing List of Activities from " + f.getName());

		Reader _reader = null;
		ArrayList<Activity> _activities = null;
		try {
			_reader = new InputStreamReader(new FileInputStream(f));
			Gson _gson = new GsonBuilder().create();

			Type _collectionType = new TypeToken<ArrayList<Activity>>() {
			}.getType();
			_activities = _gson.fromJson(_reader, _collectionType);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} finally {
			try {
				if (_reader != null) {
					_reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return _activities;
	}

	private String[] importArrayOfNames(File f) {
		assertNotNull(f);
		System.out.println("importing ArrayOfNames from " + f.getName());

		Reader _reader = null;
		String[] _names = null;
		try {
			_reader = new InputStreamReader(new FileInputStream(f));
			Gson _gson = new GsonBuilder().create();
			_names = _gson.fromJson(_reader, String[].class);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} finally {
			try {
				if (_reader != null) {
					_reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return _names;
	}

	/*
	 * private treeJSON convert(flatJSON) assumption: data is sorted ascending
	 */
	private ArrayList<Activity> convert(ArrayList<Activity> srcActivities,
			boolean isFlat) {
		System.out.println("converting data");
		final String _dummyText = "UNDEFINED";

		int oldIdx1 = -1;
		int oldIdx2 = -1;
		int oldIdx3 = -1;
		int newIdx1 = -1;
		int newIdx2 = -1;
		int newIdx3 = -1;
		Activity _srcActivity = null;
		Activity _destActivity = null;
		ArrayList<Activity> _destActivitiesL1 = new ArrayList<Activity>();
		Activity _lastL1Activity = null;
		Activity _lastL2Activity = null;
		for (int i = 0; i < srcActivities.size(); i++) {
			_srcActivity = (Activity) srcActivities.get(i);
			newIdx1 = new Integer(_srcActivity.getName().substring(1, 4))
					.intValue();
			newIdx2 = new Integer(_srcActivity.getName().substring(5, 8))
					.intValue();
			newIdx3 = new Integer(_srcActivity.getName().substring(9, 12))
					.intValue();

			_destActivity = new Activity();
			_destActivity.setName(_srcActivity.getName().substring(14));
			_destActivity.setId(newIdx1 * 1000000 + newIdx2 * 1000 + newIdx3);

			if (isFlat == true) {
				_destActivitiesL1.add(_destActivity);
			} else {
				if (newIdx1 > oldIdx1) { // level 1 has changed
					if (newIdx2 == 0 && newIdx3 == 0) {
						_lastL1Activity = _destActivity;
						_destActivitiesL1.add(_lastL1Activity);
						_lastL2Activity = null;
						System.out.println(_destActivity.getId() + ": \t"
								+ newIdx1);
					} else {
						_lastL1Activity = new Activity(_dummyText,
								newIdx1 * 1000000);
						_destActivitiesL1.add(_lastL1Activity);
						System.out.println((newIdx1 * 1000000) + ": \t"
								+ newIdx1 + " UNDEFINED");

						if (newIdx3 == 0) {
							_lastL2Activity = _destActivity;
							_lastL1Activity.addActivity(_lastL2Activity);
							System.out.println(_destActivity.getId() + ": \t\t"
									+ newIdx2);

						} else {
							_lastL2Activity = new Activity(_dummyText, newIdx1
									* 1000000 + newIdx2 * 1000);
							_lastL1Activity.addActivity(_lastL2Activity);
							System.out
									.println((newIdx1 * 1000000 + newIdx2 * 1000)
											+ ": \t\t" + newIdx2 + " UNDEFINED");

							_lastL2Activity.addActivity(_destActivity);
							System.out.println(_destActivity.getId()
									+ ": \t\t\t" + newIdx3);
						}
					}
				} else { // level1 unchanged = _lastL1Activity
					if (newIdx2 > oldIdx2) { // level 2 has changed
						if (newIdx3 == 0) {
							_lastL2Activity = _destActivity;
							_lastL1Activity.addActivity(_lastL2Activity);
							System.out.println(_destActivity.getId() + ": \t\t"
									+ newIdx2);
						} else {
							_lastL2Activity = new Activity(_dummyText, newIdx1
									* 1000000 + newIdx2 * 1000);
							_lastL1Activity.addActivity(_lastL2Activity);
							System.out
									.println((newIdx1 * 1000000 + newIdx2 * 1000)
											+ ": \t\t" + newIdx2 + " UNDEFINED");
							_lastL2Activity.addActivity(_destActivity);
							System.out.println(_destActivity.getId()
									+ ": \t\t\t" + newIdx3);
						}
					} else { // level 2 unchanged = _lastL2Activity
						if (_lastL2Activity == null) {
							_lastL2Activity = new Activity(_dummyText, newIdx1
									* 1000000 + newIdx2 * 1000);
							_lastL1Activity.addActivity(_lastL2Activity);
							System.out
									.println((newIdx1 * 1000000 + newIdx2 * 1000)
											+ ": \t\t" + newIdx2 + " UNDEFINED");

						}
						_lastL2Activity.addActivity(_destActivity);
						System.out.println(_destActivity.getId() + ": \t\t\t"
								+ newIdx3);
					}
				}
			}
			oldIdx1 = newIdx1;
			oldIdx2 = newIdx2;
			oldIdx3 = newIdx3;
		}
		return _destActivitiesL1;
	}

	private void print(ArrayList<Activity> data) {
		assertNotNull(data);
		System.out.println("******** ArrayList<Activity>: ");
		for (int i = 0; i < data.size(); i++) {
			System.out.println(data.get(i));

		}
	}

	private void print(String[] data) {
		assertNotNull(data);
		System.out.println("******** String[]: ");
		for (int i = 0; i < data.length; i++) {
			System.out.println(data[i]);
		}
	}

	private void print(Activity activity) {
		assertNotNull(activity);
		System.out.println("******** Activity: ");
		System.out.println(activity.toString());
	}

	private void export(String[] data, File f) {
		assertNotNull(f);
		assertNotNull(data);
		System.out.println("exporting String[] data into " + f.getName());
		Writer _writer = null;
		try {
			_writer = new OutputStreamWriter(new FileOutputStream(f));
			Gson _gson = new GsonBuilder().create();
			_gson.toJson(data, _writer);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			assertNotNull(_writer);
			if (_writer != null) {
				try {
					_writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void export(Activity data, File f) {
		assertNotNull(f);
		assertNotNull(data);
		System.out.println("exporting Activity data into " + f.getName());
		Writer _writer = null;
		try {
			_writer = new OutputStreamWriter(new FileOutputStream(f));
			Gson _gson = new GsonBuilder().create();
			_gson.toJson(data, _writer);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			assertNotNull(_writer);
			if (_writer != null) {
				try {
					_writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void export(ArrayList<Activity> data, File f) {
		assertNotNull(f);
		assertNotNull(data);
		System.out.println("exporting ArrayList<Activity> data into "
				+ f.getName());
		Writer _writer = null;
		try {
			_writer = new OutputStreamWriter(new FileOutputStream(f));
			Gson gson = new GsonBuilder().create();
			gson.toJson(data, _writer);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (_writer != null) {
				try {
					_writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Test
	public void simpleArrayTest() {
		System.out
				.println("******************* simpleArrayTest *********************");
		File _jsonFile = new File("simpleArray.json");
		export(getNames(), _jsonFile);
		String[] _names = importArrayOfNames(_jsonFile);
		print(_names);

		// check the results
		assertNotNull(_names);
		assertEquals(names.length, _names.length);
		for (int i = 0; i < _names.length; i++) {
			assertTrue(String.class.isInstance(_names[i]));
			assertEquals(names[i], _names[i]);
		}
	}

	@Test
	public void simpleActivityTest() {
		System.out
				.println("******************* simpleActivityTest ******************");
		File _jsonFile = new File("simpleActivity.json");
		Activity _activity = new Activity();
		_activity.setId(1);
		_activity.setName("test");
		print(_activity);
		export(_activity, _jsonFile);
		Activity _activity2 = importActivity(_jsonFile);
		print(_activity2);

		// check the results
		assertNotNull(_activity2);
		assertTrue(Activity.class.isInstance(_activity2));
		assertEquals(_activity.getId(), _activity2.getId());
		assertEquals(_activity.getName(), _activity2.getName());
		assertNotNull(_activity.getActivities());
		assertEquals(0, _activity.getActivities().size());
		assertNotNull(_activity2.getActivities());
		assertEquals(0, _activity2.getActivities().size());
	}

	@Test
	public void activityFlatListTest() {
		System.out
				.println("******************* activityFlatListTest ******************");
		File _jsonFile = new File("activityFlatList.json");
		Activity _activity = null;
		ArrayList<Activity> _list = new ArrayList<Activity>();
		for (int i = 0; i < NROF_ELEMS; i++) {
			_activity = new Activity();
			_activity.setId(i);
			_activity.setName("test" + i);
			_list.add(_activity);
		}
		print(_list);
		export(_list, _jsonFile);
		_list = importList(_jsonFile);
		print(_list);

		// check the results
		assertNotNull(_list);
		assertTrue(ArrayList.class.isInstance(_list));
		for (int i = 0; i < NROF_ELEMS; i++) {
			assertEquals(i, _list.get(i).getId());
			assertEquals("test" + i, _list.get(i).getName());
		}

	}

	@Test
	public void convertFlatTest() {
		System.out
				.println("******************* convertFlatTest ******************");
		File _jsonFile = new File(TESTDATA_FN);
		ArrayList<Activity> _srcActivities = importList(_jsonFile);
		ArrayList<Activity> _destActivities = convert(_srcActivities, true);
		export(_destActivities, new File("convertFlatTest.json"));
		print(_destActivities);

		// check the results
		assertNotNull(_srcActivities);
		assertNotNull(_destActivities);
		assertEquals(_srcActivities.size(), _destActivities.size());
		for (int i = 0; i < _srcActivities.size(); i++) {
			assertTrue(Activity.class.isInstance(_srcActivities.get(i)));
			assertTrue(Activity.class.isInstance(_destActivities.get(i)));
			assertEquals(_srcActivities.get(i).getId(), _destActivities.get(i)
					.getId());
			assertTrue(_srcActivities.get(i).getName()
					.endsWith(_destActivities.get(i).getName()));
			assertEquals(_srcActivities.get(i).getName().length() - 14,
					_destActivities.get(i).getName().length());
		}
	}

	@Test
	public void convertHierarchicalTest() {
		System.out
				.println("******************* convertHierarchicalTest ******************");
		File _jsonFile = new File(TESTDATA_FN);
		ArrayList<Activity> _srcActivities = importList(_jsonFile);
		ArrayList<Activity> _destActivities = convert(_srcActivities, false);
		export(_destActivities, new File("convertHierarchicalTest.json"));
		print(_destActivities);

		// check the results
		assertNotNull(_srcActivities);
		assertNotNull(_destActivities);
		// TODO: hierarchical tests
	}

	public static void main(String[] args) {
		System.out
				.println("************** Executing some tests with JSON/Gson ...");
		JsonTest _test = new JsonTest();
		_test.simpleArrayTest();
		_test.simpleActivityTest();
		_test.activityFlatListTest();
		_test.convertFlatTest();
		_test.convertHierarchicalTest();
	}

}
