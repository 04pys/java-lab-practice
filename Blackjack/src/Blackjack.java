import java.util.Random;
import java.util.Scanner;
public class Blackjack {

	static int comNum=0;
	//sex
	public static void printResult(House house, Player player,Computer[] computer) {
		System.out.println("--- Game Results ---");
		house.printStatus();
		if((player.getCardSum()>house.getCardSum()||house.isBust()=="Bust")&&player.isBust()!="Bust") {
			System.out.print("[Win]	");
		}
		else if(player.getCardSum()<house.getCardSum()||player.isBust() == "Bust") {
			System.out.print("[Lose]	");
		}
		else {
			System.out.print("[Draw]	");
		}
		player.printStatus();
		
		for(int i=0;i<comNum;i++) {
			if((computer[i].getCardSum()>house.getCardSum()||house.isBust()=="Bust")&&computer[i].isBust()!="Bust") {
				System.out.print("[Win]	" );
			}
			else if(computer[i].getCardSum()<house.getCardSum()||computer[i].isBust() == "Bust") {
				System.out.print("[Lose]	" );
			}
			else {
				System.out.print("[Draw]	" );
			}
			computer[i].printStatus();
		}
	}
	public static void playHouse(House house,Deck deck) {
		System.out.println("--- House turn ---");
		house.printStatus();
		while(true) {
			if(house.getCardSum()<=16) {
				System.out.println("Hit");
				house.setCard(deck.dealCard());
				house.printStatus();
				if(house.isBust()!="Bust")
					continue;
				else
					break;
			}
			else {
				System.out.println("Stand");
				house.printStatus();
				break;
			}
		}
	}
	public static void playPlayers(Player player, Computer[] computer,Deck deck) {
		Scanner	in = new Scanner(System.in);
		for(int i=-1;i<comNum;i++) {
			System.out.println("--- Player"+(i+2)+" turn ---");
			if(i==-1)
				player.printStatus();
			else
				computer[i].printStatus();
			while(true) {
				
				boolean isHit = false;
					if(i==-1) {
						
						switch(in.nextLine()) {
						case "Hit":
							player.setCard(deck.dealCard());
							player.printStatus();
							isHit = true;
							break;
						case "Stand":
							player.printStatus();
							break;
						}
						//Hit 시에 계속 반복되게 만들기 - 0404
						if(isHit && player.isBust() != "Bust") {
							continue;
						}
						else{
							System.out.println();
							break;
						}
					}
					
					else if(i<comNum) {
						if(computer[i].getCardSum()<14) {
							System.out.println("Hit");
							computer[i].setCard(deck.dealCard());
							computer[i].printStatus();
							isHit = true;
						}
						else if(computer[i].getCardSum()>17) {
							System.out.println("Stand");
							computer[i].printStatus();
						}
						else {
							Random random = new Random();
							int is_hit = (int)(random.nextInt(2));
							if(is_hit==1) {
								System.out.println("Hit");
								computer[i].setCard(deck.dealCard());
								computer[i].printStatus();
								isHit = true;
							}
							else {
								System.out.println("Stand");
								computer[i].printStatus();
							}
							
						}
						if(isHit && computer[i].isBust() != "Bust") {
							continue;
						}
						else{
							System.out.println();
							break;
						}
					}
					
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int seed = Integer.parseInt(args[0]);
		comNum = Integer.parseInt(args[1])-1;
		Deck deck = new Deck();	//Create the deck
		
		deck.shuffle(seed);			//Shuffle the deck
		
		
		/*my code*/
		House house = new House();
		Player player = new Player();
		Computer computer[] = new Computer[4];
		for(int i=0;i<Integer.parseInt(args[1])-1;i++) {
			computer[i] = new Computer();
		}
		//initial setting
		
		for(int i=0;i<2;i++) {
			
			player.setCard(deck.dealCard());
			for(int j=0;j<comNum;j++) {
				computer[j].setCard(deck.dealCard());
			}
			house.setCard(deck.dealCard());
		}
		//printStatus
		System.out.println("House: HIDDEN, "+house.getCard().theValue+house.getCard().theSuit);;
		player.printStatus();
		for(int i=0;i<comNum;i++) {
			computer[i].printStatus();
		}
		
		//isHouse BlackJack
		if(house.getCardSum()==21) {
			printResult(house,player,computer);
		}
		else {
			//turn
			playPlayers(player,computer,deck);
			
			playHouse(house,deck);
			
			System.out.println();
			printResult(house,player,computer);
		}
		
		
	}

}
class Card{
	String[] theSuits = {"c","h","d","s"};
	String theValue;
	String theSuit;
	int isA=11;
	public Card() {}
	public Card(int theValue, int theSuit) {
		switch(theValue) {
		case 0:
			this.theValue = "A";
			break;
		case 10:
			this.theValue = "J";
			break;
		case 11:
			this.theValue = "Q";
			break;
		case 12:
			this.theValue = "K";
			break;
		default:
			this.theValue = String.valueOf(theValue+1);
			break;
		}
		this.theSuit = theSuits[theSuit];
	}
}

class Deck{
	private Card[] deck = new Card[52];
	private int cardsUsed;
	
	public Deck() {
		for(int i=0,idx=0;i<13;i++) {
			for(int j=0;j<4;j++) {
				deck[idx++] = new Card(i,j);
			}
		}
	}
	
	public void shuffle(int seed) {
		Random random = new Random(seed);
		for(int i=deck.length-1;i>0;i--) {
			int rand = (int)(random.nextInt(i+1));
			Card temp = deck[i];
			deck[i] = deck[rand];
			deck[rand] = temp;
		}
		cardsUsed=0;
	}
	public Card dealCard() {
		if(cardsUsed == deck.length)
			throw new IllegalStateException("No cards are left in the deck.");
		cardsUsed++;
		return deck[cardsUsed - 1];
	}
}

//Set of cards in your hand
abstract class Hand{
	Card[] card = new Card[52];
	int cardNum=0;
	int cardSum = 0;
	/*
	 * int sum=0;
	 * for(int i=0;i<myDeck.length;i++){
	 * 	sum+=myDeck[i].Value
	 * }
	 * */
	abstract void printStatus();
	void setCard(Card card) {
		this.card[cardNum++] = card;
	}
	Card getCard() {
		return card[cardNum];
	}
	abstract String isBust();
	int getCardSum() {
		return cardSum;
	}
	int getCardNum() {
		return cardNum;
	}
}

//Player automatically participates
class Computer extends Hand{
	static int playerNumber = 1;
	int myNum;
	Computer(){
		this.myNum = ++playerNumber;
		
	}
	void printStatus() {
		System.out.print("Player"+myNum+": ");
		
		for(int i=0;i<cardNum;i++) {
			System.out.print(card[i].theValue+card[i].theSuit);
			if(i==cardNum-1) {
				System.out.print(" ");
			}
			else System.out.print(", ");
			
		}
		System.out.print("("+cardSum+")");
		if(this.isBust()=="Bust") {
			System.out.println(" - Bust!");
		}
		else System.out.println();
	}
	String isBust() {
		if(cardSum>21) {
			for(int i=0;i<cardNum;i++) {
				if(card[i].theValue == "A"&&card[i].isA==11) {
					cardSum-=10;
				}
			}
			if(cardSum>21)
				return "Bust";
		}
		
		return "No";
	}
	
	void setCard(Card card) {
		this.card[cardNum++] = card;
		switch(card.theValue) {
		case "A":
			cardSum += 11;
			if(cardSum>21) {
				card.isA=1;
				cardSum-=10;
			}
			break;
		case "J","K","Q":
			cardSum+=10;
			break;
		default:
			cardSum+=Integer.parseInt(card.theValue);
			break;
		}
		if(cardSum>21) {
			for(int i=0;i<cardNum;i++) {
				if(this.card[i].theValue == "A") {
					if(this.card[i].isA==11) {
						this.card[i].isA=1;
						cardSum-=10;
					}
				}
			}
		}
	}
	Card getCard() {
		return card[cardNum];
	}
	int getCardSum() {
		return cardSum;
	}
	int getCardNum() {
		return cardNum;
	}
}

//Player you control
class Player extends Hand{
	
	void printStatus() {
		System.out.print("Player1: ");
		for(int i=0;i<cardNum;i++) {
			System.out.print(card[i].theValue+card[i].theSuit);
			if(i==cardNum-1) {
				System.out.print(" ");
			}
			else System.out.print(", ");
			
		}
		if(this.isBust()=="Bust") {
			System.out.println("("+cardSum+") - Bust!");
		}
		else
			System.out.println("("+cardSum+")");
		
	}
	String isBust() {
		if(cardSum>21) {
			for(int i=0;i<cardNum;i++) {
				if(card[i].theValue == "A"&&card[i].isA==11) {
					cardSum-=10;
				}
			}
			if(cardSum>21)
				return "Bust";
		}
		
		return "No";
	}
	void setCard(Card card) {
		this.card[cardNum++] = card;
		switch(card.theValue) {
		case "A":
			cardSum += 11;
			if(cardSum>21) {
				card.isA=1;
				cardSum-=10;
			}
			break;
		case "J","K","Q":
			cardSum+=10;
			break;
		default:
			cardSum+=Integer.parseInt(card.theValue);
			break;
		}
		if(cardSum>21) {
			for(int i=0;i<cardNum;i++) {
				if(this.card[i].theValue == "A") {
					if(this.card[i].isA==11) {
						this.card[i].isA=1;
						cardSum-=10;
					}
				}
			}
		}
	}
	Card getCard() {
		return card[cardNum];
	}
	int getCardSum() {
		return cardSum;
	}
	int getCardNum() {
		return cardNum;
	}
}

class House extends Hand{
	void printStatus() {
		System.out.print("House: ");
		
		for(int i=0;i<cardNum;i++) {
		System.out.print(card[i].theValue+card[i].theSuit);
		if(i==cardNum-1) {
			System.out.print(" ");
		}
		else System.out.print(", ");
		
		}
		if(this.isBust()=="Bust") {
			System.out.println("("+cardSum+") - Bust!");
		}
		else
			System.out.println("("+cardSum+")");
	}
	void setCard(Card card) {
		this.card[cardNum++] = card;
		switch(card.theValue) {
		case "A":
			cardSum += 11;
			if(cardSum>21) {
				card.isA=1;
				cardSum-=10;
			}
			break;
		case "J","K","Q":
			cardSum+=10;
			break;
		default:
			cardSum+=Integer.parseInt(card.theValue);
			break;
		}
		if(cardSum>21) {
			for(int i=0;i<cardNum;i++) {
				if(this.card[i].theValue == "A") {
					if(this.card[i].isA==11) {
						this.card[i].isA=1;
						cardSum-=10;
					}
				}
			}
		}
	}
	String isBust() {
		if(cardSum>21) {
			for(int i=0;i<cardNum;i++) {
				if(card[i].theValue == "A"&&card[i].isA==11) {
					cardSum-=10;
				}
			}
			if(cardSum>21)
				return "Bust";
		}
		
		return "No";
	}
	Card getCard() {
		return card[1];
	}
	int getCardSum() {
		return cardSum;
	}
	int getCardNum() {
		return cardNum;
	}
}

