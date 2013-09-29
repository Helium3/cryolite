package cryolite.tuple;

public class Tuple2<T1, T2> {
	T1 o1;
	T2 o2;

	public Tuple2(T1 o1, T2 o2) {
		this.o1 = o1;
		this.o2 = o2;
	}

	protected T1 _1() {
		return o1;
	}

	protected T2 _2() {
		return o2;
	}

	protected void _1(T1 o1) {
		this.o1 = o1;
	}

	protected void _2(T2 o2) {
		this.o2 = o2;
	}
}
