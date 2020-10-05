package s3.ai;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

import s3.base.S3;
import s3.base.S3Map;
import s3.entities.S3PhysicalEntity;
import s3.util.Pair;


public class AStar {
	public S3 game;
	public S3Map map;
	S3PhysicalEntity entity;
	double start_x, start_y, goal_x, goal_y;
	PriorityQueue<position> open = new PriorityQueue<position>();
	PriorityQueue<position> close = new PriorityQueue<position>();
	
	public static int pathDistance(double start_x, double start_y, double goal_x, double goal_y,
			S3PhysicalEntity i_entity, S3 the_game) {
		AStar a = new AStar(start_x,start_y,goal_x,goal_y,i_entity,the_game);
		List<Pair<Double, Double>> path = a.computePath();
		if (path!=null) return path.size();
		return -1;
	}

	public AStar(double start_x, double start_y, double goal_x, double goal_y,
			S3PhysicalEntity i_entity, S3 the_game) {
            // ...
			this.game = the_game;
			this.entity = i_entity;
			this.start_x = start_x;
			this.start_y = start_y;
			this.goal_x = goal_x;
			this.goal_y = goal_y;
			this.map = the_game.getMap();
	}

	public List<Pair<Double, Double>> computePath() {
            ArrayList<Pair<Double, Double>> path = new ArrayList<Pair<Double, Double>>();
			open.offer(new position(start_x, start_y, goal_x, goal_y, 0, null));
			while(!open.isEmpty()) {
				position q = open.poll();
				close.offer(q);
				
				if(q.x==goal_x && q.y==goal_y) {
					path.add(new Pair<Double, Double>(q.x, q.y));
					position parent = q.parent;
					while(parent!=null) {
						path.add(new Pair<Double, Double>(parent.x, parent.y));
						parent = parent.parent;
					}
					
					// reverse the order of path
					//Collections.reverse(path);
					return null;
				}
				
				//left
				position child_l = new position(q.x-1, q.y, goal_x, goal_y, q.g+1, q);
				if(!open.contains(child_l) && !close.contains(child_l)) {
					if(child_l.x >=0 && child_l.y >= 0 && child_l.x <= map.getWidth() && child_l.y <= map.getHeight()){
						if(!map.anyLevelCollision(child_l.x, child_l.y)) {
							open.offer(child_l);
						}
					}
				}
				
				//right
				position child_r = new position(q.x+1, q.y, goal_x, goal_y, q.g+1, q);
				if(!open.contains(child_r) && !close.contains(child_r)) {
					if(child_r.x >=0 && child_r.y >= 0 && child_r.x <= map.getWidth() && child_r.y <= map.getHeight()){
						if(!map.anyLevelCollision(child_r.x, child_r.y)) {
							open.offer(child_r);
						}
					}
				}
				
				//up
				position child_u = new position(q.x, q.y+1, goal_x, goal_y, q.g+1, q);
				if(!open.contains(child_u) && !close.contains(child_u)) {
					if(child_u.x >=0 && child_u.y >= 0 && child_u.x <= map.getWidth() && child_u.y <= map.getHeight()){
						if(!map.anyLevelCollision(child_u.x, child_u.y)) {
							open.offer(child_u);
						}
					}
				}
				
				//down
				position child_d = new position(q.x, q.y-1, goal_x, goal_y, q.g+1, q);
				if(!open.contains(child_d) && !close.contains(child_d)) {
					if(child_d.x >=0 && child_d.y >= 0 && child_d.x <= map.getWidth() && child_d.y <= map.getHeight()){
						if(!map.anyLevelCollision(child_d.x, child_d.y)) {
							open.offer(child_d);
						}
					}
				}
				
				
			}
            return null;
	}

}
