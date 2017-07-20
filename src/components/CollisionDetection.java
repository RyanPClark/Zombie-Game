package components;

import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import toolbox.GameMath;

public class CollisionDetection {

	public static Vector2f collisionDirection(Vector2f startPosition, Vector2f endPosition, List<Vector2f> polygonOfIntersection){

		int size = polygonOfIntersection.size();
		
		Vector2f vector = new Vector2f(0,0);
		
		Vector2f p = startPosition;
		Vector2f r = new Vector2f(endPosition.x - startPosition.x, endPosition.y - startPosition.y);
		
		for (int i = 0; i < polygonOfIntersection.size(); i++){
			
			Vector2f q = new Vector2f(polygonOfIntersection.get(i));
			Vector2f s = new Vector2f(polygonOfIntersection.get((i+1)%size).x - polygonOfIntersection.get(i).x, 
					polygonOfIntersection.get((i+1)%size).y - polygonOfIntersection.get(i).y);
			
			float rXs = GameMath.crossProd(r, s);
			
			Vector2f qpDiff = new Vector2f(q.x - p.x, q.y - p.y);
			Vector2f secondVector = new Vector2f(s.x/rXs, s.y/rXs);
			
			float t = GameMath.crossProd((qpDiff), secondVector);
			float u = GameMath.crossProd((qpDiff), secondVector);
			
			if (rXs != 0 && t <= 1 && u <= 1 && t >= 0 && u >= 0){
				
				vector = new Vector2f (polygonOfIntersection.get((i+1)%size).x - polygonOfIntersection.get(i).x, 
						polygonOfIntersection.get((i+1)%size).y- polygonOfIntersection.get(i).y);
				//lines intersect at p + t*r = q + u*s - possible bullet ray particle system
					
				break;
			}
		}
		
		return vector;
		
	}
	
	public static Vector2f detectCollisions(List<List<Vector2f>> collisions, Vector3f position){
		
		Vector2f output = new Vector2f(0,0);
		
		for (int i = 0; i < collisions.size(); i++){
			for (int k = 2; k < collisions.get(i).size(); k++){
					
				List<Vector2f> currentPolygon = collisions.get(i);
				
				boolean barryCentric = GameMath.barryCentric(new Vector2f(position.x, position.z),
						currentPolygon.get(0), currentPolygon.get(k-1), currentPolygon.get(k), 0);
					
				if (barryCentric){
					output.x = 1;
					output.y = i;
					return output;
				}
			}
		}
		
		return output;
	}
}
