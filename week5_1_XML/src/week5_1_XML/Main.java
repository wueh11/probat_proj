package week5_1_XML;

public class Main {

	public static void main(String[] args) {
		//System.gc();
		long preUseMemory = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		long start = System.currentTimeMillis();
		
		XMLContoller ctrl = new XMLContoller();
		ctrl.processMethod();

		long end = System.currentTimeMillis();
		//System.gc();
		long aftUseMemory = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		System.out.println("���� �ð� : "+(end-start));
		System.out.println("�޸� ��뷮 : "+(aftUseMemory-preUseMemory)/1024);
	}// end main()

}