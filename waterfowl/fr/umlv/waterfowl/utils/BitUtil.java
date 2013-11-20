package fr.umlv.waterfowl.utils;

public class BitUtil {

	 public static int pop(long x) {
		  /* Copied from Cassandra (org.apache.cassandra.utils.obs.BitUtil.java) 
		   * Hacker's Delight 32 bit pop function:
		   * http://www.hackersdelight.org/HDcode/newCode/pop_arrayHS.cc
		   *
		  int pop(unsigned x) {
		     x = x - ((x >> 1) & 0x55555555);
		     x = (x & 0x33333333) + ((x >> 2) & 0x33333333);
		     x = (x + (x >> 4)) & 0x0F0F0F0F;
		     x = x + (x >> 8);
		     x = x + (x >> 16);
		     return x & 0x0000003F;
		    }
		  ***/

		    // 64 bit java version of the C function from above
		    x = x - ((x >>> 1) & 0x5555555555555555L);
		    x = (x & 0x3333333333333333L) + ((x >>>2 ) & 0x3333333333333333L);
		    x = (x + (x >>> 4)) & 0x0F0F0F0F0F0F0F0FL;
		    x = x + (x >>> 8);
		    x = x + (x >>> 16);
		    x = x + (x >>> 32);
		    return ((int)x) & 0x7F;
		  }
	public static void main(String[] args) {
		long val = 984278289489229281L;
		System.out.println(val + "  "+Long.toBinaryString(val));
        System.out.println(pop(val));
	}

}
