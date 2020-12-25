// Ȩ���������� ������ ��� ���ؼ� jsoup ����� �ʿ���
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.util.Scanner;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList; 

public class ex01{
	interface covid_class_func<S, T, D>{ // Ŭ�������� ���� �Լ����� �������̽��� ����
		public S get_city_name(); // ������ �̸��� ��ȯ��
		public T infect_num();	// Ȯ���� ���� ��ȯ��
		public T quar_num();	// �ݸ��� ���� ��ȯ��
		public T cur_num();		// ��ġ�� ���� ��ȯ��
		public T dead_num();	// ����� ���� ��ȯ��
		public D infect_rate();	// 10���� �� �������� ��ȯ��
		public void city_info();	// �ش� ������ ������ �����
	}

	static class covid_city<S, T, D> implements covid_class_func<S, T, D>{
		private S city_name; 	// ���� �̸�
		private	T infected;		// Ȯ����
		private T quarantine;	// �ݸ���
		private T cured;		// ��ġ��
		private T dead;			// �����
		private D rate;			// 10���� �� ������
		
		public covid_city() {
			//default constructor
		}
		
		public covid_city(S name, T con, T qua, T cur, T dead, D rate) { // ������
			this.city_name = name;
			this.infected = con;
			this.quarantine = qua;
			this.cured = cur;
			this.dead = dead;
			this.rate = rate;
		}
		
		//�������̽��� �Լ����� �������̵�
		@Override
		public S get_city_name() {
			return this.city_name;
		}
		
		@Override
		public T infect_num() {
			return this.infected;
		}
		
		@Override
		public T quar_num() {
			return this.quarantine;
		}
		
		@Override
		public T cur_num() {
			return this.cured;
		}
		
		@Override
		public T dead_num() {
			return this.dead;
		}
		
		@Override
		public D infect_rate() {
			return this.rate;
		}
		
		@Override
		public void city_info() {
			if(city_name.toString().equals("�˿�"))	// �˿��̶�� �ϸ� �� ���� �����ϱ� �����Ƿ�
				System.out.println("���� : �ؿ�����");	// �ؿ� �������� �ٲ� ���
			else
				System.out.println("���� : " + this.city_name);
			System.out.println("Ȯ���� : " + this.infected + "��");
			System.out.println("�ݸ��� : " + this.quarantine + "��");
			System.out.println("��ġ�� : " + this.cured + "��");
			System.out.println("����� : " + this.dead + "��");
			if(Double.parseDouble(rate.toString()) == 0.00)		// �������� 0���� �� ����
				System.out.println("10���� �� ������ : ���� ����");		// ���� �������� ���
			else
				System.out.println("10���� �� ������ : " + this.rate + "��");
		}
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes"}) // ���� ��� ���ø� ���� ������̼�
	public static void main(String[] args) throws Exception{	
		String url = "http://ncov.mohw.go.kr/"; // �ڷγ�19 ��������� URL
		List<covid_city> NationWide = new ArrayList<covid_city>(); // ���ú� �ڷγ� ������ ������ 
																   // list �÷��� �����ӿ�ũ ����
		Document doc = Jsoup.connect(url).get();	// �ش� �ּ��� HTML�� �о�� 
		Elements covid = doc.select(".num");	// Ŭ���� �̸��� num�� �׸��� ������
		Elements covid_city_name = doc.select(".cityname"); // Ŭ���� �̸��� cityname�� �׸��� ������
		
		// �� ������ �����Ͽ� �迭�� ����
		String[] covid_num = covid.toString().split("\n");
		String[] city_name = covid_city_name.toString().split("\n"); 

		// List�� �ڷ� ����
		for(int i = 0 ; i < 19; i++) {
 			int[] co_str = new int[4]; // �ڷγ� ���� ���ڸ� ������ �迭 ����
 			double dco_str;	// 10����� �������� ������ double�� ���� ����
 			
 			// �� ������ ������ �迭���� ���ڸ��� �����Ͽ� Integer������ �ٲ�
 			co_str[0] = Integer.parseInt(covid_num[25 + i*5].replaceAll("[^0-9]", "").toString());	
 			co_str[1] = Integer.parseInt(covid_num[26 + i*5].replaceAll("[^0-9]", "").toString());
 			co_str[2] = Integer.parseInt(covid_num[27 + i*5].replaceAll("[^0-9]", "").toString());
 			co_str[3] = Integer.parseInt(covid_num[28 + i*5].replaceAll("[^0-9]", "").toString());
 			if(i == 18)
 				dco_str = 0.00; // �ؿ������� ������ �������� �ʾƼ� 0���� ǥ��
 			else
 				dco_str = Double.parseDouble(covid_num[29 + i*5].substring(18, 23).toString());		
			
 			// ���������� �� ������ ������ �迭���� ���� �̸��鸸�� �߶�
 			covid_city cc = new covid_city(city_name[i].substring(21, 23), co_str[0], co_str[1], co_str[2], co_str[3], dco_str);
 			
 			NationWide.add(cc);
		}
 		
		// ��¥ ���
 		Calendar cal = Calendar.getInstance();
 		int year = cal.get(Calendar.YEAR);
 		int month = cal.get(Calendar.MONTH) + 1;
 		int day = cal.get(Calendar.DAY_OF_MONTH);
 		int hour = cal.get(Calendar.HOUR_OF_DAY);
 		
 		// ���α׷� UI
 		System.out.println("-----���ѹα� �ڷγ� 19 ���� ���� Ȯ�� ���α׷�-----");
 		System.out.println("");
 		System.out.println("***���� �Է��� �����ó� Ư���� ������ �Է��ؾ� �մϴ�.***");
 		System.out.println("***���� �Է� �� 2���� ���Ӹ����� �Է��ؾ��մϴ�.***");
		System.out.println("***����� 'exit' �Ǵ� '����'�� �Է����ּ���.***");
		System.out.println("");
		System.out.println("���� �ð� : " + year + "�� " + month + "�� " + day + "�� " + hour + "�� ");
 		
		// ���ٽ��� Ȱ���Ͽ� Ȯ���� ���� ���� �������� ������ ��
		Collections.sort(NationWide, (n1, n2) -> Integer.parseInt(n1.infect_num().toString())
 				- Integer.parseInt(n2.infect_num().toString()));
 		
 		System.out.println("Ȯ���ڰ� ���� ���� Top 3");		
 		
 		// ��ü �� �ۼ�Ʈ �����ϴ� �� ���ϱ� ���� double�� ������ ����
 		double total = Double.parseDouble(NationWide.get(18).infect_num().toString());
 		double first = Double.parseDouble(NationWide.get(17).infect_num().toString()) / total;
 		double second = Double.parseDouble(NationWide.get(16).infect_num().toString()) / total;
 		double third = Double.parseDouble(NationWide.get(15).infect_num().toString()) / total;
 		
 		// ��½� ������ �ش� ���ð� ��ü ������ �� �� �� �ۼ�Ʈ�� �����ϴ��� �Ҽ��� ��°�ڸ����� ���
 		System.out.print("1." + NationWide.get(17).get_city_name().toString());
 		System.out.println(String.format(" %.2f", first*100.00) + "%");
 		System.out.print("2." + NationWide.get(16).get_city_name().toString());
 		System.out.println(String.format(" %.2f", second*100.00) + "%");
 		System.out.print("3." + NationWide.get(15).get_city_name().toString());
 		System.out.println(String.format(" %.2f", third*100.00) + "%");
 		System.out.println("");
 		
 		// ����ڰ� ������ ���ϴ� ������ �Է¹���
 		while(true) {	// ���ѷ����� ���� ������ ������ �� ���� �ݺ�
 			System.out.print("���ϴ� ������ �Է����ּ��� : ");
 			Scanner sc = new Scanner(System.in);
 			String c_name = sc.next();
 			
 			if(c_name.equals("exit") || c_name.equals("����")) { // ���� ����
 				System.out.println("---------------���α׷� ����---------------");
 				break;
 			}
 			
 			if(c_name.equals("�ؿ�"))
 				c_name = "�˿�"; // �ؿܷ� �˻��ص� �ؿ� ���� Ȯ���ڸ� �� �� �ֵ��� ��.
 			
 			Iterator<covid_city> itr = NationWide.iterator(); // �ݺ��ڸ� �̿��Ͽ� �ش� ���ø� list���� ã�Ƴ�
 			for(int i = 0; itr.hasNext(); i++) {
 				if(itr.next().get_city_name().toString().equals(c_name)) {
 					NationWide.get(i).city_info(); // �ش� ������ ���� ���
 					break;
 				}
 			}
 		}
 		
	}
}
