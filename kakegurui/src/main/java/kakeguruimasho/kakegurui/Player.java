package kakeguruimasho.kakegurui;

import java.util.*;
import java.io.*;
import java.net.*;

public class Player {
	
	public static ArrayList<Integer> addCards() {
		Random random = new Random();
		ArrayList<Integer> arr = new ArrayList<>();
		for (int i = 0; i < 8; i++) arr.add(random.nextInt(5));
		return arr;
		
	}

	public static void main(String[] args) throws IOException {
		Socket s = new Socket("localhost", 500);
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		Scanner sc = new Scanner(System.in);
		ArrayList<Integer> arr = addCards();
		Threading thread = new Threading(br);
		
		System.out.println("Welcome to the game!");
		
		System.out.println("Enter your name: ");
		String name = sc.nextLine();
		bw.write(name);;
		bw.newLine();
		bw.flush();
		
		String opponentName = br.readLine();
		thread.inputName(opponentName); 
		
		thread.start();
		
		do {
			
			if (thread.getCount() > 18) {
				System.out.println("You Lose!");
				System.exit(0);
			}
			
			boolean notValid = true;
			
			while (notValid) {
				try {
					System.out.println();
					System.out.println("Your cards are: " + arr);
					System.out.print("Throw a card: ");
					String card = sc.nextLine();
					arr.remove(arr.indexOf(Integer.parseInt(card)));
					thread.setCard(Integer.parseInt(card));
					thread.setCount(thread.getCard() + thread.getCount());
					bw.write(card);;
					bw.newLine();
					bw.flush();
					notValid = false;
				} catch (Exception e) {
					System.out.println("The Card doesn't exist");
					System.out.println("Throw Again");
				}
			}
			
			
			
		} while (true); 
	}

	}

class Threading extends Thread {
	private BufferedReader br = null;
	private int count = 0;
	private int card = 0;
	private String name;

	Threading(BufferedReader br) {this.br = br;}
	
	public void inputName(String name) {this.name = name;}
	
	public String outputName() {return name;}
	
	public void setCard(int card) {this.card = card;}

	public int getCard() {return card;}
	
	public void setCount(int count) {this.count = count;}
	
	public int getCount() {return count;}
	
	@Override
	public void run() {
		try {
			do {
				String opponentCard = br.readLine();
				count += Integer.parseInt(opponentCard);
				System.out.println("\n" + name + " Threw " + opponentCard + ". Total: " + count);
				
				if (count > 18) {
					System.out.println("You Win!");
					System.exit(0);
				}
				
			} while (true);
		} catch (IOException e) {
		}
		
	}
}