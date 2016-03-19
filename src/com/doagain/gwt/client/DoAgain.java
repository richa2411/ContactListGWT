package com.doagain.gwt.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.Range;

public class DoAgain implements EntryPoint {

	private HorizontalPanel addHpanel = new HorizontalPanel();
	private Button addButton = new Button("Add Contact");
	private Column<Contact, String> editButton = new Column<Contact, String>(
			new ButtonCell()) {
		public String getValue(Contact c) {
			return "Edit";
		}
	};
	private Column<Contact, String> deleteButton = new Column<Contact, String>(
			new ButtonCell()) {
		public String getValue(Contact c) {
			return "Delete";
		}
	};

	public static List<Contact> CONTACTS = new ArrayList<Contact>();
	public static int editIndex;
	public static boolean editflag;

	CellTable<Contact> table = new CellTable<Contact>();
	ListDataProvider<Contact> dataProvider = new ListDataProvider<Contact>();

	DecoratedPopupPanel decoratedPanel = new DecoratedPopupPanel();

	final FormPanel form = new FormPanel();

	final Label addcontact = new Label("Add Contact");
	final Label lbfirstname = new Label("First Name");
	final TextBox tbfirstname = new TextBox();
	final Label lblastname = new Label("Last Name");
	final TextBox tblastname = new TextBox();
	final Label lbjobtitle = new Label("Job Title");
	final TextBox tbjobtitle = new TextBox();
	final Label lbage = new Label("Age");
	final ListBox age = new ListBox();
	VerticalPanel panel;
	final Label lbnickname = new Label("Nickname");
	final TextBox tbnickname = new TextBox();
	final Label lbgroup = new Label("Group");
	final ListBox group = new ListBox();
	final CheckBox checkBoxManager = new CheckBox("Manager");

	public void onModuleLoad() {

		form.setAction("/myFormHandler");
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		
		prepareVerticalPanel();
		
		form.setWidget(panel);
		decoratedPanel.add(form);

		createFormHandlers();

		CONTACTS.addAll(Arrays.asList(new Contact("Aoe", "Gino",
				"Business Analyst", "33", "Gin", "Finance", true), new Contact(
				"Uavier", "Vasta", "Team Lead", "40", "Vastu", "Engineering",
				false), new Contact("Sudy", "Tom", "Product Manager", "25",
				"Tommy", "Management", true)));

		final List<Contact> list = dataProvider.getList();
		for (Contact contact : CONTACTS) {
			list.add(contact);
		}
		dataProvider.setList(CONTACTS);

		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				decoratedPanel.show();
				form.setWidth("800px");
				decoratedPanel.center();
			}
		});

		addHpanel.setBorderWidth(1);
		addHpanel.add(addButton);
		RootPanel.get("doAgain").add(addHpanel);

		TextColumn<Contact> nameCol = new TextColumn<Contact>() {
			public String getValue(Contact object) {
				return object.firstname + " " + object.lastname;
			}
		};
		table.addColumn(nameCol, "Name");
		nameCol.setSortable(true);

		TextColumn<Contact> JobTitle = new TextColumn<Contact>() {
			public String getValue(Contact object) {
				return object.jobtitle;
			}
		};
		table.addColumn(JobTitle, "Job Title");

		TextColumn<Contact> Age = new TextColumn<Contact>() {
			public String getValue(Contact object) {
				return object.age;
			}
		};
		table.addColumn(Age, "Age");

		TextColumn<Contact> Nickname = new TextColumn<Contact>() {
			public String getValue(Contact object) {
				return object.nickname;
			}
		};
		table.addColumn(Nickname, "Nickame");

		TextColumn<Contact> Group = new TextColumn<Contact>() {
			public String getValue(Contact object) {
				return object.group;
			}
		};
		table.addColumn(Group, "Group");

		TextColumn<Contact> Manager = new TextColumn<Contact>() {
			public String getValue(Contact object) {
				return String.valueOf(object.manager ? "yes" : "no");
			}
		};
		table.addColumn(Manager, "Manager");

		table.addColumnStyleName(0, "Color");
		table.addColumnStyleName(1, "Color");
		table.addColumnStyleName(2, "Color");
		table.addColumnStyleName(3, "Color");
		table.addColumnStyleName(4, "Color");
		table.addColumnStyleName(5, "Color");
		table.addColumnStyleName(6, "Color");
		table.addColumnStyleName(7, "Color");

		table.addColumn(deleteButton, "");
		deleteButton.setFieldUpdater(new FieldUpdater<Contact, String>() {
			public void update(int index, Contact object, String value) {

				dataProvider.getList().remove(object);

				dataProvider.refresh();
				table.redraw();

			}
		});

		table.addColumn(editButton, "");
		editButton.setFieldUpdater(new FieldUpdater<Contact, String>() {
			public void update(int index, Contact object, String value) {

				List<Contact> locallist = dataProvider.getList();
				editIndex = locallist.indexOf(object);
				Contact c = locallist.get(editIndex);
				tbfirstname.setValue(c.firstname);
				tblastname.setValue(c.lastname);
				tbjobtitle.setValue(c.jobtitle);
				tbnickname.setValue(c.nickname);
				age.setName(c.age);
				group.setName(c.group);
				checkBoxManager.setValue(c.manager);
				editflag = true;
				decoratedPanel.show();
				decoratedPanel.center();

			}
		});

		dataProvider.addDataDisplay(table);
		table.setRowCount(CONTACTS.size(), true);
		table.setRowData(0, CONTACTS);
		table.setWidth("100%", true);

		AsyncDataProvider<Contact> dataProvider = getDataProvider();

		dataProvider.addDataDisplay(table);

		AsyncHandler columnSortHandler = new AsyncHandler(table);
		table.addColumnSortHandler(columnSortHandler);
		table.getColumnSortList().push(nameCol);
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setBorderWidth(1);
		vPanel.add(table);
		RootPanel.get("doAgain").add(vPanel);

	}

	private AsyncDataProvider<Contact> getDataProvider() {
		return new AsyncDataProvider<Contact>() {
			protected void onRangeChanged(HasData<Contact> display) {
				final Range range = display.getVisibleRange();
				final ColumnSortList sortList = table.getColumnSortList();
				new Timer() {
					public void run() {
						int start = range.getStart();
						int end = start + range.getLength();
						if(end > CONTACTS.size()) {
							end = CONTACTS.size();
						}
						
						Collections.sort(CONTACTS);
						if(!sortList.get(0).isAscending()){
							Collections.reverse(CONTACTS);
						}

						List<Contact> dataInRange = CONTACTS
								.subList(start, end);

						table.setRowData(start, dataInRange);
					}
				}.schedule(500);
			}
		};
	}

	private void createFormHandlers() {
		form.addSubmitHandler(new FormPanel.SubmitHandler() {
			public void onSubmit(SubmitEvent event) {
				if (tbfirstname.getText().length() == 0
						&& tbjobtitle.getText().length() == 0
						&& tblastname.getText().length() == 0) {
					Window.alert("First Name, Last Name & Job Title must not be blank");
					event.cancel();
				} else if (tbfirstname.getText().length() == 0) {
					Window.alert("First Name must not be blank");
					event.cancel();
				} else if (tblastname.getText().length() == 0) {
					Window.alert("Last Name must not be blank");
					event.cancel();
				} else if (tbjobtitle.getText().length() == 0) {
					Window.alert("Job Title must not be blank");
					event.cancel();
				}
			}
		});

		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			public void onSubmitComplete(SubmitCompleteEvent eventt) {

				if (editflag) {
					Contact newContact = new Contact(tbfirstname.getText(),
							tblastname.getText(), tbjobtitle.getText(), age
									.getItemText(age.getSelectedIndex()),
							tbnickname.getText(), group.getItemText(group
									.getSelectedIndex()), checkBoxManager
									.getValue());
					CONTACTS.set(editIndex, newContact);
					decoratedPanel.hide();
					dataProvider.refresh();
					editflag = false;
				}

				else {
					Contact newContact = new Contact(tbfirstname.getText(),
							tblastname.getText(), tbjobtitle.getText(), age
									.getItemText(age.getSelectedIndex()),
							tbnickname.getText(), group.getItemText(group
									.getSelectedIndex()), checkBoxManager
									.getValue());
					CONTACTS.add(newContact);
					dataProvider.setList(CONTACTS);
					tbfirstname.setValue(null);
					tblastname.setValue(null);
					tbjobtitle.setValue(null);
					tbnickname.setValue(null);
					checkBoxManager.setValue(false);
					decoratedPanel.hide();
				}
			}
		});
	}

	private void prepareVerticalPanel() {
		panel = new VerticalPanel();
		panel.setSpacing(7);
		
		addcontact.addStyleName("gwt-Border");
		panel.add(addcontact);
		
		tbfirstname.setWidth("775px");
		panel.add(lbfirstname);
		panel.add(tbfirstname);
		
		tblastname.setWidth("775px");
		panel.add(lblastname);
		panel.add(tblastname);

		
		tbjobtitle.setWidth("775px");
		panel.add(lbjobtitle);
		panel.add(tbjobtitle);
		
		for (int i = 15; i <= 100; i++) {
			age.addItem(Integer.toString(i));
		}
		panel.add(lbage);
		panel.add(age);

		
		tbnickname.setWidth("775px");
		panel.add(lbnickname);
		panel.add(tbnickname);

		
		group.setName("listBoxFormElement");
		group.addItem("Engineering", "item1");
		group.addItem("Finance", "item2");
		group.addItem("Front Office", "item3");
		group.addItem("IT", "item4");
		group.addItem("Management", "item5");
		group.addItem("Marketing", "item6");
		group.addItem("Sales", "item7");
		group.setWidth("220");
		panel.add(lbgroup);
		panel.add(group);

		
		checkBoxManager.setEnabled(true);
		panel.add(checkBoxManager);

		panel.add(new Button("Add", new ClickHandler() {
			public void onClick(ClickEvent event) {
				form.submit();
			}
		}));

		panel.add(new Button("Cancel", new ClickHandler() {
			public void onClick(ClickEvent event) {
				decoratedPanel.hide();

			}
		}));
		
	}

}
