package com.techelevator.tenmo;

import java.util.List;
import java.util.Scanner;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.services.TransferServiceException;
import com.techelevator.tenmo.services.UserService;
import com.techelevator.view.ConsoleService;

public class App {

	private static final String API_BASE_URL = "http://localhost:8080/";

	private static final String MENU_OPTION_EXIT = "Exit";
	private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN,
			MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS,
			MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS,
			MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };

	private AuthenticatedUser currentUser;
	private ConsoleService console;
	private AuthenticationService authenticationService;
	private TransferService transferService;
	private UserService userService;
	private AccountService accountService;

	public static void main(String[] args) {
		App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL),
				new AccountService(), new UserService(), new TransferService());
		app.run();
	}

	public App(ConsoleService console, AuthenticationService authenticationService, AccountService accountService,
			UserService userService, TransferService transferService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.accountService = accountService;
		this.userService = userService;
		this.transferService = transferService;
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");

		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while (true) {

			String choice = (String) console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if (MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if (MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if (MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if (MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if (MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if (MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		int userId = currentUser.getUser().getId();
		Account userAcct = accountService.getAccountbyUserID(userId);
		double currentBalance = userAcct.getBalance();
		System.out.println("Your current account balance is: $ " + currentBalance);
		
		if (currentBalance == 0 ) {
			System.out.println("LOL you're broke.");
		}
	}

	private void viewTransferHistory() {
		boolean transferView = true;
		while (transferView) {
			int userId = currentUser.getUser().getId();
			List<User> users = userService.findAllUsers();
			List<Transfer> transfers = transferService.viewMyTransferHistory(userId);
			System.out.println("TRANSER HISTORY");
			System.out.println("---------------");
			for (Transfer t : transfers) {
				User sender = userService.findById(t.getAccount_from());
				User reciever = userService.findById(t.getAccount_to());

				if (t.getAccount_from() == userId) {
					System.out.println("Transfer ID: " + t.getTransfer_id() + "\t To: " + reciever.getUsername() + "\t"
							+ "$" + t.getAmount());
				} else {
					System.out.println("Transfer ID: " + t.getTransfer_id() + "\t From:: " + sender.getUsername() + "\t"
							+ "$" + t.getAmount());
				}

			}
			if (transfers == null) {
				System.out.println("You haven't made any transfers yet!");
			}
			System.out.println("\nEnter Transfer ID to see transfer details or 0 to exit");
			Scanner scanner = new Scanner(System.in);
			String inputAsString = scanner.nextLine();
			int input = Integer.parseInt(inputAsString);
			if (input > 0) {
				Transfer detailedTransfer = transferService.transferByTransferId(input);
				if (detailedTransfer == null) {
					System.out.println("\nThat was not a valid transfer ID \n");
				} else {
					String type = "";
					if (detailedTransfer.getTransfer_type_id() == 1) {
						type = "Request";
					} else {
						type = "Send";
					}
					String status = "";
					if (detailedTransfer.getTransfer_status_id() == 1) {
						status = "Pending";
					} else if (detailedTransfer.getTransfer_status_id() == 2) {
						status = "Approved";
					} else {
						status = "Rejected";
					}
					System.out.println("\nTRANSFER DETAILS \n" + "---------------- \n" + "Id: "
							+ detailedTransfer.getTransfer_id() + "\n" + "From: " + detailedTransfer.getAccount_from()
							+ "\n" + "To: " + detailedTransfer.getAccount_to() + "\n" + "Type: " + type + "\n"
							+ "Status: " + status + "\n");
					

				}
			} else {
				transferView = false;
				
			}

		}
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub

	}

	private void sendBucks() {
		int fromUserId = currentUser.getUser().getId();
		// List ids and name of people you can send money to
		System.out.println("-------------------------------------------\n " + "Users \n " + "ID \t Name "
				+ "\n-------------------------------------------");
		List<User> users = userService.findAllUsers();
		for (User user : users) {
			if (user.getId() != currentUser.getUser().getId()) {
				System.out.println(user.getId() + "\t" + user.getUsername());
			}
		}
		System.out.println("-------------------------------------------");
		// Enter user to send to OR exit
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter ID of user you are sending to (0 to cancel): ");
		String inputToUserId = scanner.nextLine();
		int toUserId = Integer.parseInt(inputToUserId);
		if (toUserId == 0) {
			exitProgram();
		}
		// Enter how much money to send.
		System.out.println("Enter amount: ");
		String transferInput = scanner.nextLine();
		double transferAmount = Double.parseDouble(transferInput);
		// Execute transfer
		int userId = currentUser.getUser().getId();
		Account userAcct = accountService.getAccountbyUserID(userId);
		if(userAcct.getBalance() >= transferAmount) {
			Transfer result = transferService.sendBucks(fromUserId, transferAmount, toUserId);
			if (result != null) {
				System.out.println("Transfer successful!! Thank you.");
			} else {
				System.out.println("Transfer failed Try again, bub.");
			}
		} else {
			System.out.println("Transfer failed Try again, bub.");
		}
		
	}

	private void requestBucks() {
		// TODO Auto-generated method stub

	}

	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while (!isAuthenticated()) {
			String choice = (String) console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
		while (!isRegistered) // will keep looping until user is registered
		{
			UserCredentials credentials = collectUserCredentials();
			try {
				authenticationService.register(credentials);
				isRegistered = true;
				System.out.println("Registration successful. You can now login.");
			} catch (AuthenticationServiceException e) {
				System.out.println("REGISTRATION ERROR: " + e.getMessage());
				System.out.println("Please attempt to register again.");
			}
		}
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) // will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
			try {
				currentUser = authenticationService.login(credentials);
				AccountService.AUTH_TOKEN = currentUser.getToken();
				UserService.AUTH_TOKEN = currentUser.getToken();
				TransferService.AUTH_TOKEN = currentUser.getToken();
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: " + e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}

	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
