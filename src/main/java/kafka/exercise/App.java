package kafka.exercise;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
    	
    	WhiteWordThread zhang,wang;
    	zhang = new WhiteWordThread("zhang");
    	wang = new WhiteWordThread("wang");
    	zhang.start();
    	for (int i = 0; i < 2; i++) {
			System.out.println("main" + i);
		}
    	wang.start();
    	System.out.println("main finishs");
    	
    }
    
}


class WhiteWordThread extends Thread {
	int n = 0;
	public WhiteWordThread(String s) {
		setName(s);
		
	}
	@Override
	public void run() {
		for (int i = 1; i <= 8; i++) {
			System.out.println("我是一个线程，我的名字是：" + getName());
			
		}
		

	}
}
