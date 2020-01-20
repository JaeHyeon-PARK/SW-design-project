import java.io.*;
import java.util.*;

class Log_In {
	String ID;
	String PW;
	
	public int logWindow() { // 로그인창 생성 및 입력
		System.out.println("======================================");
		Scanner mSC = new Scanner(System.in);
		System.out.print("ID(-1을 입력하면 시스템이 종료됩니다.): ");
		ID = mSC.nextLine();
		System.out.print("비밀번호: ");
		PW = mSC.nextLine();
		
		if(ID.equals("-1")) return -1;
		else return 1;
	}
	
	public Member_Node isCustomer(Member_List mList, Search_Account sa) { // 고객 계정 확인
		Member_Node temp = sa.search(mList, ID, PW);
		if(temp == null) {
			System.out.println("계정이 존재하지 않습니다.");
			return null;
		}
		else {
			if(!PW.equals(temp.getPW())) {
				System.out.println("계정 정보가 일치하지 않습니다.");
				return null;
			}
		}
		return temp;
	}
	
	public boolean isMart(Mart_Option mo) { // 마트 계정 확인
		if(ID.equals(mo.getID()) && PW.equals(mo.getPW())) return true;
		else return false;
	}
}

class Customer_Option { // 고객의 계정의 정보,  Use case와 관련된 메소드를 저장
	Member_Node account; // 로그인된 Member_Node의 위치 저장
	
	public void showOption() { // 사용 할 수 있는 Option들을 출력
		System.out.println("======================================");
		System.out.println("Customer Option(-1을 입력하면 로그아웃이 됩니다.)");
		System.out.println("1. 계정 관리");
		System.out.println("2. 상품 검색");
		System.out.println("3. 상품 구매");
		System.out.println("4. 상품 주문");
		System.out.println("5. 주문 확인");
	}
	
	public void changePW() { // 비밀번호 변경
		Scanner mSC = new Scanner(System.in);
		System.out.print("설정할 비밀번호: ");
		String PW = mSC.nextLine();
		
		account.setPW(PW);
	}
	
	public int buyGoods(Goods_List gList, String name) { // 상품 구매
		Goods_Node node = showInventory(gList, name); // 검색한 상품을 탐색
		if(node == null) {
			System.out.println("검색한 상품이 없습니다.");
			return 0; // 검색한 상품이 없다는 결과 반환, 옵션 선택 화면으로
		}
		
		int quantity;
		int point;
		if(node.getQuantity() > 0) { // 수량이 있는 경우
			Scanner gSC = new Scanner(System.in);
			while(true) {
				System.out.print("구매할 수량(-1을 입력하면 상품 구매로 돌아갑니다.): ");
				quantity = gSC.nextInt();
				
				if(quantity <= 0) return -1; // 구매를 취소한다는 결과 반환, 검색 화면으로
				if(quantity > node.getQuantity()) System.out.println("상품 수량이 충분하지 않습니다.");
				else break;				
			}
			while(true) {
				System.out.print("사용할 포인트: ");
				point = gSC.nextInt();
				gSC.nextLine(); // 개행 문자 지우기
				
				if(point > account.getPoint() || point <= -1) System.out.println("정상적인 포인트 값이 아닙니다.");
				else {
					account.setPoint(account.getPoint() - point); // 포인트 차감
					account.setPoint(account.getPoint() + (int)(node.getPrice() * quantity * 0.03)); // 포인트 증가
					account.setPayment(account.getPayment() + node.getPrice() * quantity); // 총 결제액 증가
					node.setQuantity(node.getQuantity() - quantity); // 수량 차감
					System.out.println("상품 구매에 성공했습니다.");
					break;
				}
			}
		}
		else System.out.println("구매하려는 상품의 수량이 없습니다."); // 수량이 없는 경우
		return 1; // 정상적인  결과 반환, 옵션 선택 화면으로
	}
	
	public int orderGoods(Delivery_List dList, Goods_List gList, String name) { // 상품 주문
		Goods_Node node = showInventory(gList, name); // 검색한 상품을 탐색
		if(node == null) {
			System.out.println("검색한 상품이 없습니다.");
			return 0; // 검색한 상품이 없다는 결과 반환, 옵션 선택 화면으로
		}
		
		int quantity;
		int point;
		if(node.getQuantity() > 0) {
			Scanner gSC = new Scanner(System.in);
			while(true) {
				System.out.print("구매할 수량: ");
				quantity = gSC.nextInt();
				
				if(quantity <= 0) return -1; // 구매를 취소한다는 결과 반환, 검색 화면으로
				if(quantity > node.getQuantity()) System.out.println("상품 수량이 충분하지 않습니다.");
				else break;				
			}
			while(true) {
				System.out.print("사용할 포인트: ");
				point = gSC.nextInt();
				gSC.nextLine(); // 개행 문자 지우기
				
				if(point > account.getPoint() || point <= -1) System.out.println("정상적인 포인트 값이 아닙니다.");
				else {
					dList.add(account.getID(), node.getName(), quantity, quantity * node.getPrice());
					account.setPoint(account.getPoint() - point); // 포인트 차감
					account.setPoint(account.getPoint() + (int)(node.getPrice() * 0.05)); // 포인트 증가
					account.setPayment(account.getPayment() + node.getPrice() * quantity); // 총 결제액 증가
					node.setQuantity(node.getQuantity() - quantity);
					System.out.println("상품 주문에 성공했습니다.");
					break;
				}
			}
		}
		else System.out.println("구매하려는 상품의 수량이 없습니다."); // 수량이 없는 경우
		return 1; // 정상적인 결과 반환, 옵션 선택 화면으로
	}
	
	public Goods_Node showInventory(Goods_List gList, String name) { return gList.search(name); }
	public void checkDeliveryOrder(Delivery_List dList) { dList.search(account.getID()); }
}

class Mart_Option { // Mart 계정의 정보,  Use case와 관련된 메소드를 저장
	private String ID = "administrator";
	private String PW = "temppassword";
	
	public void showOption() { // 사용 할 수 있는 Option들을 출력
		System.out.println("======================================");
		System.out.println("Mart Option(-1을 입력하면 로그아웃이 됩니다.)");
		System.out.println("1. 회원 관리");
		System.out.println("2. 상품 검색");
		System.out.println("3. 상품 관리");
		System.out.println("4. 주문 관리");
		System.out.println("5. 주문 출력");
	}
	
	String getID() { return this.ID; }
	String getPW() { return this.PW; }
	
	void makeNewMember(Member_List mList, String ID) { // 새 계졍 추가
		Scanner mSC = new Scanner(System.in);
		System.out.print("설정할 비밀번호: ");
		String PW = mSC.nextLine();
		
		mList.add(ID, PW, 0, 0);
	}
	
	void deleteMember(Member_List mList, String ID) { mList.delete(ID); } // 계정 삭제
	
	Goods_Node showInventory(Goods_List gList, String name) { return gList.search(name); } // 상품 검색
	
	void addNewGoods(Goods_List gList) { // 새 상품 추가
		Scanner gSC = new Scanner(System.in);
		
		System.out.print("상품명: ");
		String name = gSC.nextLine();
		System.out.print("가격: ");
		int price = gSC.nextInt();
		System.out.print("수량: ");
		int quantity = gSC.nextInt();
		
		gList.add(name, price, quantity);
	}
	
	void manageDeliveries(Delivery_List dList) { // 주문 완료 시키기
		Scanner dSC = new Scanner(System.in);
		
		System.out.print("완료할 주문 수: ");
		int N = dSC.nextInt();
		
		dList.delete(N);
	}
	
	void checkAllDeliveries(Delivery_List dList) { dList.show(); } // 주문 정보 출력
}

class Search_Account {// Member_List에서 원하는 Member_Node를 검색 후 반환
	Member_Node x;
	
	public Member_Node search(Member_List mList, String ID, String PW) {
		for(x = mList.first; x != null; x = x.next) { // 계정의 위치 탐색
			String tempID = x.getID();
			
			if(ID.equals(tempID)) return x;
		}
		return null;
	}
}

class Member_List { // Member_Node가 저장되는 연결리스트
	Member_Node first;
	private int N = 0; // 총 노드의 수
	
	public void add(String ID, String PW, int point, int payment) { // ID의 오름차순으로 저장
		Member_Node newNode = new Member_Node(ID, PW, point, payment, null);
		N++;
		
		if(first == null) { // List가 비었는 경우
			first = newNode;
			return;
		}
		else if(ID.compareTo(first.getID()) < 0) { // 추가할 ID가 리스트의 첫 ID보다 값이 작을 경우
			newNode.next = first;
			first = newNode;
			return;
		}
		else { // 둘 다 아니면 리스트를 탐색해서 들어갈 위치를 찾음
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
		if(ID.equals(first.getID())) { // 첫번쨰 노드를 삭제
			first = first.next;	N--;
			return;
		}
		for(Member_Node x = first; x.next != null; x = x.next) { // 삭제할 노드 탐색
			if(ID.equals(x.next.getID())) {
				x.next = x.next.next; N--;
				return;
			}
		}
	}
	
	public int size() { return N; } // 저장된 노드 수 반환
}

class Goods_List { // Goods_List가 저장되는 연결리스트
	Goods_Node first;
	private int N = 0;
	
	public Goods_Node search(String name) { // 원하는 Goods_Node의 정보를 출력 후 반환
		for(Goods_Node x = first; x != null; x = x.next) {
			if(name.equals(x.getName())) {
				System.out.println("상품명: " + x.getName());
				System.out.println("가격: " + x.getPrice());
				System.out.println("수량: " + x.getQuantity());
				return x;
			}
		}
		return null;
	}
	
	public void add(String name, int price, int quantity) { // 상품명의 오름차순으로 저장
		Goods_Node newNode = new Goods_Node(name, price, quantity, null);
		N++;
		
		if(first == null) { // List가 비었는 경우
			first = newNode;
			return;
		}
		else if(name.compareTo(first.getName()) < 0) { // 추가할 상품명이 리스트의 첫 상품명보다 값이 작을 경우
			newNode.next = first;
			first = newNode;
			return;
		}
		else { // 둘 다 아니면 리스트를 탐색해서 들어갈 위치를 찾음
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
		if(name.equals(first.getName())) { // 첫번쨰 노드를 삭제
			first = first.next;	N--;
			return;
		}
		for(Goods_Node x = first; x.next != null; x = x.next) { // 삭제할 노드 탐색
			if(name.equals(x.next.getName())) {
				x.next = x.next.next; N--;
				return;
			}
		}
	}
	
	public int size() { return N; } // 저장된 노드 수 반환
}

class Delivery_List { // Delivery_List가 저장되는 배열리스트
	ArrayList<Delivery_Node> dList = new ArrayList<Delivery_Node>();
	private int N = 0;
	
	public void show() {
		Iterator<Delivery_Node> it = dList.iterator();
		
		if(N < 10) { // 저장된 노드가 10개 미만일 경우 전체를 출력
			while(it.hasNext()) {
				Delivery_Node node = it.next();
				System.out.println("주문자: " + node.getBuyer());
				System.out.println("주문 상품: " + node.getName());
				System.out.println("주문 수량: " + node.getQuantity());
				System.out.println("총 가격: " + node.getPrice());
				System.out.println();
			}
		}
		else { // 저장된 노드가 10개 이상일 경우 10개만 출력
			for(int i = 0; i < 10; i++) {
				Delivery_Node node = it.next();
				System.out.println("주문자: " + node.getBuyer());
				System.out.println("주문 상품: " + node.getName());
				System.out.println("주문 수량: " + node.getQuantity());
				System.out.println("총 가격: " + node.getPrice());
				System.out.println();
			}
		}
	}
	
	public void search(String buyer) { // 주문자가 일치하는 노드의 정보를 출력
		Iterator<Delivery_Node> it = dList.iterator();
		
		while(it.hasNext()) {
			Delivery_Node node = it.next();
			if(!buyer.equals(node.getBuyer())) continue;
			System.out.println("주문자: " + node.getBuyer());
			System.out.println("주문 상품: " + node.getName());
			System.out.println("주문 수량: " + node.getQuantity());
			System.out.println("총 가격: " + node.getPrice());
			System.out.println();
		}
	}
	
	public void add(String buyer, String name, int quantity, int price) { // 시간순으로 저장
		Delivery_Node newNode = new Delivery_Node(buyer, name, quantity, price);
		dList.add(newNode);
		this.N++;
	}
	
	public void delete(int N) { // N번 만큼 가장 앞에 있는 노드를 삭제
		for(int i = 0; i < N; i++) {
			dList.remove(0);
			this.N--;
		}
	}
	
	public int size() { return N; } // 저장된 노드 수 반환
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
		
		int mode = 0, choose; // mode는 마트는 1, 고객은 2를 가짐
		int isLogOut = 0; // 로그아웃 플래그
		Scanner sc = new Scanner(System.in);
		
		try { // 기존 파일의 데이터를 읽어오는 작업
			StringTokenizer stok; // 데이터를 종류별로 분리하기 위함
			String str; // 한 줄 씩 읽어옴
			BufferedReader reader = new BufferedReader(new FileReader("mList.txt")); // Member_List의 데이터를 읽어옴
			while((str = reader.readLine()) != null) {
				if(!str.equals("")) {
					stok = new StringTokenizer(str); // 데이터를 종류별로 분리
					while(stok.hasMoreTokens()) {
						String ID = stok.nextToken().toString();
						String PW = stok.nextToken().toString();
						int point = Integer.parseInt(stok.nextToken().toString());
						int payment = Integer.parseInt(stok.nextToken().toString());
						mList.add(ID, PW, point, payment); // Member_List에 하나씩 저장
					}
				}
			}
			
			reader = new BufferedReader(new FileReader("gList.txt")); // Goods_List의 데이터를 읽어옴
			while((str = reader.readLine()) != null) {
				if(!str.equals("")) {
					stok = new StringTokenizer(str); // 데이터를 종류별로 분리
					while(stok.hasMoreTokens()) {
						String name = stok.nextToken().toString();
						int price = Integer.parseInt(stok.nextToken().toString());
						int quantity = Integer.parseInt(stok.nextToken().toString());
						gList.add(name, price, quantity);
					}
				}
			}
			
			reader = new BufferedReader(new FileReader("dList.txt")); // Delivery_List의 데이터를 읽어옴
			while((str = reader.readLine()) != null) {
				if(!str.equals("")) {
					stok = new StringTokenizer(str); // 데이터를 종류별로 분리
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
		
		while(true) { // console을 종료할 때 까지 반복
			int isQuit = logIn.logWindow(); // 로그인 성공할 때 까지 반복
			if(isQuit == -1) { // 로그인 창에서 시스템을 종료
				System.out.println("시스템이 종료됩니다.");
				break;
			}
			
			if(logIn.isMart(mo)) {
				mode = 1; // 마트 모드로 로그인 성공
				System.out.println("Administrator님이 로그인 하였습니다.");
			}
			else {
				co.account = logIn.isCustomer(mList, sa);
				if(co.account != null) {
					System.out.println(co.account.getID() + "님이 로그인 하였습니다.");
					mode = 2; // 고객 모드로 로그인 성공
				}
				else continue;
			}
			
			if(mode == 1) { // 마트 모드
				while(true) {
					mo.showOption();
					System.out.print("입력: "); // 옵션 입력
					choose = sc.nextInt();
					sc.nextLine(); // 개행 문자 지우기
					
					switch(choose) {
						case -1: // 로그아웃
							System.out.println("Administrator님이 로그아웃 하였습니다.");
							isLogOut = 1;
							break;
						case 1: { // 회원 관리
							System.out.println("======================================");
							System.out.println("[ 회원 관리 ]");
							System.out.print("계정 입력: "); // 계정 검색
							String acID = sc.nextLine();
							
							Member_Node mem = sa.search(mList, acID, null);
							if(mem == null) { // 계정이 없는 경우
								System.out.print("계정이 없습니다. 새 계정을 만드시겠습니까(Y/N): ");
								String ch = sc.nextLine();
								if(ch.equals("Y")) {
									System.out.print("비밀번호 입력: ");
									String acPW = sc.nextLine();
									mList.add(acID, acPW, 0, 0);
									System.out.println("새 계정이 생성되었습니다.");
									break;
								}
								else break;
							}
							else {
								System.out.println("ID: " + mem.getID());
								System.out.println("포인트: " + mem.getPoint());
								System.out.println("총 결제액: " + mem.getPayment());
								System.out.print("계정을 삭제하시겠습니까(Y/N): ");
								String ch = sc.nextLine();
								if(ch.equals("Y")) {
									mList.delete(mem.getID());
									System.out.println("계정이 삭제되었습니다.");
									break;
								}
								else break;
							}
						}
						case 2: { // 상품 검색
							System.out.println("======================================");
							System.out.println("[ 상품 검색 ]");
							System.out.print("상품명 입력: ");
							String gName = sc.nextLine();
							Goods_Node goods = mo.showInventory(gList, gName);
							if(goods == null) System.out.println("검색한 상품이 없습니다");
							break;
						}
						case 3: { // 상품 관리
							System.out.println("======================================");
							System.out.println("[ 상품 관리 ]");
							System.out.print("상품명 입력: ");
							String gName = sc.nextLine();
							Goods_Node goods = mo.showInventory(gList, gName);
							if(goods == null) {
								System.out.print("검색하신 상품이 없습니다. 상품을 추가하시겠습니까(Y/N): ");
								String ch = sc.nextLine();
								if(ch.equals("Y")) {
									System.out.print("가격 입력: ");
									int gPrice = sc.nextInt();
									sc.hasNextLine(); // 개행 문자 지우기
									System.out.print("수량 입력: ");
									int gQuantity = sc.nextInt();
									sc.hasNextLine(); // 개행 문자 지우기
									gList.add(gName, gPrice, gQuantity);
									System.out.println("새 상품이 추가되었습니다.");
									break;
								}
								else break;
							}
							else {
								System.out.print("새 상품명 입력: ");
								String newName = sc.nextLine();
								System.out.print("새 가격 입력: ");
								int newPrice = sc.nextInt();
								sc.hasNextLine(); // 개행 문자 지우기
								System.out.print("새 수량 입력: ");
								int newQuantity = sc.nextInt();
								sc.hasNextLine(); // 개행 문자 지우기
								goods.setName(newName);
								goods.setPrice(newPrice);
								goods.setQuantity(newQuantity);
								System.out.println("새 정보가 반영되었습니다.");
								break;
							}
						}
						case 4 : { // 주문 관리
							int num;
							while(true) {
								System.out.print("완료할 주문의 수를 입력하세요: ");
								num = sc.nextInt();
								sc.hasNextLine(); // 개행 문자 지우기
								if(num > 0) break;
								System.out.println("정상적인 값을 입력하세요.");
							}
							if(num < dList.size()) dList.delete(num);
							else dList.delete(dList.size());
							System.out.println(num + "개의 주문이 완료되었습니다.");
							break;
						}
						case 5 : { // 주문 출력
							dList.show();
							break;
						}
					}
					if(isLogOut == 1) {
						isLogOut = 0; // 로그아웃 이후 다시 초기화
						mode = 0; // 모드 초기화
						break;
					}
				}
			}
			else if(mode == 2) { // 고객 모드
				while(true) {
					co.showOption();
					System.out.print("입력: "); // 옵션 입력
					choose = sc.nextInt();
					sc.nextLine(); // 개행 문자 지우기
					
					switch(choose) {
						case -1: // 로그아웃
							System.out.println(co.account.getID() + "님이 로그아웃 하였습니다.");
							isLogOut = 1;
							break;
						case 1: { // 계정 관리
							System.out.println("======================================");
							System.out.println("[ 계정 관리 ]");
							System.out.println("ID: " + co.account.getID());
							System.out.println("포인트 잔액: " + co.account.getPoint());
							System.out.println("총 결제액: " + co.account.getPayment());
							System.out.print("비밀번호를 변경하시겠습니까(Y/N): ");
							String ch = sc.nextLine();
							if(ch.equals("Y")) {
								System.out.println("현재 비밀번호: " + co.account.getPW());
								System.out.print("변경할 비밀번호를 입력하세요: ");
								String PW = sc.nextLine();
								co.account.setPW(PW);
								System.out.print("비밀번호가 변경되었습니다.");
							}
							break;
						}
						case 2: { // 상품검색
							System.out.println("======================================");
							System.out.println("[ 상품 검색 ]");
							System.out.print("상품명 입력: ");
							String gName = sc.nextLine();
							Goods_Node goods = mo.showInventory(gList, gName);
							if(goods == null) System.out.println("검색한 상품이 없습니다.");
							break;
						}
						case 3: { // 상품 구매
							while(true) { // 상품을 찾을 때 까지 검색
								System.out.println("======================================");
								System.out.println("[ 상품 구매 ]");
								System.out.print("상품명 입력(-1을 입력하면 옵션 선택으로 돌아갑니다.): ");
								String gName = sc.nextLine();
								if(gName.equals("-1")) break; // 옵션 선택으로 돌아가기
								int res = co.buyGoods(gList, gName);
								if(res == 1) break;
							}
							break;
						}
						case 4: { // 상품 주문
							while(true) { // 상품을 찾을 때 까지 검색
								System.out.println("======================================");
								System.out.println("[ 상품 주문 ]");
								System.out.print("상품명 입력(-1을 입력하면 옵션 선택으로 돌아갑니다.): ");
								String gName = sc.nextLine();
								if(gName.equals("-1")) break; // 옵션 선택으로 돌아가기
								int res = co.orderGoods(dList, gList, gName);
								if(res == 1) break;
							}
							break;
						}
						case 5: { // 주문 확인
							co.checkDeliveryOrder(dList);
							break;
						}
					}
					if(isLogOut == 1) {
						isLogOut = 0; // 로그아웃 이후 다시 초기화
						mode = 0; // 모드 초기화
						break;
					}
				}
			}
		}
		
		try { // 종료시 모든 list에 대해 변경된 데이터 저장
			FileWriter writer1 = new FileWriter("mList.txt");
			for(Member_Node x = mList.first; x != null; x = x.next) { // Member_List의 모든 데이터를 저장
				String data = x.getID() + " " + x.getPW() + " " +  x.getPoint() + " " +  x.getPayment() + "\r\n";
				writer1.write(data);
			}
			
			FileWriter writer2 = new FileWriter("gList.txt");
			for(Goods_Node x = gList.first; x != null; x = x.next) { // Goods_List의 모든 데이터를 저장
				String data = x.getName() + " " + x.getPrice() + " " + x.getQuantity() + "\r\n";
				writer2.write(data);
			}
			
			FileWriter writer3 = new FileWriter("dList.txt"); // Delivery_List의 모든 데이터를 저장
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