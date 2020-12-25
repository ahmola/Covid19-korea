// 홈페이지에서 정보를 얻기 위해서 jsoup 모듈이 필요함
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
	interface covid_class_func<S, T, D>{ // 클래스에서 사용될 함수들을 인터페이스로 선언
		public S get_city_name(); // 도시의 이름을 반환함
		public T infect_num();	// 확진자 수를 반환함
		public T quar_num();	// 격리자 수를 반환함
		public T cur_num();		// 완치자 수를 반환함
		public T dead_num();	// 사망자 수를 반환함
		public D infect_rate();	// 10만명 당 감염률을 반환함
		public void city_info();	// 해당 도시의 정보를 출력함
	}

	static class covid_city<S, T, D> implements covid_class_func<S, T, D>{
		private S city_name; 	// 도시 이름
		private	T infected;		// 확진자
		private T quarantine;	// 격리자
		private T cured;		// 완치자
		private T dead;			// 사망자
		private D rate;			// 10만명 당 감염률
		
		public covid_city() {
			//default constructor
		}
		
		public covid_city(S name, T con, T qua, T cur, T dead, D rate) { // 생성자
			this.city_name = name;
			this.infected = con;
			this.quarantine = qua;
			this.cured = cur;
			this.dead = dead;
			this.rate = rate;
		}
		
		//인터페이스의 함수들을 오버라이드
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
			if(city_name.toString().equals("검역"))	// 검역이라고 하면 한 번에 이해하기 어려우므로
				System.out.println("도시 : 해외유입");	// 해외 유입으로 바꿔 출력
			else
				System.out.println("도시 : " + this.city_name);
			System.out.println("확진자 : " + this.infected + "명");
			System.out.println("격리자 : " + this.quarantine + "명");
			System.out.println("완치자 : " + this.cured + "명");
			System.out.println("사망자 : " + this.dead + "명");
			if(Double.parseDouble(rate.toString()) == 0.00)		// 감염률이 0으로 된 것은
				System.out.println("10만명 당 감염률 : 정보 없음");		// 정보 없음으로 취급
			else
				System.out.println("10만명 당 감염률 : " + this.rate + "명");
		}
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes"}) // 보안 경고 무시를 위한 어노테이션
	public static void main(String[] args) throws Exception{	
		String url = "http://ncov.mohw.go.kr/"; // 코로나19 선별진료소 URL
		List<covid_city> NationWide = new ArrayList<covid_city>(); // 도시별 코로나 정보를 저장할 
																   // list 컬렉션 프레임워크 선언
		Document doc = Jsoup.connect(url).get();	// 해당 주소의 HTML을 읽어옴 
		Elements covid = doc.select(".num");	// 클래스 이름이 num인 항목만을 가져옴
		Elements covid_city_name = doc.select(".cityname"); // 클래스 이름이 cityname인 항목만을 가져옴
		
		// 줄 단위로 구분하여 배열에 저장
		String[] covid_num = covid.toString().split("\n");
		String[] city_name = covid_city_name.toString().split("\n"); 

		// List에 자료 저장
		for(int i = 0 ; i < 19; i++) {
 			int[] co_str = new int[4]; // 코로나 관련 숫자를 저장할 배열 선언
 			double dco_str;	// 10만명당 감염률을 저장할 double형 변수 선언
 			
 			// 줄 단위로 구분한 배열에서 숫자만을 추출하여 Integer형으로 바꿈
 			co_str[0] = Integer.parseInt(covid_num[25 + i*5].replaceAll("[^0-9]", "").toString());	
 			co_str[1] = Integer.parseInt(covid_num[26 + i*5].replaceAll("[^0-9]", "").toString());
 			co_str[2] = Integer.parseInt(covid_num[27 + i*5].replaceAll("[^0-9]", "").toString());
 			co_str[3] = Integer.parseInt(covid_num[28 + i*5].replaceAll("[^0-9]", "").toString());
 			if(i == 18)
 				dco_str = 0.00; // 해외유입은 정보를 제공하지 않아서 0으로 표시
 			else
 				dco_str = Double.parseDouble(covid_num[29 + i*5].substring(18, 23).toString());		
			
 			// 마찬가지로 줄 단위로 구분한 배열에서 도시 이름들만을 잘라냄
 			covid_city cc = new covid_city(city_name[i].substring(21, 23), co_str[0], co_str[1], co_str[2], co_str[3], dco_str);
 			
 			NationWide.add(cc);
		}
 		
		// 날짜 출력
 		Calendar cal = Calendar.getInstance();
 		int year = cal.get(Calendar.YEAR);
 		int month = cal.get(Calendar.MONTH) + 1;
 		int day = cal.get(Calendar.DAY_OF_MONTH);
 		int hour = cal.get(Calendar.HOUR_OF_DAY);
 		
 		// 프로그램 UI
 		System.out.println("-----대한민국 코로나 19 지역 정보 확인 프로그램-----");
 		System.out.println("");
 		System.out.println("***지역 입력은 광역시나 특별시 단위로 입력해야 합니다.***");
 		System.out.println("***지역 입력 시 2글자 줄임명으로 입력해야합니다.***");
		System.out.println("***종료는 'exit' 또는 '종료'를 입력해주세요.***");
		System.out.println("");
		System.out.println("현재 시각 : " + year + "년 " + month + "월 " + day + "일 " + hour + "시 ");
 		
		// 람다식을 활용하여 확진자 수에 따른 내림차순 정렬을 함
		Collections.sort(NationWide, (n1, n2) -> Integer.parseInt(n1.infect_num().toString())
 				- Integer.parseInt(n2.infect_num().toString()));
 		
 		System.out.println("확진자가 많은 도시 Top 3");		
 		
 		// 전체 몇 퍼센트 차지하는 지 구하기 위한 double형 변수들 선언
 		double total = Double.parseDouble(NationWide.get(18).infect_num().toString());
 		double first = Double.parseDouble(NationWide.get(17).infect_num().toString()) / total;
 		double second = Double.parseDouble(NationWide.get(16).infect_num().toString()) / total;
 		double third = Double.parseDouble(NationWide.get(15).infect_num().toString()) / total;
 		
 		// 출력시 순위와 해당 도시가 전체 감염자 수 중 몇 퍼센트를 차지하는지 소수점 둘째자리까지 출력
 		System.out.print("1." + NationWide.get(17).get_city_name().toString());
 		System.out.println(String.format(" %.2f", first*100.00) + "%");
 		System.out.print("2." + NationWide.get(16).get_city_name().toString());
 		System.out.println(String.format(" %.2f", second*100.00) + "%");
 		System.out.print("3." + NationWide.get(15).get_city_name().toString());
 		System.out.println(String.format(" %.2f", third*100.00) + "%");
 		System.out.println("");
 		
 		// 사용자가 정보를 원하는 지역을 입력받음
 		while(true) {	// 무한루프로 종료 조건이 만족할 때 까지 반복
 			System.out.print("원하는 지역을 입력해주세요 : ");
 			Scanner sc = new Scanner(System.in);
 			String c_name = sc.next();
 			
 			if(c_name.equals("exit") || c_name.equals("종료")) { // 종료 조건
 				System.out.println("---------------프로그램 종료---------------");
 				break;
 			}
 			
 			if(c_name.equals("해외"))
 				c_name = "검역"; // 해외로 검색해도 해외 유입 확진자를 볼 수 있도록 함.
 			
 			Iterator<covid_city> itr = NationWide.iterator(); // 반복자를 이용하여 해당 도시를 list에서 찾아냄
 			for(int i = 0; itr.hasNext(); i++) {
 				if(itr.next().get_city_name().toString().equals(c_name)) {
 					NationWide.get(i).city_info(); // 해당 도시의 정보 출력
 					break;
 				}
 			}
 		}
 		
	}
}
