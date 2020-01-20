import java.io.*;
import java.util.*;

class Log_In {
	String ID;
	String PW;
	
	public int logWindow() { // �α���â ���� �� �Է�
		System.out.println("======================================");
		Scanner mSC = new Scanner(System.in);
		System.out.print("ID(-1�� �Է��ϸ� �ý����� ����˴ϴ�.): ");
		ID = mSC.nextLine();
		System.out.print("��й�ȣ: ");
		PW = mSC.nextLine();
		
		if(ID.equals("-1")) return -1;
		else return 1;
	}
	
	public Member_Node isCustomer(Member_List mList, Search_Account sa) { // �� ���� Ȯ��
		Member_Node temp = sa.search(mList, ID, PW);
		if(temp == null) {
			System.out.println("������ �������� �ʽ��ϴ�.");
			return null;
		}
		else {
			if(!PW.equals(temp.getPW())) {
				System.out.println("���� ������ ��ġ���� �ʽ��ϴ�.");
				return null;
			}
		}
		return temp;
	}
	
	public boolean isMart(Mart_Option mo) { // ��Ʈ ���� Ȯ��
		if(ID.equals(mo.getID()) && PW.equals(mo.getPW())) return true;
		else return false;
	}
}

class Customer_Option { // ���� ������ ����,  Use case�� ���õ� �޼ҵ带 ����
	Member_Node account; // �α��ε� Member_Node�� ��ġ ����
	
	public void showOption() { // ��� �� �� �ִ� Option���� ���
		System.out.println("======================================");
		System.out.println("Customer Option(-1�� �Է��ϸ� �α׾ƿ��� �˴ϴ�.)");
		System.out.println("1. ���� ����");
		System.out.println("2. ��ǰ �˻�");
		System.out.println("3. ��ǰ ����");
		System.out.println("4. ��ǰ �ֹ�");
		System.out.println("5. �ֹ� Ȯ��");
	}
	
	public void changePW() { // ��й�ȣ ����
		Scanner mSC = new Scanner(System.in);
		System.out.print("������ ��й�ȣ: ");
		String PW = mSC.nextLine();
		
		account.setPW(PW);
	}
	
	public int buyGoods(Goods_List gList, String name) { // ��ǰ ����
		Goods_Node node = showInventory(gList, name); // �˻��� ��ǰ�� Ž��
		if(node == null) {
			System.out.println("�˻��� ��ǰ�� �����ϴ�.");
			return 0; // �˻��� ��ǰ�� ���ٴ� ��� ��ȯ, �ɼ� ���� ȭ������
		}
		
		int quantity;
		int point;
		if(node.getQuantity() > 0) { // ������ �ִ� ���
			Scanner gSC = new Scanner(System.in);
			while(true) {
				System.out.print("������ ����(-1�� �Է��ϸ� ��ǰ ���ŷ� ���ư��ϴ�.): ");
				quantity = gSC.nextInt();
				
				if(quantity <= 0) return -1; // ���Ÿ� ����Ѵٴ� ��� ��ȯ, �˻� ȭ������
				if(quantity > node.getQuantity()) System.out.println("��ǰ ������ ������� �ʽ��ϴ�.");
				else break;				
			}
			while(true) {
				System.out.print("����� ����Ʈ: ");
				point = gSC.nextInt();
				gSC.nextLine(); // ���� ���� �����
				
				if(point > account.getPoint() || point <= -1) System.out.println("�������� ����Ʈ ���� �ƴմϴ�.");
				else {
					account.setPoint(account.getPoint() - point); // ����Ʈ ����
					account.setPoint(account.getPoint() + (int)(node.getPrice() * quantity * 0.03)); // ����Ʈ ����
					account.setPayment(account.getPayment() + node.getPrice() * quantity); // �� ������ ����
					node.setQuantity(node.getQuantity() - quantity); // ���� ����
					System.out.println("��ǰ ���ſ� �����߽��ϴ�.");
					break;
				}
			}
		}
		else System.out.println("�����Ϸ��� ��ǰ�� ������ �����ϴ�."); // ������ ���� ���
		return 1; // ��������  ��� ��ȯ, �ɼ� ���� ȭ������
	}
	
	public int orderGoods(Delivery_List dList, Goods_List gList, String name) { // ��ǰ �ֹ�
		Goods_Node node = showInventory(gList, name); // �˻��� ��ǰ�� Ž��
		if(node == null) {
			System.out.println("�˻��� ��ǰ�� �����ϴ�.");
			return 0; // �˻��� ��ǰ�� ���ٴ� ��� ��ȯ, �ɼ� ���� ȭ������
		}
		
		int quantity;
		int point;
		if(node.getQuantity() > 0) {
			Scanner gSC = new Scanner(System.in);
			while(true) {
				System.out.print("������ ����: ");
				quantity = gSC.nextInt();
				
				if(quantity <= 0) return -1; // ���Ÿ� ����Ѵٴ� ��� ��ȯ, �˻� ȭ������
				if(quantity > node.getQuantity()) System.out.println("��ǰ ������ ������� �ʽ��ϴ�.");
				else break;				
			}
			while(true) {
				System.out.print("����� ����Ʈ: ");
				point = gSC.nextInt();
				gSC.nextLine(); // ���� ���� �����
				
				if(point > account.getPoint() || point <= -1) System.out.println("�������� ����Ʈ ���� �ƴմϴ�.");
				else {
					dList.add(account.getID(), node.getName(), quantity, quantity * node.getPrice());
					account.setPoint(account.getPoint() - point); // ����Ʈ ����
					account.setPoint(account.getPoint() + (int)(node.getPrice() * 0.05)); // ����Ʈ ����
					account.setPayment(account.getPayment() + node.getPrice() * quantity); // �� ������ ����
					node.setQuantity(node.getQuantity() - quantity);
					System.out.println("��ǰ �ֹ��� �����߽��ϴ�.");
					break;
				}
			}
		}
		else System.out.println("�����Ϸ��� ��ǰ�� ������ �����ϴ�."); // ������ ���� ���
		return 1; // �������� ��� ��ȯ, �ɼ� ���� ȭ������
	}
	
	public Goods_Node showInventory(Goods_List gList, String name) { return gList.search(name); }
	public void checkDeliveryOrder(Delivery_List dList) { dList.search(account.getID()); }
}

class Mart_Option { // Mart ������ ����,  Use case�� ���õ� �޼ҵ带 ����
	private String ID = "administrator";
	private String PW = "temppassword";
	
	public void showOption() { // ��� �� �� �ִ� Option���� ���
		System.out.println("======================================");
		System.out.println("Mart Option(-1�� �Է��ϸ� �α׾ƿ��� �˴ϴ�.)");
		System.out.println("1. ȸ�� ����");
		System.out.println("2. ��ǰ �˻�");
		System.out.println("3. ��ǰ ����");
		System.out.println("4. �ֹ� ����");
		System.out.println("5. �ֹ� ���");
	}
	
	String getID() { return this.ID; }
	String getPW() { return this.PW; }
	
	void makeNewMember(Member_List mList, String ID) { // �� ���� �߰�
		Scanner mSC = new Scanner(System.in);
		System.out.print("������ ��й�ȣ: ");
		String PW = mSC.nextLine();
		
		mList.add(ID, PW, 0, 0);
	}
	
	void deleteMember(Member_List mList, String ID) { mList.delete(ID); } // ���� ����
	
	Goods_Node showInventory(Goods_List gList, String name) { return gList.search(name); } // ��ǰ �˻�
	
	void addNewGoods(Goods_List gList) { // �� ��ǰ �߰�
		Scanner gSC = new Scanner(System.in);
		
		System.out.print("��ǰ��: ");
		String name = gSC.nextLine();
		System.out.print("����: ");
		int price = gSC.nextInt();
		System.out.print("����: ");
		int quantity = gSC.nextInt();
		
		gList.add(name, price, quantity);
	}
	
	void manageDeliveries(Delivery_List dList) { // �ֹ� �Ϸ� ��Ű��
		Scanner dSC = new Scanner(System.in);
		
		System.out.print("�Ϸ��� �ֹ� ��: ");
		int N = dSC.nextInt();
		
		dList.delete(N);
	}
	
	void checkAllDeliveries(Delivery_List dList) { dList.show(); } // �ֹ� ���� ���
}

class Search_Account {// Member_List���� ���ϴ� Member_Node�� �˻� �� ��ȯ
	Member_Node x;
	
	public Member_Node search(Member_List mList, String ID, String PW) {
		for(x = mList.first; x != null; x = x.next) { // ������ ��ġ Ž��
			String tempID = x.getID();
			
			if(ID.equals(tempID)) return x;
		}
		return null;
	}
}

class Member_List { // Member_Node�� ����Ǵ� ���Ḯ��Ʈ
	Member_Node first;
	private int N = 0; // �� ����� ��
	
	public void add(String ID, String PW, int point, int payment) { // ID�� ������������ ����
		Member_Node newNode = new Member_Node(ID, PW, point, payment, null);
		N++;
		
		if(first == null) { // List�� ����� ���
			first = newNode;
			return;
		}
		else if(ID.compareTo(first.getID()) < 0) { // �߰��� ID�� ����Ʈ�� ù ID���� ���� ���� ���
			newNode.next = first;
			first = newNode;
			return;
		}
		else { // �� �� �ƴϸ� ����Ʈ�� Ž���ؼ� �� ��ġ�� ã��
			Member_Node temp = first;
			
			while(temp.next != null) {
				if(ID.compareTo(temp.next.getID()) < 0) {
					newNode.next = temp.next;
					temp.next = newNode;
					return;
				}
				temp = temp.next;
			}
			temp.next = newNode;
		}
	}
	
	public void delete(String ID) {
		if(ID.equals(first.getID())) { // ù���� ��带 ����
			first = first.next;	N--;
			return;
		}
		for(Member_Node x = first; x.next != null; x = x.next) { // ������ ��� Ž��
			if(ID.equals(x.next.getID())) {
				x.next = x.next.next; N--;
				return;
			}
		}
	}
	
	public int size() { return N; } // ����� ��� �� ��ȯ
}

class Goods_List { // Goods_List�� ����Ǵ� ���Ḯ��Ʈ
	Goods_Node first;
	private int N = 0;
	
	public Goods_Node search(String name) { // ���ϴ� Goods_Node�� ������ ��� �� ��ȯ
		for(Goods_Node x = first; x != null; x = x.next) {
			if(name.equals(x.getName())) {
				System.out.println("��ǰ��: " + x.getName());
				System.out.println("����: " + x.getPrice());
				System.out.println("����: " + x.getQuantity());
				return x;
			}
		}
		return null;
	}
	
	public void add(String name, int price, int quantity) { // ��ǰ���� ������������ ����
		Goods_Node newNode = new Goods_Node(name, price, quantity, null);
		N++;
		
		if(first == null) { // List�� ����� ���
			first = newNode;
			return;
		}
		else if(name.compareTo(first.getName()) < 0) { // �߰��� ��ǰ���� ����Ʈ�� ù ��ǰ���� ���� ���� ���
			newNode.next = first;
			first = newNode;
			return;
		}
		else { // �� �� �ƴϸ� ����Ʈ�� Ž���ؼ� �� ��ġ�� ã��
			Goods_Node temp = first;
			
			while(temp.next != null) {
				if(name.compareTo(temp.next.getName()) < 0) {
					newNode.next = temp.next;
					temp.next = newNode;
					return;
				}
				temp = temp.next;
			}
			temp.next = newNode;
		}
	}
	
	public void delete(String name) {
		if(name.equals(first.getName())) { // ù���� ��带 ����
			first = first.next;	N--;
			return;
		}
		for(Goods_Node x = first; x.next != null; x = x.next) { // ������ ��� Ž��
			if(name.equals(x.next.getName())) {
				x.next = x.next.next; N--;
				return;
			}
		}
	}
	
	public int size() { return N; } // ����� ��� �� ��ȯ
}

class Delivery_List { // Delivery_List�� ����Ǵ� �迭����Ʈ
	ArrayList<Delivery_Node> dList = new ArrayList<Delivery_Node>();
	private int N = 0;
	
	public void show() {
		Iterator<Delivery_Node> it = dList.iterator();
		
		if(N < 10) { // ����� ��尡 10�� �̸��� ��� ��ü�� ���
			while(it.hasNext()) {
				Delivery_Node node = it.next();
				System.out.println("�ֹ���: " + node.getBuyer());
				System.out.println("�ֹ� ��ǰ: " + node.getName());
				System.out.println("�ֹ� ����: " + node.getQuantity());
				System.out.println("�� ����: " + node.getPrice());
				System.out.println();
			}
		}
		else { // ����� ��尡 10�� �̻��� ��� 10���� ���
			for(int i = 0; i < 10; i++) {
				Delivery_Node node = it.next();
				System.out.println("�ֹ���: " + node.getBuyer());
				System.out.println("�ֹ� ��ǰ: " + node.getName());
				System.out.println("�ֹ� ����: " + node.getQuantity());
				System.out.println("�� ����: " + node.getPrice());
				System.out.println();
			}
		}
	}
	
	public void search(String buyer) { // �ֹ��ڰ� ��ġ�ϴ� ����� ������ ���
		Iterator<Delivery_Node> it = dList.iterator();
		
		while(it.hasNext()) {
			Delivery_Node node = it.next();
			if(!buyer.equals(node.getBuyer())) continue;
			System.out.println("�ֹ���: " + node.getBuyer());
			System.out.println("�ֹ� ��ǰ: " + node.getName());
			System.out.println("�ֹ� ����: " + node.getQuantity());
			System.out.println("�� ����: " + node.getPrice());
			System.out.println();
		}
	}
	
	public void add(String buyer, String name, int quantity, int price) { // �ð������� ����
		Delivery_Node newNode = new Delivery_Node(buyer, name, quantity, price);
		dList.add(newNode);
		this.N++;
	}
	
	public void delete(int N) { // N�� ��ŭ ���� �տ� �ִ� ��带 ����
		for(int i = 0; i < N; i++) {
			dList.remove(0);
			this.N--;
		}
	}
	
	public int size() { return N; } // ����� ��� �� ��ȯ
}

class Member_Node {
	private String ID;
	private String PW;
	private int point = 0;
	private int payment = 0;
	Member_Node next;
	
	public Member_Node(String ID, String PW, int point, int payment, Member_Node next) {
		this.ID = ID;
		this.PW = PW;
		this.point = point;
		this.payment = payment;
		this.next = next;
	}
	
	public void setID(String ID) { this.ID = ID; }
	public void setPW(String PW) { this.PW = PW; }
	public void setPoint(int point) { this.point = point; }
	public void setPayment(int payment) { this.payment = payment; }
	
	public String getID() { return this.ID; }
	public String getPW() { return this.PW; }
	public int getPoint() { return this.point; }
	public int getPayment() { return this.payment; }
}

class Goods_Node {
	private String name;
	private int price;
	private int quantity;
	Goods_Node next;
	
	public Goods_Node(String name, int price, int quantity, Goods_Node next) {
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.next = next;
	}
	
	public void setName(String name) { this.name = name; }
	public void setPrice(int price) { this.price = price; }
	public void setQuantity(int quantity) { this.quantity = quantity; }
	
	public String getName() { return this.name; }
	public int getPrice() { return this.price; }
	public int getQuantity() { return this.quantity; }
}

class Delivery_Node {
	private String buyer;
	private String name;
	private int quantity;
	private int price;
	
	public Delivery_Node(String buyer, String name, int quantity, int price) {
		this.buyer = buyer;
		this.name = name;
		this.quantity = quantity;
		this.price = price;
	}
	
	public void setBuyer(String buyer) { this.buyer = buyer; }
	public void setName(String name) { this.name = name; }
	public void setQuantity(int quantity) { this.quantity = quantity; }
	public void setPrice(int price) { this.price = price; }
	
	public String getBuyer() { return this.buyer; }
	public String getName() { return this.name; }
	public int getQuantity() { return this.quantity; }
	public int getPrice() { return this.price; }
}

public class SWProject {
	public static void main(String[] args) {
		Log_In logIn = new Log_In(); Search_Account sa = new Search_Account();
		Customer_Option co = new Customer_Option(); Mart_Option mo = new Mart_Option();
		Member_List mList = new Member_List(); Goods_List gList = new Goods_List(); Delivery_List dList = new Delivery_List();
		
		int mode = 0, choose; // mode�� ��Ʈ�� 1, ���� 2�� ����
		int isLogOut = 0; // �α׾ƿ� �÷���
		Scanner sc = new Scanner(System.in);
		
		try { // ���� ������ �����͸� �о���� �۾�
			StringTokenizer stok; // �����͸� �������� �и��ϱ� ����
			String str; // �� �� �� �о��
			BufferedReader reader = new BufferedReader(new FileReader("mList.txt")); // Member_List�� �����͸� �о��
			while((str = reader.readLine()) != null) {
				if(!str.equals("")) {
					stok = new StringTokenizer(str); // �����͸� �������� �и�
					while(stok.hasMoreTokens()) {
						String ID = stok.nextToken().toString();
						String PW = stok.nextToken().toString();
						int point = Integer.parseInt(stok.nextToken().toString());
						int payment = Integer.parseInt(stok.nextToken().toString());
						mList.add(ID, PW, point, payment); // Member_List�� �ϳ��� ����
					}
				}
			}
			
			reader = new BufferedReader(new FileReader("gList.txt")); // Goods_List�� �����͸� �о��
			while((str = reader.readLine()) != null) {
				if(!str.equals("")) {
					stok = new StringTokenizer(str); // �����͸� �������� �и�
					while(stok.hasMoreTokens()) {
						String name = stok.nextToken().toString();
						int price = Integer.parseInt(stok.nextToken().toString());
						int quantity = Integer.parseInt(stok.nextToken().toString());
						gList.add(name, price, quantity);
					}
				}
			}
			
			reader = new BufferedReader(new FileReader("dList.txt")); // Delivery_List�� �����͸� �о��
			while((str = reader.readLine()) != null) {
				if(!str.equals("")) {
					stok = new StringTokenizer(str); // �����͸� �������� �и�
					while(stok.hasMoreTokens()) {
						String buyer = stok.nextToken().toString();
						String name = stok.nextToken().toString();
						int quantity = Integer.parseInt(stok.nextToken().toString());
						int price = Integer.parseInt(stok.nextToken().toString());
						dList.add(buyer, name, quantity, price);
					}
				}
			}
			reader.close();
		} catch (FileNotFoundException fnfe) {
			System.out.println("No File");
		} catch (IOException ioe) {
			System.out.println("Can't Read File");
		}
		
		while(true) { // console�� ������ �� ���� �ݺ�
			int isQuit = logIn.logWindow(); // �α��� ������ �� ���� �ݺ�
			if(isQuit == -1) { // �α��� â���� �ý����� ����
				System.out.println("�ý����� ����˴ϴ�.");
				break;
			}
			
			if(logIn.isMart(mo)) {
				mode = 1; // ��Ʈ ���� �α��� ����
				System.out.println("Administrator���� �α��� �Ͽ����ϴ�.");
			}
			else {
				co.account = logIn.isCustomer(mList, sa);
				if(co.account != null) {
					System.out.println(co.account.getID() + "���� �α��� �Ͽ����ϴ�.");
					mode = 2; // �� ���� �α��� ����
				}
				else continue;
			}
			
			if(mode == 1) { // ��Ʈ ���
				while(true) {
					mo.showOption();
					System.out.print("�Է�: "); // �ɼ� �Է�
					choose = sc.nextInt();
					sc.nextLine(); // ���� ���� �����
					
					switch(choose) {
						case -1: // �α׾ƿ�
							System.out.println("Administrator���� �α׾ƿ� �Ͽ����ϴ�.");
							isLogOut = 1;
							break;
						case 1: { // ȸ�� ����
							System.out.println("======================================");
							System.out.println("[ ȸ�� ���� ]");
							System.out.print("���� �Է�: "); // ���� �˻�
							String acID = sc.nextLine();
							
							Member_Node mem = sa.search(mList, acID, null);
							if(mem == null) { // ������ ���� ���
								System.out.print("������ �����ϴ�. �� ������ ����ðڽ��ϱ�(Y/N): ");
								String ch = sc.nextLine();
								if(ch.equals("Y")) {
									System.out.print("��й�ȣ �Է�: ");
									String acPW = sc.nextLine();
									mList.add(acID, acPW, 0, 0);
									System.out.println("�� ������ �����Ǿ����ϴ�.");
									break;
								}
								else break;
							}
							else {
								System.out.println("ID: " + mem.getID());
								System.out.println("����Ʈ: " + mem.getPoint());
								System.out.println("�� ������: " + mem.getPayment());
								System.out.print("������ �����Ͻðڽ��ϱ�(Y/N): ");
								String ch = sc.nextLine();
								if(ch.equals("Y")) {
									mList.delete(mem.getID());
									System.out.println("������ �����Ǿ����ϴ�.");
									break;
								}
								else break;
							}
						}
						case 2: { // ��ǰ �˻�
							System.out.println("======================================");
							System.out.println("[ ��ǰ �˻� ]");
							System.out.print("��ǰ�� �Է�: ");
							String gName = sc.nextLine();
							Goods_Node goods = mo.showInventory(gList, gName);
							if(goods == null) System.out.println("�˻��� ��ǰ�� �����ϴ�");
							break;
						}
						case 3: { // ��ǰ ����
							System.out.println("======================================");
							System.out.println("[ ��ǰ ���� ]");
							System.out.print("��ǰ�� �Է�: ");
							String gName = sc.nextLine();
							Goods_Node goods = mo.showInventory(gList, gName);
							if(goods == null) {
								System.out.print("�˻��Ͻ� ��ǰ�� �����ϴ�. ��ǰ�� �߰��Ͻðڽ��ϱ�(Y/N): ");
								String ch = sc.nextLine();
								if(ch.equals("Y")) {
									System.out.print("���� �Է�: ");
									int gPrice = sc.nextInt();
									sc.hasNextLine(); // ���� ���� �����
									System.out.print("���� �Է�: ");
									int gQuantity = sc.nextInt();
									sc.hasNextLine(); // ���� ���� �����
									gList.add(gName, gPrice, gQuantity);
									System.out.println("�� ��ǰ�� �߰��Ǿ����ϴ�.");
									break;
								}
								else break;
							}
							else {
								System.out.print("�� ��ǰ�� �Է�: ");
								String newName = sc.nextLine();
								System.out.print("�� ���� �Է�: ");
								int newPrice = sc.nextInt();
								sc.hasNextLine(); // ���� ���� �����
								System.out.print("�� ���� �Է�: ");
								int newQuantity = sc.nextInt();
								sc.hasNextLine(); // ���� ���� �����
								goods.setName(newName);
								goods.setPrice(newPrice);
								goods.setQuantity(newQuantity);
								System.out.println("�� ������ �ݿ��Ǿ����ϴ�.");
								break;
							}
						}
						case 4 : { // �ֹ� ����
							int num;
							while(true) {
								System.out.print("�Ϸ��� �ֹ��� ���� �Է��ϼ���: ");
								num = sc.nextInt();
								sc.hasNextLine(); // ���� ���� �����
								if(num > 0) break;
								System.out.println("�������� ���� �Է��ϼ���.");
							}
							if(num < dList.size()) dList.delete(num);
							else dList.delete(dList.size());
							System.out.println(num + "���� �ֹ��� �Ϸ�Ǿ����ϴ�.");
							break;
						}
						case 5 : { // �ֹ� ���
							dList.show();
							break;
						}
					}
					if(isLogOut == 1) {
						isLogOut = 0; // �α׾ƿ� ���� �ٽ� �ʱ�ȭ
						mode = 0; // ��� �ʱ�ȭ
						break;
					}
				}
			}
			else if(mode == 2) { // �� ���
				while(true) {
					co.showOption();
					System.out.print("�Է�: "); // �ɼ� �Է�
					choose = sc.nextInt();
					sc.nextLine(); // ���� ���� �����
					
					switch(choose) {
						case -1: // �α׾ƿ�
							System.out.println(co.account.getID() + "���� �α׾ƿ� �Ͽ����ϴ�.");
							isLogOut = 1;
							break;
						case 1: { // ���� ����
							System.out.println("======================================");
							System.out.println("[ ���� ���� ]");
							System.out.println("ID: " + co.account.getID());
							System.out.println("����Ʈ �ܾ�: " + co.account.getPoint());
							System.out.println("�� ������: " + co.account.getPayment());
							System.out.print("��й�ȣ�� �����Ͻðڽ��ϱ�(Y/N): ");
							String ch = sc.nextLine();
							if(ch.equals("Y")) {
								System.out.println("���� ��й�ȣ: " + co.account.getPW());
								System.out.print("������ ��й�ȣ�� �Է��ϼ���: ");
								String PW = sc.nextLine();
								co.account.setPW(PW);
								System.out.print("��й�ȣ�� ����Ǿ����ϴ�.");
							}
							break;
						}
						case 2: { // ��ǰ�˻�
							System.out.println("======================================");
							System.out.println("[ ��ǰ �˻� ]");
							System.out.print("��ǰ�� �Է�: ");
							String gName = sc.nextLine();
							Goods_Node goods = mo.showInventory(gList, gName);
							if(goods == null) System.out.println("�˻��� ��ǰ�� �����ϴ�.");
							break;
						}
						case 3: { // ��ǰ ����
							while(true) { // ��ǰ�� ã�� �� ���� �˻�
								System.out.println("======================================");
								System.out.println("[ ��ǰ ���� ]");
								System.out.print("��ǰ�� �Է�(-1�� �Է��ϸ� �ɼ� �������� ���ư��ϴ�.): ");
								String gName = sc.nextLine();
								if(gName.equals("-1")) break; // �ɼ� �������� ���ư���
								int res = co.buyGoods(gList, gName);
								if(res == 1) break;
							}
							break;
						}
						case 4: { // ��ǰ �ֹ�
							while(true) { // ��ǰ�� ã�� �� ���� �˻�
								System.out.println("======================================");
								System.out.println("[ ��ǰ �ֹ� ]");
								System.out.print("��ǰ�� �Է�(-1�� �Է��ϸ� �ɼ� �������� ���ư��ϴ�.): ");
								String gName = sc.nextLine();
								if(gName.equals("-1")) break; // �ɼ� �������� ���ư���
								int res = co.orderGoods(dList, gList, gName);
								if(res == 1) break;
							}
							break;
						}
						case 5: { // �ֹ� Ȯ��
							co.checkDeliveryOrder(dList);
							break;
						}
					}
					if(isLogOut == 1) {
						isLogOut = 0; // �α׾ƿ� ���� �ٽ� �ʱ�ȭ
						mode = 0; // ��� �ʱ�ȭ
						break;
					}
				}
			}
		}
		
		try { // ����� ��� list�� ���� ����� ������ ����
			FileWriter writer1 = new FileWriter("mList.txt");
			for(Member_Node x = mList.first; x != null; x = x.next) { // Member_List�� ��� �����͸� ����
				String data = x.getID() + " " + x.getPW() + " " +  x.getPoint() + " " +  x.getPayment() + "\r\n";
				writer1.write(data);
			}
			
			FileWriter writer2 = new FileWriter("gList.txt");
			for(Goods_Node x = gList.first; x != null; x = x.next) { // Goods_List�� ��� �����͸� ����
				String data = x.getName() + " " + x.getPrice() + " " + x.getQuantity() + "\r\n";
				writer2.write(data);
			}
			
			FileWriter writer3 = new FileWriter("dList.txt"); // Delivery_List�� ��� �����͸� ����
			Iterator<Delivery_Node> it = dList.dList.iterator();
			while(it.hasNext()) {
				Delivery_Node x = it.next();
				String data = x.getBuyer() + " " + x.getName() + " " + x.getQuantity() + " " + x.getPrice() + "\r\n";
				writer3.write(data);
			}
			writer1.close();
			writer2.close();
			writer3.close();
		} catch (FileNotFoundException fnfe) {
			System.out.println("No File");
		} catch (IOException ioe) {
			System.out.println("Can't Read File");
		}
	}
}