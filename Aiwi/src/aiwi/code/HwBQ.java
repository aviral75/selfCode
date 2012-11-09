package aiwi.code;

import java.util.concurrent.locks.ReentrantLock;

public class HwBQ {
	static String s="ASvhiurkalla";
	static int i=0;
	private static Thread t1;
	private static Thread t2;
	final static ReentrantLock lock=new ReentrantLock();

	public static void main(String[] args) {
		t1 = new Thread(
		new Runnable() {
			public void run() {
				synchronized (s) {
					while(i<s.length()){
						try {
							System.out.println(Thread.currentThread().getName()+": "+s.charAt(i++));
							notifyAll();
							wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		});
				
				
		t1.setName("t1");
		t2 = new Thread(
				new Runnable() {
					public void run() {
						synchronized (s) {
							while(i<s.length()){
								try {
									wait();
									System.out.println("\t"+Thread.currentThread().getName()+": "+s.charAt(i++));
									notifyAll();
									wait();
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

						}
					}
				});
		t2.setName("t2");
		try {
			t1.start();
			Thread.sleep(100);
			t2.start();

			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
