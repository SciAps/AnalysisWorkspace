package com.svm4j;

import java.util.ArrayList;
import java.util.List;

public class TrainingSet 
{
	private List<ClassifiedPoint> points;
	
	public TrainingSet()
	{
		points = new ArrayList<ClassifiedPoint>();
	}
	
	public void add(ClassifiedPoint point)
	{
		points.add(point);
	}

	public List<ClassifiedPoint> getPoints() 
	{
		return points;
	}

	public void setPoints(List<ClassifiedPoint> points) 
	{
		this.points = points;
	}
	
	
}
