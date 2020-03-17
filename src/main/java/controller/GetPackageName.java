package controller;

public class GetPackageName {

	public static void main(String[] args) {

        

		 

		  // Create new object of this class

		        GetPackageName o = new GetPackageName();

		         

		 

		  // Get package name and print it

		        Package pack = o.getClass().getPackage();

		         

		        String packageName = pack.getName();

		         

		        System.out.println("Package = " + packageName);

		 

		    }

}
