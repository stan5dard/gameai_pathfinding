package s3.ai;
import s3.base.S3;
import s3.base.S3Map;
import s3.entities.S3PhysicalEntity;
import s3.util.Pair;
import java.util.*;


public class AStar {
	public S3 game;
	public S3Map map;
	S3PhysicalEntity entity;
	// variables to keep the start point and the goal
	double start_x, start_y, goal_x, goal_y;
	
	// lists to keep the tree of A*
	List<position> open;
	List<position> close;
	
	public static int pathDistance(double start_x, double start_y, double goal_x, double goal_y,
			S3PhysicalEntity i_entity, S3 the_game) {
		AStar a = new AStar(start_x,start_y,goal_x,goal_y,i_entity,the_game);
		List<Pair<Double, Double>> path = a.computePath();
		if (path!=null) return path.size();
		return -1;
	}

	public AStar(double start_x, double start_y, double goal_x, double goal_y,
			S3PhysicalEntity i_entity, S3 the_game) {
            // initialize the variables and make the list
			this.game = the_game;
			this.entity = i_entity;
			this.start_x = start_x;
			this.start_y = start_y;
			this.goal_x = goal_x;
			this.goal_y = goal_y;
			this.map = the_game.getMap();
			open = new ArrayList<position>();
			close = new ArrayList<position>();
	}

	public List<Pair<Double, Double>> computePath() {
		
			// make a list to keep the final path
            ArrayList<Pair<Double, Double>> path = new ArrayList<Pair<Double, Double>>();
			
            // add the starting point to the open list
            open.add(new position(start_x, start_y, goal_x, goal_y, 0, null));
			
            // while there is an element inside the open list
            // find next position to move that leads to goal
            while(!open.isEmpty()) {
            	// get the first element from the sorted open list
				position q = open.get(0);
				
				// the first element is now closed, so add it into the close list
				close.add(q);

				//System.out.println("("+q.x + ", "+ q.y+")");
				
				// if the current position is same as the goal position
				// end the while loop and return the path
				if(q.x==goal_x && q.y==goal_y) {
					//System.out.println("end");
					Pair<Double, Double> p = new Pair<Double, Double>(q.x, q.y);
					path.add(p);
					position parent = q.parent;
					while(parent!=null) {
						path.add(new Pair<Double, Double>(parent.x, parent.y));
						parent = parent.parent;
					}
					
					// reverse the order of path
					Collections.reverse(path);
					return path;
				}
				

				//left child
				// if there isn't any collision, the position is inside the map,
				// and the child doesn't exist inside open and close list
				// then push the child into the open list
				position child_l = new position(q.x-1, q.y, goal_x, goal_y, q.g+1, q);
				if(!already_exists(open, child_l) && !already_exists(close, child_l)) {
					if(child_l.x >=0 && child_l.y >= 0 && child_l.x < map.getWidth() && child_l.y < map.getHeight()){
						if(!map.anyLevelCollision(child_l.x, child_l.y) && game.entityAt((int)child_l.x, (int)child_l.y)==null) {
							//System.out.println("add left" + "("+child_l.x + ", "+ child_l.y+")");
							open.add(child_l);
						}
					}
				}
				
				//right child
				position child_r = new position(q.x+1, q.y, goal_x, goal_y, q.g+1, q);
				if(!already_exists(open, child_r) && !already_exists(close, child_r)) {
					if(child_r.x >=0 && child_r.y >= 0 && child_r.x < map.getWidth() && child_r.y < map.getHeight()){
						if(!map.anyLevelCollision(child_r.x, child_r.y) && game.entityAt((int)child_r.x, (int)child_r.y)==null) {
							//System.out.println("add right"+"("+child_r.x + ", "+ child_r.y+")");
							open.add(child_r);
						}
					}
				}
				
				//up child
				position child_u = new position(q.x, q.y+1, goal_x, goal_y, q.g+1, q);
				if(!already_exists(open, child_u) && !already_exists(close, child_u)) {
					if(child_u.x >=0 && child_u.y >= 0 && child_u.x < map.getWidth() && child_u.y < map.getHeight()){
						if(!map.anyLevelCollision(child_u.x, child_u.y) && game.entityAt((int)child_u.x, (int)child_u.y)==null) {
							//System.out.println("add up"+"("+child_u.x + ", "+ child_u.y+")");
							open.add(child_u);
						}
					}
				}
				
				//down child
				position child_d = new position(q.x, q.y-1, goal_x, goal_y, q.g+1, q);
				if(!already_exists(open, child_d) && !already_exists(close, child_d)) {
					if(child_d.x >=0 && child_d.y >= 0 && child_d.x < map.getWidth() && child_d.y < map.getHeight()){
						if(!map.anyLevelCollision(child_d.x, child_d.y) && game.entityAt((int)child_d.x, (int)child_d.y)==null) {
							//System.out.println("add down"+"("+child_d.x + ", "+ child_d.y+")");
							open.add(child_d);
						}
					}
				}
				
				// now remove the first element from the open list
				// since we pushed all of its children into the open list
				open.remove(q);
				
				// now the children is added into the list, sort it by f value
				Collections.sort(open);
			}
            return null;
	}
	
	// this method returns true if there already exists a same position
	// inside the open or close lists
	boolean already_exists(List<position> l, position p) {
		boolean flag = false;
		for(int i=0; i<l.size(); i++) {
			if(l.get(i).x == p.x && l.get(i).y == p.y) {
				flag = true;
			}
		}
		if(flag) {
			return true;
		}
		else {
			return false;
		}
	}
}
