 package com.gnet.utils;
 
  
 public class L
 {
 
   public static void i(Object s)
   {
     System.out.println(s);
   }
 
   public static void i( Object... obj){
	   int i =1;
	  for(Object o:obj){
		  
		  i(i++ +" ="+ o );
		  
	  }
   }
   
   
   public static void err(Object s)
   {
     System.err.println(s);
   }
 }

