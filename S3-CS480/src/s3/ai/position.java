package s3.ai;

public class position implements Comparable<position>{
	int node_id;
	public double x;
	public double y;
	public int g;
	public int h;
	public int f;
	position parent;
	
	public position(double x, double y, double goal_x, double goal_y, int g, position parent) {
		this.x = x;
		this.y = y;
		this.h = (int)Math.abs(x-goal_x) + (int)Math.abs(y-goal_y);
		this.g = g;
		this.f = g+h;
		this.parent = parent;
	}
	
	public double get_x() {
		return this.x;
	}
	
	public double get_y() {
		return this.y;
	}
	
	public int get_h()
	{
		return this.h;
	}
	
	public int get_g() {
		return this.g;
	}
	
	public int get_f()
	{
		return this.f;
	}
	
	public position get_parent() {
		return this.parent;
	}
	
	@Override
	public int compareTo(position P) {
		if(this.f > P.f) {
			return 1;
		}
		else {
			return -1;
		}
	}
}
