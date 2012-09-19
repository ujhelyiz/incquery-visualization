package hu.bme.mit.emf.incquery.visualization.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.viatra2.patternlanguage.core.patternLanguage.Pattern;
import org.eclipse.viatra2.patternlanguage.core.patternLanguage.PatternCall;

public class CallGraphModel {
	private List<PatternElement> patterns;
	//private List<MyNode> bodies;
	public CallGraphModel()
	{
		patterns=new ArrayList<PatternElement>();
		//bodies=new ArrayList<MyNode>();
	}
	
	public void addPattern(Pattern pattern)
	{
		getPattern(pattern);
//		boolean l=false;
//		MyNode tmppattern=pattern;
//		for (MyNode n:patterns)
//		{
//			if (n.getName().equals(pattern.getName())) 
//			{
//				l=true;
//				tmppattern=n;
//			}
//		}
//		if (!l) patterns.add(pattern);
//		for (MyNode n:bodies)
//		{
//			if (n.equals(body)) 
//			{
//				MyConnection conn=new MyConnection(body.getName(),body,tmppattern);
//				if (negative) conn.setNegative(true);
//				body.getConnectedTo().add(conn);
//			}
//		}
	}
	public void addPatternCall(Pattern p,PatternCall pc,boolean b,int index)
	{
		PatternElement src=getPattern(p);
		PatternElement dst=getPattern(pc.getPatternRef());
		MyConnection conn=new MyConnection("Body: "+index,src,dst);
		conn.setNegative(b);
		boolean l=false;
		for (MyConnection item:src.getConnectedTo())
		{
			if ((item.getLabel().equals(conn.getLabel()))&&(dst.getName().equals(item.getDestination().getName()))) l=true;
		}
		if (!l) src.getConnectedTo().add(conn);
//		boolean l=false;
//		MyNode tmppattern=pattern;
//		for (MyNode n:patterns)
//		{
//			if (n.getName().equals(pattern.getName())) 
//			{
//				l=true;
//				tmppattern=n;
//			}
//		}
//		if (!l) patterns.add(pattern);
	}
//	public void addBodie(MyNode body,MyNode pattern)
//	{
//		bodies.add(body);
//		for (MyNode n:patterns)
//		{
//			if (n.equals(pattern)) 
//			{
//				MyConnection conn=new MyConnection(body.getName(),pattern,body);
//				pattern.getConnectedTo().add(conn);
//			}
//		}
//	}
	private PatternElement findPattern(Pattern p)
	{
		for (PatternElement pe:patterns)
		{
			if (pe.getName().equals(p.getName())) return pe;
		}
		return null;
	}
	private PatternElement getPattern(Pattern p)
	{
		PatternElement pe=findPattern(p);
		if (pe!=null) return pe;
		pe=new PatternElement(p.getName(),p);
		patterns.add(pe);
		return pe;
	}
	public List<PatternElement> getNodes()
	{
		List<PatternElement> list=new ArrayList<PatternElement>();
		list.addAll(patterns);
//		list.addAll(bodies);
		//for (PatternElement pe:list) System.out.println(pe.getName());
		return list;
	}

}
