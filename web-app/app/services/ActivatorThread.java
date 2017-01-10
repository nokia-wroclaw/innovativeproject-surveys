package services;

import java.util.Date;
import java.util.List;

import models.UnactivatedAccount;
import models.UserAccount;
import play.Logger;

public class ActivatorThread extends Thread implements Runnable {

	@Override
	public void run() {
		
		while(true) {
			

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}		
			
		    long intervalInMs = 5000*3600; // run every hour 
		    long nextRun = System.currentTimeMillis() + intervalInMs;
		    method();
		    if (nextRun > System.currentTimeMillis()) {
		        try {
					Thread.sleep(nextRun - System.currentTimeMillis());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		    }
		}
	}
	
	
	public void method(){
		
		List<UnactivatedAccount> unactiv = UnactivatedAccount.find.all();
		List<UserAccount> userAccounts = UserAccount.find.all();
		UserAccount expiredAccount;
	       
		
		Date dat = new Date(System.currentTimeMillis());
		if (unactiv != null) {
			Logger.info("Number of inactivated accounts " +  unactiv.size());
			Logger.info("Number of user accounts " +  userAccounts.size());
			for (int i = 0; i < unactiv.size(); i++) {
				if (unactiv.get(i).expiredDate.compareTo(dat) > 0) {  

				}else{
					expiredAccount = UserAccount.find.byId(unactiv.get(i).login);
					unactiv.get(i).delete();
					expiredAccount.delete();
					Logger.info("Expired Account Deleted");
		           
				}
			}
		} 
	}
	
	

}
