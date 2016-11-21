package services;

import java.util.Date;
import java.util.List;

import models.UnactivatedAccount;
import play.Logger;

public class ActivatorThread extends Thread implements Runnable {

	@Override
	public void run() {
		
		while(true) {
			
			// TODO Auto-generated method stub
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}		
			
		    long intervalInMs = 3600*1000; // run every second
		    long nextRun = System.currentTimeMillis() + intervalInMs;
		    method();
		    if (nextRun > System.currentTimeMillis()) {
		        try {
					Thread.sleep(nextRun - System.currentTimeMillis());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		}
	}
	
	
	public void method(){
		
		List<UnactivatedAccount> unactiv = UnactivatedAccount.find.all();
	       
		
		Date dat = new Date(System.currentTimeMillis());
		if (unactiv != null) {
			Logger.info("Number of inactive accounts" +  unactiv.size());
			for (int i = 0; i < unactiv.size(); i++) {
				if (unactiv.get(i).expiredDate.compareTo(dat) > 0) {  
					
				}else{
					unactiv.get(i).delete();
					Logger.info("Deleted account");
		           
				
				}
			}
		} 
	}
	
	

}
