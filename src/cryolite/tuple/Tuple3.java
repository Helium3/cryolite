package cryolite.tuple;

public class Tuple3<T1, T2, T3> {
	T1 o1;
	T2 o2;
	T3 o3;
	
	public Tuple3(T1 o1, T2 o2, T3 o3) {
		this.o1 = o1;
		this.o2 = o2;
		this.o3 = o3;
	}
	
	protected T1 _1() {
		return o1;
	}
	
	protected T2 _2() {
		return o2;
	}
	
	protected T3 _3() {
		return o3;
	}
}
