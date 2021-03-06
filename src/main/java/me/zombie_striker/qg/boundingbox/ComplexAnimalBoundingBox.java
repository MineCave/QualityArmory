package me.zombie_striker.qg.boundingbox;

import me.zombie_striker.qg.handlers.BoundingBoxUtil;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class ComplexAnimalBoundingBox implements AbstractBoundingBox{

	private double bodyWidthRadius = 0.47;
	private double headWidthRadius = 0.47;

	private double bodyheight = 1.45;
	private double headTopHeight = 1.95;
	private double headBottomHeight = 1.95;

	private double headOffsetDistance = 0;



	public ComplexAnimalBoundingBox(double bodyheight, double bodyRadius,double headBottomHeight, double headTopHeight, double headRadius, double headOffsetDistance) {
		this.bodyheight = bodyheight;
		this.bodyWidthRadius = bodyRadius;
		this.headBottomHeight = headBottomHeight;
		this.headTopHeight = headTopHeight;
		this.headWidthRadius = headRadius;
		this.headOffsetDistance = headOffsetDistance;

	}
	
	@Override
	public boolean intersects(Entity shooter, Location check, Entity base) {
		if(intersectsBody(check, base))
			return true;
		return intersectsHead(check,base);
	}

	@Override
	public boolean allowsHeadshots() {
		return true;
	}

	@Override
	public boolean intersectsBody(Location check, Entity base) {
		return BoundingBoxUtil.within2D(base,check,bodyWidthRadius, bodyheight,bodyWidthRadius);
	}

	@Override
	public boolean intersectsHead(Location check, Entity base) {
		double cos = Math.cos(base.getLocation().getYaw()/180*Math.PI);
		double sin = Math.sin(base.getLocation().getYaw()/180*Math.PI);
		Vector newVal = new Vector((headOffsetDistance * cos) - (0 * sin), 0,
				(0 * cos) + (headOffsetDistance * sin));

		Location newCheck = check.clone();
		newCheck.subtract(newVal);
		return BoundingBoxUtil.within2D(base,newCheck,headWidthRadius,(headTopHeight-headBottomHeight),headBottomHeight,Math.max(bodyWidthRadius,headWidthRadius));
	}


	@Override
	public double maximumCheckingDistance(Entity base) {
		return Math.max(bodyWidthRadius,headWidthRadius+headOffsetDistance+bodyWidthRadius)*2;
	}

}
