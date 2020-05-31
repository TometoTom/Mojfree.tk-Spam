package spam;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kong.unirest.Unirest;
import net.andreinc.mockneat.MockNeat;
import net.andreinc.mockneat.types.enums.PassStrengthType;

public class Spam {

	public static ExecutorService executor = Executors.newCachedThreadPool();
	public static volatile int successes = 0;
	public static volatile int failures = 0;
	public static final String URL = "http://mojfree.tk/login.php";
	
	public static void main(String[] args) {
		
		while (true) {
			executor.execute(Spam::request);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (successes % 100 == 0 && successes != 0)
				p("Done " + successes + " requests.");
			if (failures > 250)
				p("Too many failures - shutting program down to prevent resource wastage");
		}
	}
	
	public static void request() {
		
		MockNeat mock = MockNeat.threadLocal();
		
		try {
			int status = Unirest.post("http://mojfree.tk/login.php")
					  .header("host", "mojfree.tk")
					  .header("connection", "keep-alive")
					  .header("pragma", "no-cache")
					  .header("cache-control", "no-cache")
					  .header("origin", "http://mojfree.tk")
					  .header("upgrade-insecure-requests", "1")
					  .header("dnt", "1")
					  .header("content-type", "application/x-www-form-urlencoded")
					  .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")
					  .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
					  .header("referer", "http://mojfree.tk/")
					  .header("accept-encoding", "gzip, deflate")
					  .header("accept-language", "en-US,en;q=0.9")
					  .queryString("username", mock.emails().get())
					  .queryString("password", mock.passwords().type(PassStrengthType.MEDIUM).get())
					  .queryString("authenticityToken", "7c33e4507aede5296e3dbbe32ec3fd35082acf37")
					  .asEmpty().getStatus();
			if (status == 302)
				successes++;
			else
				failures++;
		} catch (Exception e) {
			failures++;
			e.printStackTrace();
		}
	}
	
	public static String p(String msg) {
		System.out.println(msg);
		return msg;
	}
}
