package controller;

import model.Boat;
import model.Member;
import model.Registry;
import view.Console;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


import javax.xml.bind.JAXBException;


public class MemberController {
	private Console c = new Console();
	private Registry memberList = new Registry();
	private File file = new File("Member_Registry.txt");
	private RegistryController rc = new RegistryController();

	public void getInputResult() throws IOException, JAXBException {
		Scanner input = new Scanner(System.in);
		try{
			int selection = input.nextInt();

			switch (selection) {

			case 0:
				rc.saveToRegistry(memberList, file);
				System.exit(0);
				break;

			case 1:
				registerMember(input);
				break;

			case 2:
				updateMember(input);
				break;

			case 3:
				removeMember(input);
				break;

			case 4:
				registerBoat(input);
				break;

			case 5:
				updateBoat(input);
				break;
			case 6:
				removeBoat(input);
				break;

			case 7:
				displaySpecific(input);
				break;

			case 8:
				displayVerbose();
				break;

			case 9:
				displayCompact();
				break;
			
			case 10:
				System.exit(0);

			case 100:
				appStart(c);

			default:

				System.err.println("Wrong input, please choose a number between 0-10 or 100 to display menu \n");
				goBack();

			}
	 	}catch(Exception e) {
   			System.err.println("Wrong input, please choose a number between 0-10 or 100 to display menu");
   		System.out.println(" ");
   		goBack();
  		}
	}

	public void appStart(view.Console view) throws JAXBException, IOException {
		if (!file.exists()) {
			file.createNewFile();
			System.out.println("New File Created!");
		}
		else if (file.length() == 0) {
			view.displayWelcome();
			try {
				getInputResult();
			} catch (IOException e) {
				System.err.println("Please check input!");
			}
		}
		else {
			System.out.println(file.length());
			memberList = rc.loadFromRegistry(memberList, file);
		}
		
		
		view.displayWelcome();
		try {
			getInputResult();
		} catch (IOException e) {
			System.err.println("Please check input!");
		}
	}
	
	//Method for registering member
	public void registerMember(Scanner input) throws JAXBException {
		System.out.print("------------------------------------------"
				+ "\nRegister a new member! (Type 0 to go back) \n"
				+ "\nFirst name of new member: ");
		String temp = input.next(); // Input which user gives
		temp += input.nextLine();
		temp = temp.substring(0, 1).toUpperCase() + temp.substring(1); // Putting first letter to uppercase and the rest to what the user inputs.
		goBackOnDemand(temp);
		if (nameCheckDigit(temp) == false) {
			goBack();
		}

		System.out.print("Last name of new member: "); // Getting a last name for the member too
		String temp2 = input.next();
		temp2 += input.nextLine();
		temp2 = temp2.substring(0, 1).toUpperCase() + temp2.substring(1);
		goBackOnDemand(temp2);
		if (nameCheckDigit(temp2) == false) {
			goBack();
		}

		String memberName = temp + " " + temp2; // Adding the two names together to create one Full name
		goBackOnDemand(memberName);

		System.out.print("Personal number in the form YYMMDD-XXXX: "); // Asking for personal number.
		String memberPersNum = input.next();
		goBackOnDemand(memberPersNum);

		if (persNumCheck(memberPersNum)) {
			Member mem = new Member(memberName, memberPersNum);
			String memberID = mem.createID();
			mem.setId(memberID);
			memberList.addMember(mem);
			
			// If successfull, this will be printed out
			System.out.println("");
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			System.out.println("x Member successfully added to member registry! x");
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			System.out.println("");
			System.out.println(memberList.toString());
			goBack();

		} else { // Otherwise and error will pop out
			persNumErr();
			goBack();
		}
	}

	public void updateMember(Scanner input) throws JAXBException {
		System.out.println("------------------------------------------");
		System.out.println("Update an existing member! (Type 0 to go back)");
		System.out.println("");
		System.out.print("Please enter existing member's ID: ");
		String temp = input.next();
		goBackOnDemand(temp);
		temp = temp.substring(0,1) + temp.substring(1, 2).toUpperCase() + temp.substring(2);
		if (memberList.getRegistry().isEmpty()) {
			System.err.println("There are no members to update, please register a member first!");
			System.out.flush();
			System.err.flush();
			System.out.print("");
			goBack();
		}
		try {
			Member mem = memberList.getRegistry().get(getMemberID(temp));
			System.out.println("");

			System.out.print("Update member first name: ");
			String name = input.next();
			name += input.nextLine();
			name = name.substring(0, 1).toUpperCase() + name.substring(1);
			goBackOnDemand(name);
			if (nameCheckDigit(name) == false) {
				goBack();
			}
			System.out.print("Update member last name: ");
			String name2 = input.next();
			name2 += input.nextLine();
			goBackOnDemand(name2);
			if (nameCheckDigit(name2) == false) {
				goBack();
			}

			String realName = name + " " + name2;

			System.out.print("New member personal number in the form YYMMDD-XXXX: ");
			String persnum = input.next();
			goBackOnDemand(persnum);

			if (persNumCheck(persnum)) {
				mem.setPersNum(persnum);
			} else {
				persNumErr();
				goBack();
			}

			mem.setName(realName);
			System.out.println("");
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			System.out.println("x Member successfully updated! x");
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			goBack();

		} catch (Exception e) {
			System.err.println("A member with that ID was not found, try again!");
			System.out.flush();
			System.err.flush();
			System.out.println("");
			goBack();
		}
	}

	public void removeMember(Scanner input) throws JAXBException {
		if (memberList.getRegistry().isEmpty()) {
			System.err.println("There are no members to remove, please register a member first!");
			System.out.flush();
			System.err.flush();
			System.out.println(" ");
			goBack();
		}
		System.out.println("------------------------------------------");
		System.out.println("Remove a member!");
		System.out.println("");
		System.out.println("Are you sure you want to remove a member?");
		System.out.println("No = 0 , Yes = 1");
		System.out.print("Input: ");
		int text = input.nextInt();
		if (text == 0) {
			goBack();
		} else if (text == 1) {
			System.out.print("Please enter member's ID to remove member: ");
		}
		String temp = input.next();
		temp = temp.substring(0,1) + temp.substring(1, 2).toUpperCase() + temp.substring(2);
		try {
			Member mem = memberList.getRegistry().get(getMemberID(temp));
			memberList.getRegistry().remove(mem);
			System.out.println("");
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			System.out.println("x Member successfully removed :( x");
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			goBack();
		} catch (Exception e) {
			System.err.println("A member with that ID was not found, try again!\n");
			goBack();
		}

	}

	public void registerBoat(Scanner input) throws JAXBException {
		Boat bt = new Boat();

		System.out.println("Register a boat to a member! (Type 0 to go back!)");

		System.out.println("Please input member ID: ");
		String id = input.next();
		goBackOnDemand(id);
		id = id.substring(0,1) + id.substring(1, 2).toUpperCase() + id.substring(2);
		if (memberList.getRegistry().isEmpty()) {
			System.err.println("There are no members in the registry, please register a member first!");
			System.out.println(" ");
			goBack();
		}
		try {
			Member mem = memberList.getRegistry().get(getMemberID(id));
			System.err.println("One word name allowed!");
			System.out.println();
			System.out.println();
			System.out.print("Name of boat: ");
			String boatName = input.next();
			boatName = boatName.substring(0, 1).toUpperCase() + boatName.substring(1);
			goBackOnDemand(boatName);
			checkBoatName(boatName);
			System.out.println("Please choose a boat type:" + "\n1.Sailboat" + "\n2.Motorsailer" + "\n3.Kayak\\Canoe"
					+ "\n4.Other" + "\n");
			System.out.print("Input: ");
			String boatType = input.next();
			goBackOnDemand(boatType);

			if (boatType.equals("1")) {
				boatType = "Sailboat";
			} else if (boatType.equals("2")) {
				boatType = "Motorsailer";
			} else if (boatType.equals("3")) {
				boatType = "Kayak\\Canoe";
			} else if (boatType.equals("4")) {
				boatType = "Other";
			} else {
				System.err.println("Input error, try again!");
				System.out.println("");
				goBack();
			}

			System.out.print("Boat length (in metres): ");
			String boatLength = input.next();
			goBackOnDemand(boatLength);
			if (checkBoatSize(boatLength) == false) {
				goBack();
			}
			bt.setLength(boatLength);
			bt.setType(boatType);
			bt.setName(boatName);
			mem.setBoat(bt);
			mem.setBoats(bt);
			mem.getBoats().stream().forEach(System.out::println);

			int i = mem.getNumOfBoats();
			i++;
			mem.setNumOfBoats(i);

			System.out.println("");
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			System.out.println("x Boat successfully registered! x");
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

			goBack();
		} catch (Exception e) {
			System.err.println("Input error, try again!");
			goBack();
		}
	}
	public void updateBoat(Scanner input) throws JAXBException {
		System.out.println("------------------------------------------");
		System.out.println("Update an existing boat! (Type 0 to go back)");
		System.out.println("");
		System.out.print("Please enter existing member's ID: ");
		String temp = input.next();
		temp = temp.substring(0, 1).toUpperCase() + temp.substring(1);
		goBackOnDemand(temp);

		if (memberList.getRegistry().isEmpty()) {
			System.err.print("There are no members, please register a member first!");
			System.out.flush();
			System.err.flush();
			System.out.print("");
			goBack();
		}
		for (int i = 0; i < memberList.getRegistry().size(); i++) {  // Checking that boat registry for member is not empty
			if (memberList.getRegistry().get(i).getId().equals(temp)) {
				if(memberList.getRegistry().get(i).getBoats().isEmpty()) {
				System.err.print("There are no boats to remove, please register a boat first!");
				System.out.flush();
				System.err.flush();
				System.out.print("");
				goBack();
				}
			}
		}
		System.out.print("Please enter existing boat's name: ");
		String eBoatName = input.next();
		eBoatName = eBoatName.substring(0, 1).toUpperCase() + eBoatName.substring(1); // Changes to capital first letter
		goBackOnDemand(temp);
		for (int i = 0; i < memberList.getRegistry().size(); i++) {
			for (int j = 0; j < memberList.getRegistry().get(i).getBoats().size(); j++) {
				if (memberList.getRegistry().get(i).getBoats().get(j).getName().equals(eBoatName)) {
					try {
						System.out.println("Boat found!");
						System.out.println("");

						System.out.print("Update boat name: ");
						String boatName = input.next();
						goBackOnDemand(boatName);
						checkBoatName(boatName);
						System.out.print("Please choose a new boat type:" + "\n1.Sailboat" + "\n2.Motorsailer"
								+ "\n3.Kayak\\Canoe" + "\n4.Other" + "\n");
						System.out.print("Input: ");
						String boatType = input.next();
						goBackOnDemand(boatType);

						if (boatType.equals("1")) {
							boatType = "Sailboat";
						} else if (boatType.equals("2")) {
							boatType = "Motorsailer";
						} else if (boatType.equals("3")) {
							boatType = "Kayak\\Canoe";
						} else if (boatType.equals("4")) {
							boatType = "Other";
						} else {
							System.err.println("Input error, try again!");
							System.out.println("");
							goBack();
						}
						//Updating Boat Length
						System.out.print("Update boat length: ");
						String boatLength = input.next();
						goBackOnDemand(boatLength);
						if (checkBoatSize(boatLength) == false) {
							goBack();
						}
						memberList.getRegistry().get(i).getBoats().get(j).setName(boatName);
						memberList.getRegistry().get(i).getBoats().get(j).setType(boatType);
						memberList.getRegistry().get(i).getBoats().get(j).setLength(boatLength);
						System.out.println("");
						System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
						System.out.println("x Boat successfully updated! x");
						System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
						goBack();
					} catch (Exception e) {
						System.err.println("Wrong input, try again!");
						System.out.flush();
						System.err.flush();
						System.out.println("");
						goBack();
					}
				}
			}
		}
		System.err.println("Something went wrong, try again!");
		System.out.flush();
		System.err.flush();
		System.out.println("");
		goBack();

	}

	public void removeBoat(Scanner input) throws JAXBException {

		System.out.println("------------------------------------------");
		System.out.println("Delete an existing boat! (Type 0 to go back)");
		System.out.println("");
		System.out.print("Please enter existing member's ID: ");
		String temp = input.next();
		goBackOnDemand(temp);
		temp = temp.substring(0,1) + temp.substring(1, 2).toUpperCase() + temp.substring(2);

		if (memberList.getRegistry().isEmpty()) {
			System.err.print("There are no members, please register a member first!");
			System.out.flush();
			System.err.flush();
			System.out.print("");
			goBack();
		}
		for (int i = 0; i < memberList.getRegistry().size(); i++) { //Checking that the give Members boat registry is not empty. 
			if (memberList.getRegistry().get(i).getId().equals(temp)) {
				if(memberList.getRegistry().get(i).getBoats().isEmpty()) {
				System.err.print("There are no boats to remove, please register a boat first!");
				System.out.flush();
				System.err.flush();
				System.out.print("");
				goBack();
				}
			}
		}
		System.out.print("Please enter existing boat's name: ");
		String eBoatName = input.next();
		goBackOnDemand(temp);
		eBoatName = eBoatName.substring(0, 1).toUpperCase() + eBoatName.substring(1);
		for (int i = 0; i < memberList.getRegistry().size(); i++) {
			for (int j = 0; j < memberList.getRegistry().get(i).getBoats().size(); j++) {	
				if (memberList.getRegistry().get(i).getBoats().get(j).getName().equals(eBoatName)) {	// Checking if the member has such a boat
					try {
						Member mem = memberList.getRegistry().get(getMemberID(temp));
						System.out.println("Boat found!");
						memberList.getRegistry().get(i).getBoats().remove(j);
						
						int q = mem.getNumOfBoats();
						q--;
						mem.setNumOfBoats(q);
						
						System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
						System.out.println("x Boat successfully removed! x");
						System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

						goBack();
					} catch (Exception e) {
						System.err.println("Wrong input, try again!");
						System.out.flush();
						System.err.flush();
						System.out.println("");
						goBack();
					}
				}
			}
		}
		System.err.println("Something went wrong, try again!");
		System.out.flush();
		System.err.flush();
		System.out.println("");
		goBack();

	}

	private Integer getMemberID(String ID) {
		for (int i = 0; i < memberList.getRegistry().size(); i++) {
			if (memberList.getRegistry().get(i).getId().equals(ID)) {
				return i;
			}
		}
		return null; // Should not be return = 0;
	}

	private void persNumErr() {
		System.err.println("Incorrect personal number form, try again!");
		System.out.flush();
		System.err.flush();
		System.out.println("");
	}

	private boolean persNumCheck(String persNum) {

		if (persNum.length() >= 8) {
			if (persNum.substring(6, 7).equals("-") && persNum.length() == 11 && charIsDigit(persNum)) {

				return true;
			}
		}
		return false;
	}
	
	private boolean checkBoatSize(String name) {
		for (int i = 0; i < name.length(); i++) {
			if (!Character.isDigit(name.charAt(i))) {
				System.err.println("The length can only be a positive integer, try again!\n");
				return false;
			}
		}
		return true;
	}	

	private boolean nameCheckDigit(String name) {
		
		for (int i = 0; i < name.length(); i++) {
			if (!Character.isLetter(name.charAt(i))) {
				char temp = name.charAt(i);
				if(temp == ' ') {
					String nameTemp = "\" \" ---> Whitespace";
					System.err.println("You used the character " + nameTemp);
					System.err.println("The name can only contain letters, try again!");
					System.out.println("");
					return false;
				}else {
				System.err.println("You used the character " + "\""+ name.charAt(i) + "\"");
				System.err.println("The name can only contain letters, try again!");
				System.out.println("");
				return false;
				}
			}
		}
		return true;
	}
	
	private void checkBoatName(String name) throws JAXBException {
		for(int j=0; j < memberList.getRegistry().size(); j++) {
			for (int i = 0; i < memberList.getRegistry().get(j).getBoats().size(); i++) { //Checking that the give Members boat registry is not empty. 
				if(memberList.getRegistry().get(j).getBoats().isEmpty()) {
					continue;
				}
				else if (memberList.getRegistry().get(j).getBoats().get(i).getName().equals(name)) {				
					System.err.print("Boat name is taken! Please choose another name.");
					goBack();
				}
			}
		}
	}
	
	private boolean charIsDigit(String temp) {
		for (int i = 0; i < 6; i++) {
			if (!Character.isDigit(temp.charAt(i))) {
				return false;
			}
		}

		for (int j = 7; j < 11; j++) {
			if (!Character.isDigit(temp.charAt(j))) {
				return false;
			}
		}

		return true;
	}

	private void goBackOnDemand(String name) throws JAXBException {
		if (name.equals(Integer.toString(0))) {
			try {
				System.out.println();
				System.out.println("----------------------------");
				System.out.println("Type \"100\" to display menu!");
				System.out.print("Choose from menu by typing a number: ");
				getInputResult();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void goBack() throws JAXBException {
		try {
			System.out.println(" ");
			System.out.println("----------------------------");
			System.out.println("Type \"100\" to display menu!");
			System.out.print("Choose from menu by typing a number: ");
			getInputResult();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	/*Method to display a verbose list of the members in the registry and their boats numbers as well as the boats descriptions */
	private void displayVerbose() throws JAXBException {
		System.out.println("=========== Displaying a verbose list of the members ===========");
		if (memberList.getRegistry().isEmpty()) {
			System.err.println("The Member Registry is currently empty.");
			goBack();
		} else {
			for (int i = 0; i < memberList.getRegistry().size(); i++) {
				System.out.println(memberList.getRegistry().get(i));
				System.out.println(memberList.getRegistry().get(i).getBoats()+ "\n");
				System.out.println(" ");
			}
			goBack();
		}
	}
	/*Method to display a compact list of the members in the registry and their boats numbers */
	private void displayCompact() throws JAXBException {
		System.out.println("=========== Displaying a compact list of the members ===========");
		if (memberList.getRegistry().isEmpty()) {
			System.err.println("The Member Registry is currently empty.");
			goBack();
		} else {
			for (int i = 0; i < memberList.getRegistry().size(); i++) {
				System.out.print("\nMember: " + memberList.getRegistry().get(i).getName() + "\nMember ID: "
						+ memberList.getRegistry().get(i).getId() + "\nNumber of Boats: "
						+ memberList.getRegistry().get(i).getNumOfBoats() + "\n");
			}
			goBack();
		}
	}
	/*Method to display a specific members information */
	private void displaySpecific(Scanner ID) throws JAXBException {
		System.out.println("=================== Displaying specific member =================");
		if (memberList.getRegistry().isEmpty()) {
			System.err.println("The Member Registry is currently empty.");
			goBack();
		} else {
			System.out.println("Please enter member ID!  (Input 0 to go back) ");
			String temp = ID.next();
			goBackOnDemand(temp);
			temp = temp.substring(0,1) + temp.substring(1, 2).toUpperCase() + temp.substring(2);
				Member mem = memberList.getMember(temp);
				if (mem == null) {
					System.err.println("A member with that ID was not found, try again!");
					System.err.flush();
					goBack();
				}
				else {
				System.out.println(mem);
				goBack();
				}
			}
		}

	public ArrayList<Member> getMembers() {
		return memberList.getRegistry();
	}
}
