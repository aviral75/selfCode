package aiwi.code;

public class SyncPrinter {
	public int index;
	boolean done = false;
	synchronized void print2() {
		if(!done)
			try {
				wait();
			} catch(InterruptedException e) {
                Thread.interrupted();
			}
		if(index<s.length())
			System.out.println("\t" + s.charAt(index));
		done = false;
		notify();
	}
	synchronized void print1(int n) {
		if(done)
			try {
				wait();
			} catch(InterruptedException e) {
				System.out.println("InterruptedException caught");
			}
		if(n<s.length())
			System.out.println("" + s.charAt(n));
		this.index = n+1;
		done = true;
		
		notify();
	}
	
	public static String s="ASvhiurkalla";
	
	public static void main(String[] args) {
		SyncPrinter q=new SyncPrinter();
		new Printer1(q).start();
		new Printer2(q);
	}
}

class Printer1 extends Thread {
	SyncPrinter q;
	Printer1(SyncPrinter q) {
		this.q = q;
	}
	public void run() {
		int i = -2;
		while(q.index<=SyncPrinter.s.length()) {
			i=i+2;
			q.print1(i);
		}
	}
}

class Printer2 extends Thread {
	SyncPrinter q;
	Printer2(SyncPrinter q) {
		this.q = q;
		new Thread(this, "Printer2").start();
	}
	public void run() {
		while(q.index<=SyncPrinter.s.length()) {
			q.print2();
		}
	}
}


